package internal

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import java.io.File

/**
 * Verifies that the no-op module stays a drop-in replacement for the real ones: every public
 * declaration of the mirrored modules (`panel-core` and the plugins) must be repeated in the no-op
 * module under the same fully qualified name and with the same members, so that swapping
 * `debugImplementation` for `releaseImplementation` keeps the consumer code compiling.
 *
 * The comparison is made on the ABI dumps produced by `convention.abi.validation`, so any change
 * of the public API fails the build until it is mirrored in the no-op module (or hidden from the
 * dump by making the declaration `internal`).
 *
 * Only two kinds of declarations are skipped, and both are named explicitly: what a compiler
 * plugin generates ([ARTIFACT_TYPE_PREFIXES], `synthetic` classes and the Compose `$stable`
 * field), and the handful of [EXCLUDED_DECLARATIONS] that make up the plugin authoring surface
 * rather than the application one. Members mentioning any of those types are skipped too, as are
 * the members a declaration only has because it implements an excluded supertype. Everything else
 * is compared, so a new public declaration is covered by default.
 *
 * The `ComposableSingletons$*` holders never reach this task: `convention.abi.validation` keeps
 * them out of the dumps themselves.
 */
abstract class CheckNoopApiTask : DefaultTask() {

    /** ABI dump of the no-op module. */
    @get:InputFile
    @get:PathSensitive(PathSensitivity.NONE)
    abstract val noopDump: RegularFileProperty

