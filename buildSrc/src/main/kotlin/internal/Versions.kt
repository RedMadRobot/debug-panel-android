package internal

internal object Versions {
    const val MIN_SDK = 23
    const val TARGET_SDK = 36
    const val COMPILE_SDK = 37

    /** Bytecode reader used by binary-compatibility-validator workers. Keep in sync with the plugin's own version. */
    const val ASM = "9.6"
}
