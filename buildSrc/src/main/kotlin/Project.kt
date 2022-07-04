import org.gradle.kotlin.dsl.provideDelegate
import java.io.File
import java.util.*

object Project {
    const val MIN_SDK = 21
    const val TARGET_SDK = 31
    const val COMPILE_SDK = 31

    private val versionProperties = Properties().apply {
        load(File("gradle/version.properties").inputStream())
    }

    val versionCod: Int = versionProperties.getProperty("version_code").toInt()
    val versionName: String = Properties().apply {
            load(File("gradle/version.properties").inputStream())
        }.getProperty("version_name")


    object BuildTypes {
        const val release = "release"
    }

    object Proguard {
        const val androidOptimizedRules = "proguard-android-optimize.txt"
        const val projectRules = "proguard-rules.pro"
    }
}