plugins {
    id(Plugins.Android.libraryPlagin)
    kotlin(Plugins.Kotlin.androidPlugin)
    kotlin(Plugins.Kotlin.androidExtensions)
    kotlin(Plugins.Kotlin.kapt)
}

android {

    compileSdk = Project.COMPILE_SDK

    defaultConfig {
        minSdk = Project.MIN_SDK
        targetSdk = Project.TARGET_SDK

        consumerProguardFile("consumer-rules.pro")
    }

    buildTypes {
        getByName(Project.BuildTypes.release) {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile(Project.Proguard.androidOptimizedRules),
                Project.Proguard.projectRules
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += "-Xexplicit-api=strict"
    }

    java.sourceSets.create("src.main.kotlin")

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    api(Dependencies.kotlin.stdlib)
    api(Dependencies.okhttp)
    api(Dependencies.androidx.app_compat)
    api(Dependencies.androidx.design)
    api(Dependencies.androidx.constraint_layout)

    api(Dependencies.coroutines_android)

    api(Dependencies.lifecycle.runtime)
    api(Dependencies.lifecycle.extensions)
    api(Dependencies.lifecycle.lifecycle_livedata)
    api(Dependencies.lifecycle.lifecycle_livedata_core)
    api(Dependencies.lifecycle.lifecycle_view_model)
    api(Dependencies.room.runtime)
    api(Dependencies.room.ktx)

    api(Dependencies.tools.android_ktx)
    api(Dependencies.ui.items_adapter)

    api(Dependencies.feature_toggles.flipper)

    api(Dependencies.debugging.timber)
//    implementation fileTree (dir: 'libs', include: ['*.jar'])
    kapt(Dependencies.room.compiler)
}


tasks.register("wrapper", Wrapper::class) {
    setGradleVersion( "7.0.0")
}

tasks.register("prepareKotlinBuildScriptModel") {}
