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
 * The panel's own machinery is not part of the contract and is skipped: declarations from the
 * [INTERNAL_TYPE_PREFIXES] packages, from `internal`/`ui` packages, and every member that mentions
 * such a type, is Compose-related, or is a `kotlinx.serialization` synthetic. That is exactly the
 * surface a plugin implementation uses and an application does not.
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
        val expected = declarations(dumps.flatMap(::parse), dropInternal = true)
        val actual = declarations(parse(noopDump.get().asFile), dropInternal = false)

        val problems = buildList {
            (expected - actual.keys).values.forEach { declaration ->
                add("Missing in $noopModule:\n" + render(declaration).prependIndent("  "))
            }
            (actual - expected.keys).values.forEach { declaration ->
                add("Not part of the mirrored public API:\n" + render(declaration).prependIndent("  "))
            }
            expected.keys.intersect(actual.keys).forEach { name ->
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
         * Types that are the panel's own machinery, not part of the no-op contract: `panel-core`
         * packages meant for plugin implementations, Compose, and serialization synthetics.
         */
        val INTERNAL_TYPE_PREFIXES = listOf(
            "com/redmadrobot/debug/core/annotation/",
            "com/redmadrobot/debug/core/extension/",
            "com/redmadrobot/debug/core/inapp/",
            "com/redmadrobot/debug/core/plugin/",
            "com/redmadrobot/debug/uikit/",
            "androidx/compose/",
            "kotlinx/serialization/",
        )

        /** Package segments marking declarations internal to the panel, e.g. plugin screens. */
        val INTERNAL_PACKAGE_SEGMENTS = setOf("internal", "ui")

        /** Keywords a member line starts with, right after its modifiers. */
        val MEMBER_KEYWORDS = listOf("fun ", "field ")

        /** Matches a class reference inside a JVM descriptor, e.g. `Lcom/redmadrobot/debug/Foo;`. */
        val TYPE_REGEX = Regex("""L([\w/$]+);""")

        /** Synthetic field added by the Compose compiler; the no-op module has no Compose. */
        const val STABLE_FIELD = " field \$stable "

        fun isInternal(type: String): Boolean {
            return INTERNAL_TYPE_PREFIXES.any(type::startsWith) ||
                type.split('/').dropLast(1).any { it in INTERNAL_PACKAGE_SEGMENTS }
        }

        /** Member without its modifiers, so that an override matches the declaration it overrides. */
        fun signature(member: String): String {
            val keyword = MEMBER_KEYWORDS.firstOrNull { it in member } ?: return member
            return member.substring(member.indexOf(keyword))
        }

        fun isInternal(member: String, dropped: Set<String>): Boolean {
            return STABLE_FIELD in member ||
                TYPE_REGEX.findAll(member).any { it.groupValues[1].let { type -> isInternal(type) || type in dropped } }
        }

        /**
         * Splits an ABI dump into declarations: a header line plus its indented members.
         * Compiler-generated (`synthetic`) classes are skipped, as are the members that are not
         * part of the no-op contract.
         */
        fun parse(file: File): List<Declaration> {
            return file.readLines()
                .filterNot { it.isBlank() || it.startsWith("//") }
                .fold(mutableListOf<MutableList<String>>()) { blocks, line ->
                    if (line.first().isWhitespace()) blocks.last() += line.trim() else blocks += mutableListOf(line)
                    blocks
                }
                .mapNotNull(::parseDeclaration)
        }

        fun parseDeclaration(block: List<String>): Declaration? {
            val header = block.first().removeSuffix(" {")
            val modifiers = header.substringBefore("class ").trim()
            if ("synthetic" in modifiers.split(' ')) return null

            val declaration = header.substringAfter("class ")
            val name = declaration.substringBefore(" : ")
            val supertypes = declaration.substringAfter(" : ", missingDelimiterValue = "")
                .split(", ")
                .filter(String::isNotEmpty)
            val publicSupertypes = supertypes.filterNot(::isInternal).sorted()

            return Declaration(
                name = name,
                header = "$modifiers class $name" +
                    if (publicSupertypes.isEmpty()) "" else publicSupertypes.joinToString(", ", prefix = " : "),
                supertypes = supertypes,
                members = block.drop(1).filterNot { isInternal(it, dropped = emptySet()) }.toSet(),
            )
        }

        /**
         * Members a declaration only has because it implements a panel-internal supertype, e.g.
         * `Plugin.getName()`. They are a part of the plugin machinery rather than of the API the
         * application calls, so the no-op module does not repeat them. Constructors are kept:
         * they are not inherited.
         */
        fun inheritedFromInternal(declaration: Declaration, index: Map<String, Declaration>): Set<String> {
            val inherited = mutableSetOf<String>()

            fun collect(name: String) {
                val supertype = index[name] ?: return
                supertype.members
                    .filterNot { "fun <init> " in it }
                    .mapTo(inherited, ::signature)
                supertype.supertypes.forEach(::collect)
            }

            declaration.supertypes.filter(::isInternal).forEach(::collect)
            return inherited
        }

        /**
         * Indexes declarations by name, dropping the ones the no-op module does not have to
         * mirror. A companion left without members (it only held a serializer, for example) is
         * dropped together with the field referencing it.
         */
        fun declarations(all: List<Declaration>, dropInternal: Boolean): Map<String, Declaration> {
            val index = all.associateBy(Declaration::name)
            val kept = if (dropInternal) all.filterNot { isInternal(it.name) } else all
            val emptyCompanions = kept
                .filter { it.name.endsWith("\$Companion") && it.members.isEmpty() }
                .mapTo(mutableSetOf()) { it.name }

            return kept
                .filterNot { it.name in emptyCompanions }
                .associateBy(
                    keySelector = Declaration::name,
                    valueTransform = { declaration ->
                        val inherited = inheritedFromInternal(declaration, index)
                        declaration.copy(
                            members = declaration.members
                                .filterNot { signature(it) in inherited || isInternal(it, emptyCompanions) }
                                .toSet(),
                        )
                    },
                )
                .toSortedMap()
        }

        fun diff(expected: Declaration, actual: Declaration, noopModule: String): String? {
            val missingMembers = expected.members - actual.members
            val extraMembers = actual.members - expected.members
            if (expected.header == actual.header && missingMembers.isEmpty() && extraMembers.isEmpty()) return null

            return buildString {
                appendLine("${expected.name} differs:")
                if (expected.header != actual.header) {
                    appendLine("  declaration:")
                    appendLine("    expected: ${expected.header}")
                    appendLine("    actual:   ${actual.header}")
                }
                if (missingMembers.isNotEmpty()) {
                    appendLine("  missing in $noopModule:")
                    missingMembers.sorted().forEach { appendLine("    $it") }
                }
                if (extraMembers.isNotEmpty()) {
                    appendLine("  not part of the mirrored public API:")
                    extraMembers.sorted().forEach { appendLine("    $it") }
                }
            }.trimEnd()
        }

        fun render(declaration: Declaration): String {
            val members = declaration.members.sorted().joinToString(separator = "") { "\n\t$it" }
            return "${declaration.header} {$members\n}"
        }
    }

    private data class Declaration(
        val name: String,
        val header: String,
        val supertypes: List<String>,
        val members: Set<String>,
    )
}
