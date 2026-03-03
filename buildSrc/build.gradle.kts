plugins {
    `kotlin-dsl`
}

group = "com.redmadrobot.build"


tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
}

java {
    targetCompatibility = JavaVersion.VERSION_11
    sourceCompatibility = JavaVersion.VERSION_11
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
}
