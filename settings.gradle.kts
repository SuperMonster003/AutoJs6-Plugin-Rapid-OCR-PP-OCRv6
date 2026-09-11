enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "autojs6-plugin-rapid-ocr-pp-ocrv6"

pluginManagement {
    providers.gradleProperty("autojs.buildPlugins.includeBuild").orNull?.let { includeBuild(it) }
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
    plugins {
        id("io.github.supermonster003.autojs6-platform-versions") version "1.8.0"
        id("io.github.supermonster003.autojs6-native-alignment") version "1.8.0"
        id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    }
}

plugins {
    id("io.github.supermonster003.autojs6-platform-versions")
    id("org.gradle.toolchains.foojay-resolver-convention")
}

includeBuild("build-logic")

private val libs = listOf(
    "rapidocr",
)

include(
    ":app",
    *libs.map { ":libs:$it" }.toTypedArray(),
)
