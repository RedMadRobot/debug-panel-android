import internal.Versions
import internal.stack
import kotlinx.validation.KotlinApiBuildTask
import kotlinx.validation.KotlinApiCompareTask
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

/*
 * Public ABI validation for Android library modules.
 *
 * The reference dump lives in `<module>/api/<module>.api` and is verified by `check`.
 * Run `./gradlew apiDump` to update dumps after an intentional API change.
 *
 * Note: the ABI validation built into the Kotlin Gradle plugin (`kotlin { abiValidation { } }`)
 * cannot be used here. KGP registers its setup actions only from `org.jetbrains.kotlin.android`,
 * and since AGP 9 provides Kotlin support itself that plugin must not be applied — so the DSL is
 * present but inert. The tasks are wired manually instead; the dump format is identical, so the
 * files stay valid once the built-in validation becomes usable.
 */

val abiTools = configurations.dependencyScope("abiTools")
val abiToolsClasspath = configurations.resolvable("abiToolsClasspath") {
    extendsFrom(abiTools.get())
}

dependencies {
    add(abiTools.name, "org.ow2.asm:asm:${Versions.ASM}")
    add(abiTools.name, "org.ow2.asm:asm-tree:${Versions.ASM}")
    add(abiTools.name, "org.jetbrains.kotlin:kotlin-metadata-jvm:${stack.versions.kotlin.get()}")
}

val apiFileName = "${project.name}.api"
val referenceApiDir = layout.projectDirectory.dir("api")
// Taken from the compile tasks rather than from `kotlin.target.compilations`: with AGP's built-in
// Kotlin support the compilation outputs are not populated, so the collection would come out empty.
val releaseClasses = files(
    provider { tasks.named<KotlinJvmCompile>("compileReleaseKotlin").flatMap { it.destinationDirectory } },
    provider { tasks.named<JavaCompile>("compileReleaseJavaWithJavac").flatMap { it.destinationDirectory } },
)

val apiBuild = tasks.register<KotlinApiBuildTask>("apiBuild") {
    description = "Dumps the public ABI of the 'release' variant into the build directory."
    runtimeClasspath.from(abiToolsClasspath)
    inputClassesDirs.from(releaseClasses)
    outputApiFile.set(layout.buildDirectory.file("api/$apiFileName"))
    nonPublicMarkers.add("com.redmadrobot.debug.core.annotation.DebugPanelInternal")
    ignoredClasses.addAll(composableSingletons(releaseClasses))
}

/*
 * Names of the holder classes the Compose compiler generates, one per file, for the composable
 * lambdas of that file which capture nothing. They are `public` in bytecode and carry no Kotlin
 * metadata to filter on, so `nonPublicMarkers` and `internal` have no effect on them -- yet they
 * are pure codegen and no consumer can call them. Left in the dump, every new such lambda would
 * show up as a public API change.
 *
 * The names are collected from the compiled classes rather than listed by hand: `ignoredClasses`
 * matches exact names only, so a hardcoded list would need an entry per Compose file.
 */
fun composableSingletons(classes: FileCollection): Provider<List<String>> = providers.provider {
    classes.files.filter(File::isDirectory).flatMap { root ->
        root.walkTopDown()
            .filter { it.isFile && it.extension == "class" && it.name.startsWith("ComposableSingletons$") }
            .map { it.relativeTo(root).path.removeSuffix(".class").replace(File.separatorChar, '.') }
            .toList()
    }
}

val apiCheck = tasks.register<KotlinApiCompareTask>("apiCheck") {
    description = "Checks that the public ABI matches the reference dump in the 'api' directory."
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    projectApiFile.set(referenceApiDir.file(apiFileName))
    generatedApiFile.set(apiBuild.flatMap { it.outputApiFile })
}

tasks.register<Sync>("apiDump") {
    description = "Overwrites the reference ABI dump with the ABI of the current code."
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    from(apiBuild.flatMap { it.outputApiFile })
    into(referenceApiDir)
}

tasks.named(LifecycleBasePlugin.CHECK_TASK_NAME) {
    dependsOn(apiCheck)
}
