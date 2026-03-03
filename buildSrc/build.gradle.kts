import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.redmadrobot.build"


tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
}

java {
    targetCompatibility = JavaVersion.VERSION_17
    sourceCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(rmr.infrastructure.publish)
    implementation(rmr.infrastructure.android)
    implementation(libs.publish.gradlePlugin)
    implementation(stack.gradle.android.cacheFixGradlePlugin)
    implementation(stack.kotlin.gradlePlugin)
    implementation(stack.kotlin.composeCompiler.gradlePlugin)
    implementation(stack.detekt.gradlePlugin)
    implementation(stack.android.tools.build.gradle)

    // Hack-around to access version catalogs inside precompiled script plugins
    // See: https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
    implementation(files(androidx.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(files(stack.javaClass.superclass.protectionDomain.codeSource.location))
}
