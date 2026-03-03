object Project {
    const val MIN_SDK = 23
    const val TARGET_SDK = 36
    const val COMPILE_SDK = 36

    object BuildTypes {
        const val release = "release"
    }

    object Proguard {
        const val androidOptimizedRules = "proguard-android-optimize.txt"
        const val projectRules = "proguard-rules.pro"
    }
}
