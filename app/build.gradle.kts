import com.android.build.api.variant.FilterConfiguration
import org.gradle.api.provider.Property

plugins {
    id("org.autojs.build.utils")
    id("org.autojs.build.versions")
    id("org.autojs.build.signs")
    id("org.autojs.build.jvm-convention")
    id("com.android.application")
    id("io.github.supermonster003.autojs6-native-alignment")
}

val globalApplicationId = "io.github.supermonster003.autojs6.plugin.rapidocr.ppocrv6"

val buildTypeDebug = "debug"
val buildTypeRelease = "release"

android {

    namespace = globalApplicationId
    compileSdk = versions.sdkVersionCompile

    defaultConfig {
        applicationId = globalApplicationId

        minSdk = versions.sdkVersionMin
        targetSdk = versions.sdkVersionTarget

        versionCode = versions.appVersionCode
        versionName = versions.appVersionName

        resValue("string", "app_name", "Rapid OCR (PP-OCRv6 Small)")
        resValue("string", "plugin_author", "SuperMonster003")
        resValue("string", "plugin_id", "rapid-ocr-pp-ocrv6")
        resValue("string", "plugin_engine", "rapid-ocr")
        resValue("string", "plugin_variant", "pp-ocrv6")
        resValue("string", "plugin_version_date", utils.getDateString("MMM d, yyyy", "GMT+08:00"))

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        multiDexEnabled = true
        multiDexKeepProguard = file("multidex-keep.pro")

        ndk {
            abiFilters += listOf("arm64-v8a", "armeabi-v7a", "x86", "x86_64")
        }
    }

    lint {
        abortOnError = false
    }

    signingConfigs {
        if (signs.isValid) {
            create(buildTypeRelease) {
                storeFile = signs.properties["storeFile"]?.let { file(it as String) }
                keyPassword = signs.properties["keyPassword"] as String
                keyAlias = signs.properties["keyAlias"] as String
                storePassword = signs.properties["storePassword"] as String
            }
        }
    }

    buildTypes {
        val proguardFiles = arrayOf<Any>(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro",
        )
        val niceSigningConfig = takeIf { signs.isValid }?.let {
            signingConfigs.getByName(buildTypeRelease)
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(*proguardFiles)
            niceSigningConfig?.let { signingConfig = it }
        }
        release {
            isMinifyEnabled = true
            proguardFiles(*proguardFiles)
            niceSigningConfig?.let { signingConfig = it }
        }
    }

    buildFeatures {
        aidl = true
        buildConfig = true
        resValues = true
    }

    @Suppress("DEPRECATION")
    packagingOptions {
        jniLibs.useLegacyPackaging = true

        listOf(
            "META-INF/DEPENDENCIES",
            "META-INF/LICENSE",
            "META-INF/LICENSE.*",
            "META-INF/LICENSE-notice.*",
            "META-INF/license.*",
            "META-INF/NOTICE",
            "META-INF/NOTICE.*",
            "META-INF/notice.*",
            "META-INF/ASL2.0",
            "META-INF/*.kotlin_module",
        ).let { resources.pickFirsts.addAll(it) }

        listOf(
            "com/**/*",
            "frameworks/**/*",
            "junit/**/*",
            "LICENSE-junit.txt",
            "spec.txt",
            "EmojiReference.txt",
        ).let { resources.excludes.addAll(it) }
    }

    splits {
        abi {
            isEnable = true
            reset()
            include("arm64-v8a", "armeabi-v7a", "x86", "x86_64")
            isUniversalApk = true
        }
    }
}

androidComponents {
    onVariants { variant ->
        variant.outputs.forEach { output ->
            val architecture = output.filters.find {
                it.filterType == FilterConfiguration.FilterType.ABI
            }?.identifier ?: "universal"
            val outputFileNameProperty = output.javaClass.methods.firstOrNull {
                it.name == "getOutputFileName" && it.parameterTypes.isEmpty()
            }?.invoke(output) as? Property<*>

            @Suppress("UNCHECKED_CAST")
            (outputFileNameProperty as? Property<String>)?.set(
                output.versionName.map { versionName ->
                    val version = versionName.replace("\\s".toRegex(), "-")
                    val extension = utils.FILE_EXTENSION_APK
                    "${rootProject.name}-v$version-$architecture.$extension".lowercase()
                }
            )
        }
    }
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test:runner:1.7.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.2.21")
    implementation("org.jetbrains:annotations:26.0.2")

    implementation(files("$rootDir/libs/common-plugin-api.aar"))
    implementation(files("$rootDir/libs/paddle-ocr-api.aar"))

    implementation(project(":libs:rapidocr"))

    implementation(libs.annotation)
}

tasks {
    withType(JavaCompile::class.java) {
        options.encoding = "UTF-8"
    }


}

extra {
    versions.handleIfNeeded(project, listOf(buildTypeDebug, buildTypeRelease))
}

apply(from = rootProject.file("gradle/release-archive.gradle"))
