object Versions {

    const val KOTLIN = "1.6.10"

    const val COROUTINES = "1.3.9"
    const val OKHTTP = "4.9.0"

    const val APP_COMPAT = "1.1.0"
    const val DESIGN = "1.5.0-alpha02"
    const val CONSTRAINT_LAYOUT = "1.1.3"
    const val RECYCLERVIEW = "1.1.0"
    const val ANNOTATION = "1.1.0"
    const val CUSTOMTABS = "1.2.0"

    const val APP_DISTRIBUTION = "1.3.1"
    const val FIREBASE_CRASHLYTICS = "2.0.0-beta04"

    const val LIFECYCLE = "2.2.0"
    const val LIFECYCLE_RUNTIME = "2.4.0"
    const val LIFECYCLE_TESTING = "2.1.0"

    const val ROOM = "2.4.0"
    const val NAVIGATION = "2.2.0"

    const val GLIDE = "4.11.0"
    const val ITEMS_ADAPTER = "1.1"

    const val ANDROID_KTX = "1.1.0"
    const val TIMBER = "4.7.1"
    const val FLIPPER = "2.0.0"
}

object Dependencies {
    object kotlin {
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.KOTLIN}"
        const val test = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.KOTLIN}"
    }

    const val coroutines_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINES}"
    const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.OKHTTP}"

    object androidx {
        const val app_compat = "androidx.appcompat:appcompat:${Versions.APP_COMPAT}"
        const val design = "com.google.android.material:material:${Versions.DESIGN}"
        const val constraint_layout = "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT}"
        const val recyclerview = "androidx.recyclerview:recyclerview:${Versions.RECYCLERVIEW}"
        const val annotations = "androidx.annotation:annotation:${Versions.ANNOTATION}"
        const val customtabs = "androidx.browser:browser:${Versions.CUSTOMTABS}"
    }

    object firebase {
        const val app_distribution = "com.google.firebase:firebase-appdistribution-gradle:${Versions.APP_DISTRIBUTION}"
        const val firebase_crashlytics =
            "com.google.firebase:firebase-crashlytics-gradle:${Versions.FIREBASE_CRASHLYTICS}"
    }

    object lifecycle {
        const val extensions = "androidx.lifecycle:lifecycle-extensions:${Versions.LIFECYCLE}"
        const val runtime = "androidx.lifecycle:lifecycle-runtime:${Versions.LIFECYCLE}"
        const val lifecycle_livedata = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.LIFECYCLE}"
        const val lifecycle_livedata_core = "androidx.lifecycle:lifecycle-livedata-core-ktx:${Versions.LIFECYCLE}"
        const val lifecycle_view_model = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFECYCLE}"
        const val lifecycle_runtime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.LIFECYCLE_RUNTIME}"
        const val testing = "androidx.arch.core:core-testing:${Versions.LIFECYCLE_TESTING}"
    }


    object room {
        const val ktx = "androidx.room:room-ktx:${Versions.ROOM}"
        const val runtime = "androidx.room:room-runtime:${Versions.ROOM}"
        const val compiler = "androidx.room:room-compiler:${Versions.ROOM}"
    }

    object navigation {
        const val plugin = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.NAVIGATION}"
        const val fragment_ktx = "androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION}"
        const val ui_ktx = "androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION}"
    }

    object ui {
        const val items_adapter = "com.redmadrobot.itemsadapter:itemsadapter-viewbinding:${Versions.ITEMS_ADAPTER}"
    }

    object tools {
        const val android_ktx = "androidx.core:core-ktx:${Versions.ANDROID_KTX}"
    }


    object debugging {
        const val timber = "com.jakewharton.timber:timber:${Versions.TIMBER}"
    }

    object feature_toggles {
        const val flipper = "com.redmadrobot:flipper:${Versions.FLIPPER}"
    }
}