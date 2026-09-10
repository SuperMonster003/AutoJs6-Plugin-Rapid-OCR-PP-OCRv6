enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "autojs6-plugin-rapid-ocr-pp-ocrv6"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
    plugins {
        id("io.github.supermonster003.autojs6-platform-versions") version "1.7.4"
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
