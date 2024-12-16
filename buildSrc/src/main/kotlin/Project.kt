object Project {
    const val MIN_SDK = 21
    const val TARGET_SDK = 35
    const val COMPILE_SDK = 35

    object BuildTypes {
        const val release = "release"
    }

    object Proguard {
        const val androidOptimizedRules = "proguard-android-optimize.txt"
        const val projectRules = "proguard-rules.pro"
    }
}
