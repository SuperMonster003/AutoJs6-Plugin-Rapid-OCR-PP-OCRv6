import java.net.URI
import java.security.MessageDigest

/**
 * Rapid OCR (https://github.com/RapidAI/RapidOCR) build script (Kotlin DSL).
 *
 * Created by SuperMonster003 on Sep 19, 2024.
 * Transformed by SuperMonster003 on Sep 30, 2025.
 */

plugins {
    id("org.autojs.build.utils")
    id("org.autojs.build.properties")
    id("org.autojs.build.jvm-convention")
    id("com.android.library")
    id("kotlin-parcelize")
}

ext {
    set("projectName", "Rapid OCR (PP-OCRv6 Small)")
}

val onnxRuntimeReleaseVersion = props["RAPID_OCR/ONNX_RUNTIME"]
val onnxRuntimeAssetVersion = props["RAPID_OCR/ONNX_RUNTIME_ASSET"]

val versionMap = mapOf(
    "OFFICIAL_NAME" to "1.3.0", /* From original build.gradle file. */
    "MIN_SDK" to props["MIN_SDK"].toInt(),
    "COMPILE_SDK" to props["COMPILE_SDK"].toInt(),
    "TARGET_SDK" to props["TARGET_SDK"].toInt(),
    "NDK" to props["RAPID_OCR/NDK"],
    "CMAKE" to props["RAPID_OCR/CMAKE"],
    "OPENCV_MOBILE" to props["RAPID_OCR/OPENCV_MOBILE"],
    "OPENCV_MOBILE_LABEL" to props["RAPID_OCR/OPENCV_MOBILE_LABEL"],
    "ONNX_RUNTIME" to onnxRuntimeReleaseVersion,
    "ONNX_RUNTIME_ASSET" to onnxRuntimeAssetVersion,
)

val nameMap = mapOf(
    "PROJECT" to extensions.extraProperties["projectName"] as String,
    "OPENCV_MOBILE" to "OpenCV Mobile",
    "OPENCV_MOBILE_LABEL" to "OpenCV Mobile Label",
    "ONNX_RUNTIME" to "Onnx Runtime",
    "ONNX_RUNTIME_ASSET" to "Onnx Runtime Asset",
    "NDK" to "NDK",
    "CMAKE" to "Cmake",
)

val libsToDeploy = listOf(
    utils.newLibDeployer(
        project,
        nameMap["OPENCV_MOBILE"] as String,
        "https://github.com/nihui/opencv-mobile/releases/download/v${versionMap["OPENCV_MOBILE_LABEL"]}/opencv-mobile-${versionMap["OPENCV_MOBILE"]}-android.zip",
    ).apply {
        setSourceDir("/opencv-mobile-${versionMap["OPENCV_MOBILE"]}-android/sdk/native/")
        setDestDir("/src/sdk/native/")
    },
    utils.newLibDeployer(
        project,
        nameMap["ONNX_RUNTIME"] as String,
        "https://github.com/RapidAI/OnnxruntimeBuilder/releases/download/${versionMap["ONNX_RUNTIME"]}/onnxruntime-${versionMap["ONNX_RUNTIME_ASSET"]}-android-shared.7z",
    ).apply {
        setSourceDir("/onnxruntime-android-shared/")
        setDestDir("/src/main/onnxruntime-shared/")
    },
)

data class RapidOcrRemoteAsset(
    val fileName: String,
    val url: String,
    val sha256: String? = null,
)

fun File.sha256String(): String {
    val digest = MessageDigest.getInstance("SHA-256")
    inputStream().use { input ->
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        while (true) {
            val read = input.read(buffer)
            if (read < 0) {
                break
            }
            digest.update(buffer, 0, read)
        }
    }
    return digest.digest().joinToString("") { "%02x".format(it.toInt() and 0xff) }
}

val rapidOcrV6Assets = listOf(
    RapidOcrRemoteAsset(
        fileName = "PP-OCRv6_det_small.onnx",
        url = "https://www.modelscope.cn/models/RapidAI/RapidOCR/resolve/v3.9.1/onnx/PP-OCRv6/det/PP-OCRv6_det_small.onnx",
        sha256 = "090f04abcd9d9a7498bc4ebf677e4cb9bdce1fe4197ddb7e529f1ef44e1ff94f",
    ),
    RapidOcrRemoteAsset(
        fileName = "PP-OCRv6_rec_small.onnx",
        url = "https://www.modelscope.cn/models/RapidAI/RapidOCR/resolve/v3.9.1/onnx/PP-OCRv6/rec/PP-OCRv6_rec_small.onnx",
        sha256 = "6f327246b50388f3c176ae304bd95767ea6dc0c9ae92153ef8cbe210b3c14884",
    ),
    RapidOcrRemoteAsset(
        fileName = "ch_ppocr_mobile_v2.0_cls_mobile.onnx",
        url = "https://www.modelscope.cn/models/RapidAI/RapidOCR/resolve/v3.9.1/onnx/PP-OCRv4/cls/ch_ppocr_mobile_v2.0_cls_mobile.onnx",
        sha256 = "e47acedf663230f8863ff1ab0e64dd2d82b838fceb5957146dab185a89d6215c",
    ),
    RapidOcrRemoteAsset(
        fileName = "ppocrv6_dict.txt",
        url = "https://www.modelscope.cn/models/RapidAI/RapidOCR/resolve/v3.9.1/paddle/PP-OCRv6/rec/PP-OCRv6_rec_small/ppocrv6_dict.txt",
        sha256 = "b5f2bfe2bdd9448429e3e82b51c789775d9b42f2403d082b00662eb77e401c5d",
    ),
)