    /** ABI dumps of the modules the no-op module must mirror. */
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.NONE)
    abstract val mirroredDumps: ConfigurableFileCollection

    @TaskAction
    fun check() {
        val dumps = mirroredDumps.files.sortedBy(File::getPath)
        check(dumps.isNotEmpty()) { "No ABI dumps to compare the no-op module against." }
        val absentDumps = dumps.filterNot(File::isFile)
        check(absentDumps.isEmpty()) {
            "ABI dumps are not generated yet, run './gradlew apiDump':\n" +
                absentDumps.joinToString("\n") { "  $it" }
        }

        val noopModule = noopDump.get().asFile.nameWithoutExtension
        val expected = contract(dumps.flatMap(::parseDump), dropExcluded = true)
        val actual = contract(parseDump(noopDump.get().asFile), dropExcluded = false)

        val problems = buildList {
            for (name in expected.keys - actual.keys) {
                add("Missing in $noopModule:\n" + render(expected.getValue(name)).prependIndent("  "))
            }
            for (name in actual.keys - expected.keys) {
                add("Not part of the mirrored public API:\n" + render(actual.getValue(name)).prependIndent("  "))
            }
            for (name in expected.keys intersect actual.keys) {
                diff(expected.getValue(name), actual.getValue(name), noopModule)?.let(::add)
            }
        }
        if (problems.isEmpty()) return

        error(
            buildString {
                appendLine("$noopModule does not cover the public API of the mirrored modules.")
                appendLine()
                appendLine(
                    "Mirror the declarations below in $noopModule keeping the original package, " +
                        "or make them `internal` if they are not meant for library consumers. " +
                        "Run './gradlew apiDump' afterwards to update the ABI dumps."
                )
                appendLine()
                append(problems.joinToString("\n\n"))
            },
        )
    }

    private companion object {

        /**
         * Types the no-op module cannot repeat because they come from a compiler plugin it does
         * not apply: Compose and `kotlinx.serialization`. Mirroring them would mean pulling both
         * into release builds, which is exactly what the no-op module exists to avoid.
         */
        val ARTIFACT_TYPE_PREFIXES = listOf(
            "androidx/compose/",
            "kotlinx/serialization/",
        )

        /**
         * Declarations deliberately left out of the no-op contract. Listed one by one rather than
         * by package, so that anything else appearing in the same packages fails the check instead
         * of slipping through unnoticed.
         */
        val EXCLUDED_DECLARATIONS = setOf(
            // The base class a plugin implementation extends, together with the optional interface
            // for plugins with a settings screen. Not mirrorable even in principle: `content()` and
            // `settingsContent()` are `@Composable`, so a real subclass would force the no-op module
            // to depend on the Compose runtime just to match the method signature -- exactly what
            // panel-no-op exists to keep out of release builds.
            //
            // It is also unnecessary: application code never references `Plugin` as a type. It calls
            // concrete plugin constructors directly (`DebugPanel.initialize(app, listOf(ServersPlugin(...)))`),
            // and `DebugPanel.initialize` takes `List<Any>` on the no-op side -- Kotlin's `List<out T>`
            // covariance accepts any `List<SomePlugin>` there without a shared supertype. Each plugin
            // module is mirrored individually instead (see e.g. panel-no-op's own `ServersPlugin`,
            // which does not extend anything).
            "com/redmadrobot/debug/core/plugin/Plugin",
            "com/redmadrobot/debug/core/internal/EditablePlugin",
            // Opt-in marker for the panel's own machinery.
            "com/redmadrobot/debug/core/annotation/DebugPanelInternal",
            // `internal` helper kept public in bytecode by `@PublishedApi` because the public
            // `inline fun <reified T> getPlugin()` calls it; not callable from outside.
            "com/redmadrobot/debug/core/extension/PluginsExtKt",
        )

        /** Keywords a member line starts with, right after its modifiers. */
        val MEMBER_KEYWORDS = listOf("fun ", "field ")

        /** Matches a class reference inside a JVM descriptor, e.g. `Lcom/redmadrobot/debug/Foo;`. */
        val TYPE_REGEX = Regex("""L([\w/$]+);""")

        /** Synthetic field added by the Compose compiler; the no-op module has no Compose. */
        const val STABLE_FIELD = " field \$stable "

        // ---------------------------------------------------------------------------------------
        // Step 1: split a dump into declarations. Every line is kept as written -- nothing here
        // decides what belongs in the no-op contract, that is entirely [contract]'s job.
        // ---------------------------------------------------------------------------------------

        /** One class/interface/annotation block of an ABI dump. */
        data class Declaration(
            val name: String,
            val modifiers: String,
            val supertypes: List<String>,
            val members: Set<String>,
        )

        /** Groups a dump's lines into blocks: an unindented header line and its indented members. */
        fun splitIntoBlocks(file: File): List<List<String>> {
            val blocks = mutableListOf<MutableList<String>>()
            for (line in file.readLines()) {
                if (line.isBlank() || line.startsWith("//")) continue
                if (line.first().isWhitespace()) blocks.last() += line.trim() else blocks += mutableListOf(line)
            }
            return blocks
        }

        fun parseBlock(block: List<String>): Declaration {
            val header = block.first().removeSuffix(" {")
            val modifiers = header.substringBefore("class ").trim()
            val declaration = header.substringAfter("class ")
            val name = declaration.substringBefore(" : ")
            val supertypes = declaration.substringAfter(" : ", missingDelimiterValue = "")
                .split(", ")
                .filter(String::isNotEmpty)
            return Declaration(name, modifiers, supertypes, members = block.drop(1).toSet())
        }

        fun parseDump(file: File): List<Declaration> = splitIntoBlocks(file).map(::parseBlock)

        // ---------------------------------------------------------------------------------------
        // Step 2: apply the no-op contract -- decide what a declaration/member is exempt from.
        // ---------------------------------------------------------------------------------------

        fun isExcludedType(type: String): Boolean {
            return type in EXCLUDED_DECLARATIONS || ARTIFACT_TYPE_PREFIXES.any(type::startsWith)
        }

        /** A member is exempt if it mentions an excluded/artifact type, directly or via [alsoExclude]. */
        fun mentionsExcludedType(member: String, alsoExclude: Set<String>): Boolean {
            if (STABLE_FIELD in member) return true
            for (match in TYPE_REGEX.findAll(member)) {
                val type = match.groupValues[1]
                if (isExcludedType(type) || type in alsoExclude) return true
            }
            return false
        }

        /** Member without its modifiers, so that an override matches the declaration it overrides. */
        fun signature(member: String): String {
            val keyword = MEMBER_KEYWORDS.firstOrNull { it in member } ?: return member
            return member.substring(member.indexOf(keyword))
        }

        /**
         * Signatures a declaration only has because it implements an excluded supertype, e.g.
         * `Plugin.getName()`. They are part of the plugin machinery rather than of the API the
         * application calls, so the no-op module does not repeat them. Constructors are excluded
         * from this walk: they are never inherited.
         */
        fun inheritedFromExcludedSupertypes(declaration: Declaration, index: Map<String, Declaration>): Set<String> {
            val inherited = mutableSetOf<String>()
            val toVisit = ArrayDeque(declaration.supertypes.filter(::isExcludedType))
            while (toVisit.isNotEmpty()) {
                val supertype = index[toVisit.removeFirst()] ?: continue
                for (member in supertype.members) {
                    if ("fun <init> " !in member) inherited += signature(member)
                }
                toVisit += supertype.supertypes
            }
            return inherited
        }

        /**
         * Applies the no-op contract to a set of raw declarations. On the mirrored side
         * (`dropExcluded`), declarations named in [EXCLUDED_DECLARATIONS] are dropped outright. On
         * both sides, a declaration's members are reduced to the ones that are part of the
         * contract: not mentioning an excluded/artifact type, and not inherited from an excluded
         * supertype. A companion left with no members this way (it only held a `serializer()`, for
         * example) is dropped together with the field that points at it.
         */
        fun contract(all: List<Declaration>, dropExcluded: Boolean): Map<String, Declaration> {
            val index = all.associateBy(Declaration::name)

            val kept = all
                .filterNot { "synthetic" in it.modifiers.split(' ') }
                .filterNot { dropExcluded && isExcludedType(it.name) }

            fun contractMembers(declaration: Declaration, alsoExclude: Set<String>): Set<String> {
                val inherited = inheritedFromExcludedSupertypes(declaration, index)
                return declaration.members.filterNotTo(mutableSetOf()) {
                    signature(it) in inherited || mentionsExcludedType(it, alsoExclude)
                }
            }

            val emptyCompanions = kept
                .filter { it.name.endsWith("\$Companion") }
                .filter { contractMembers(it, alsoExclude = emptySet()).isEmpty() }
                .mapTo(mutableSetOf(), Declaration::name)

            return kept
                .filterNot { it.name in emptyCompanions }
                .associate { it.name to it.copy(members = contractMembers(it, emptyCompanions)) }
                .toSortedMap()
        }

        // ---------------------------------------------------------------------------------------
        // Step 3: compare. Declaration sets are compared by key; member sets, by plain set diff.
        // ---------------------------------------------------------------------------------------

        /** The declaration's header with excluded supertypes (e.g. `Plugin`) removed. */
        fun publicHeader(declaration: Declaration): String {
            val publicSupertypes = declaration.supertypes.filterNot(::isExcludedType).sorted()
            val suffix = if (publicSupertypes.isEmpty()) "" else publicSupertypes.joinToString(", ", prefix = " : ")
            return "${declaration.modifiers} class ${declaration.name}$suffix"
        }

        fun diff(expected: Declaration, actual: Declaration, noopModule: String): String? {
            val expectedHeader = publicHeader(expected)
            val actualHeader = publicHeader(actual)
            val missingMembers = expected.members - actual.members
            val extraMembers = actual.members - expected.members
            if (expectedHeader == actualHeader && missingMembers.isEmpty() && extraMembers.isEmpty()) return null

            return buildString {
                appendLine("${expected.name} differs:")
                if (expectedHeader != actualHeader) {
                    appendLine("  declaration:")
                    appendLine("    expected: $expectedHeader")
                    appendLine("    actual:   $actualHeader")
                }
                if (missingMembers.isNotEmpty()) {
                    appendLine("  missing in $noopModule:")
                    for (member in missingMembers.sorted()) appendLine("    $member")
                }
                if (extraMembers.isNotEmpty()) {
                    appendLine("  not part of the mirrored public API:")
                    for (member in extraMembers.sorted()) appendLine("    $member")
                }
            }.trimEnd()
        }

        fun render(declaration: Declaration): String {
            val members = declaration.members.sorted().joinToString(separator = "") { "\n\t$it" }
            return "${publicHeader(declaration)} {$members\n}"
        }
    }
}
