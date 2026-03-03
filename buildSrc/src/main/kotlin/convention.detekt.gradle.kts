import internal.stack

plugins {
    id("io.gitlab.arturbosch.detekt")
}

detekt {
    config.from(rootProject.files("config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
    parallel = true
    autoCorrect = true
}

dependencies {
    detektPlugins(stack.detekt.formatting)
    detektPlugins(stack.detekt.rules.compose)
}