val rapidOcrAbis = listOf("armeabi-v7a", "arm64-v8a", "x86_64", "x86")

tasks.register("downloadRapidOcrV6Models") {
    group = "rapidocr"
    description = "Download PP-OCRv6 ONNX model assets used by Rapid OCR (PP-OCRv6 Small)."

    doLast {
        val modelDir = layout.projectDirectory.dir("src/main/assets/models").asFile
        modelDir.mkdirs()

        modelDir.listFiles { file ->
            file.name in setOf(
                "ch_PP-OCRv3_det_infer.onnx",
                "ch_PP-OCRv3_rec_infer.onnx",
                "ch_ppocr_mobile_v2.0_cls_infer.onnx",
                "ppocr_keys_v1.txt",
            ) || (file.name.startsWith("project-rapidocronnx-") && file.name.endsWith(".skip"))
        }?.forEach { obsolete ->
            logger.lifecycle("Deleting obsolete RapidOCR asset: ${obsolete.name}")
            obsolete.delete()
        }

        rapidOcrV6Assets.forEach { asset ->
            val target = modelDir.resolve(asset.fileName)
            val shouldDownload = !target.exists() ||
                    asset.sha256?.let { expected -> !target.sha256String().equals(expected, ignoreCase = true) } == true

            if (shouldDownload) {
                logger.lifecycle("Downloading RapidOCR asset: ${asset.fileName}")
                URI(asset.url).toURL().openStream().use { input ->
                    target.outputStream().use { output -> input.copyTo(output) }
                }
            }

            asset.sha256?.let { expected ->
                val actual = target.sha256String()
                require(actual.equals(expected, ignoreCase = true)) {
                    "SHA-256 mismatch for ${asset.fileName}: expected=$expected actual=$actual"
                }
            }
        }
    }
}

tasks.register("syncOnnxRuntimeJniLibs") {
    group = "rapidocr"
    description = "Copy ONNX Runtime shared libraries into Android jniLibs packaging layout."

    doLast {
        val sourceRoot = layout.projectDirectory.dir("src/main/onnxruntime-shared").asFile
        val targetRoot = layout.projectDirectory.dir("src/main/sharedLibs").asFile
        targetRoot.deleteRecursively()

        rapidOcrAbis.forEach { abi ->
            val source = sourceRoot.resolve("$abi/lib/libonnxruntime.so")
            require(source.isFile) {
                "Missing ONNX Runtime shared library for $abi: ${source.absolutePath}"
            }
            val target = targetRoot.resolve("$abi/libonnxruntime.so")
            target.parentFile.mkdirs()
            source.copyTo(target, overwrite = true)
        }
    }
}

tasks.named("preBuild").configure {
    dependsOn("downloadRapidOcrV6Models")
    dependsOn("syncOnnxRuntimeJniLibs")
}

utils.configureLibraryLifecycleHooks(
    project,
    nameMap["PROJECT"] as String,
    listOf("OPENCV_MOBILE", "OPENCV_MOBILE_LABEL", "ONNX_RUNTIME", "ONNX_RUNTIME_ASSET", "NDK", "CMAKE")
        .map { "${nameMap[it] ?: it}: ${versionMap[it]}" },
    libsToDeploy,
    "isCleanupRapidOcr",
    listOf(".cxx"),
)

android {

    namespace = "com.benjaminwan.ocrlibrary"
    version = versionMap["OFFICIAL_NAME"] as String

    ndkVersion = versionMap["NDK"] as String
    compileSdk = versionMap["COMPILE_SDK"] as Int

    defaultConfig {
        minSdk = versionMap["MIN_SDK"] as Int

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        externalNativeBuild {
            cmake {
                abiFilters += rapidOcrAbis
            }
        }
    }

    sourceSets {
        getByName("main") {
            jniLibs.directories.add("src/main/sharedLibs")
        }
    }

    lint {
        targetSdk = versionMap["TARGET_SDK"] as Int
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = versionMap["CMAKE"] as String
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))

    testImplementation(libs.junit)
    androidTestImplementation(libs.test.ext.junit)
    androidTestImplementation(libs.test.espresso.core)

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
}
