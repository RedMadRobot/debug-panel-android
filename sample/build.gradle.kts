plugins {
    id(Plugins.Android.applicationPlugin)
    kotlin(Plugins.Kotlin.androidPlugin)
}

android {
    compileSdk = Project.COMPILE_SDK
    namespace = "com.redmadrobot.debugpanel"

    defaultConfig {
        minSdk = Project.MIN_SDK
        targetSdk = Project.TARGET_SDK
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = androidx.versions.compose.compiler.get()
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(androidx.appcompat)
    implementation(androidx.compose.runtime)
    implementation(stack.material)
    implementation(androidx.constraintlayout)
    implementation(rmr.flipper)
    implementation(stack.timber)
    implementation(stack.kotlinx.coroutines.android)
    implementation(androidx.lifecycle.runtime)
    debugImplementation(project(":core"))
    debugImplementation(project(":plugins:servers"))

    // Debug panel dependencies
    debugImplementation(project(":plugins:accounts"))
    debugImplementation(project(":plugins:app-settings"))
    debugImplementation(project(":plugins:flipper"))
    debugImplementation(project(":plugins:variable"))
//    debugImplementation("com.redmadrobot.debug:panel-core:${project.version}")
//    debugImplementation("com.redmadrobot.debug:accounts-plugin:${project.version}")
//    debugImplementation("com.redmadrobot.debug:servers-plugin:${project.version}")
//    debugImplementation("com.redmadrobot.debug:app-settings-plugin:${project.version}")
//    debugImplementation("com.redmadrobot.debug:flipper-plugin:${project.version}")
//    debugImplementation("com.redmadrobot.debug:variable-plugin:${project.version}")

    //No-op dependency
    releaseImplementation(project(":no-op"))
//    releaseImplementation("com.redmadrobot.debug:panel-no-op:${project.version}")

    implementation(stack.retrofit)
}
