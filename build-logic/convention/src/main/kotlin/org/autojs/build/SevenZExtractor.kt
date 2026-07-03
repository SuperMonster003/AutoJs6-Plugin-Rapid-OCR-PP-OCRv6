package org.autojs.build

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry
import org.apache.commons.compress.archivers.sevenz.SevenZFile
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object SevenZExtractor {

    @JvmStatic
    fun extractDirectoryFrom7z(
        archive: File,
        sourceDir: String,
        outDir: File,
        shouldPrintProgress: Boolean = true
    ): Long {
        return try {
            extractDirectoryFrom7zWithCommons(archive, sourceDir, outDir, shouldPrintProgress)
        } catch (e: Throwable) {
            extractDirectoryFrom7zWithExternalTool(archive, sourceDir, outDir, e)
        }
    }

    private fun extractDirectoryFrom7zWithCommons(
        archive: File,
        sourceDir: String,
        outDir: File,
        shouldPrintProgress: Boolean = true
    ): Long {
        require(archive.isFile) { "7z archive not found: ${archive.absolutePath}" }
        if (!outDir.exists()) outDir.mkdirs()

        val sourceDirPath = normalizePrefix(sourceDir)
        var sevenZFile: SevenZFile? = null

        var totalBytes: Long
        var writtenBytes = 0L
        val buffer = ByteArray(64 * 1024)

        try {
            sevenZFile = SevenZFile.Builder().setFile(archive).get()
            val allEntries: Iterable<SevenZArchiveEntry> = sevenZFile.entries

            val targetEntries = allEntries.filter { e ->
                val entryPath = e.name.replace('\\', '/')
                entryPath.startsWith(sourceDirPath) ||
                        entryPath.startsWith(trimLeadingSlash(sourceDirPath))
            }

            totalBytes = targetEntries
                .filter { !it.isDirectory && it.size >= 0 && it.hasStream() }
                .sumOf { it.size }

            val entriesCount = targetEntries.size
            var processed = 0

            for (entry in targetEntries) {
                val entryPath = entry.name.replace('\\', '/')
                var relative = when {
                    entryPath.startsWith(sourceDirPath) ->
                        entryPath.substring(sourceDirPath.length)
                    entryPath.startsWith(trimLeadingSlash(sourceDirPath)) ->
                        entryPath.substring(trimLeadingSlash(sourceDirPath).length)
                    else -> {
                        processed++
                        continue
                    }
                }

                // Remove leading separator to avoid being treated as absolute path.
                // zh-CN: 去掉前导分隔符, 避免被当作绝对路径.
                while (relative.startsWith("/") || relative.startsWith("\\")) {
                    relative = relative.substring(1)
                }
                if (relative.isEmpty()) {
                    processed++
                    continue
                }

                val outFile = File(outDir, relative)
                if (entry.isDirectory) {
                    outFile.mkdirs()
                } else {
                    if (!entry.hasStream()) {
                        processed++
                        continue
                    }
                    outFile.parentFile?.mkdirs()
                    var ins: InputStream? = null
                    var bos: BufferedOutputStream? = null
                    try {
                        ins = sevenZFile.getInputStream(entry)
                        bos = BufferedOutputStream(FileOutputStream(outFile))
                        while (true) {
                            val read = ins.read(buffer)
                            if (read == -1) break
                            bos.write(buffer, 0, read)
                            if (shouldPrintProgress && totalBytes > 0) {
                                writtenBytes += read
                                printProgress(writtenBytes, totalBytes)
                            }
                        }
                        bos.flush()
                        if (entry.size >= 0 && outFile.length() != entry.size) {
                            throw IllegalStateException(
                                "Extracted file size mismatch for: ${entry.name}, expected=${entry.size}, actual=${outFile.length()}"
                            )
                        }
                    } finally {
                        try { bos?.close() } catch (_: Throwable) {}
                        try { ins?.close() } catch (_: Throwable) {}
                    }
                }

                if (shouldPrintProgress && totalBytes == 0L) {
                    val pct = processed * 100.0 / entriesCount
                    printProgress(pct)
                }
                processed++
            }
            return totalBytes
        } finally {
            try { sevenZFile?.close() } catch (_: Throwable) {}
        }
    }

    private fun extractDirectoryFrom7zWithExternalTool(
        archive: File,
        sourceDir: String,
        outDir: File,
        cause: Throwable
    ): Long {
        val javaCliFailure = try {
            extractDirectoryFrom7zWithJavaCli(archive, sourceDir, outDir, cause)?.let { return it }
            null
        } catch (e: Throwable) {
            println("Isolated Java 7z extractor failed; trying external 7z executable")
            e
        }

        val sevenZip = findSevenZipExecutable() ?: throw IllegalStateException(
            "Failed to initialize bundled 7z extractor and no external 7z executable was found",
            javaCliFailure ?: cause
        )
        println("Bundled 7z extractor failed; using external 7z executable: $sevenZip")
        val sourceDirPath = trimLeadingSlash(normalizePrefix(sourceDir))
        val sourceDirPathWithoutTrailingSlash = sourceDirPath.trimEnd('/')
        val staging = File(outDir.parentFile ?: outDir, "${outDir.name}-7z-staging")

        if (staging.exists()) staging.deleteRecursively()
        if (outDir.exists()) outDir.deleteRecursively()
        staging.mkdirs()
        outDir.mkdirs()

        try {
            val process = ProcessBuilder(
                sevenZip,
                "x",
                "-y",
                archive.absolutePath,
                "$sourceDirPath*",
                "-o${staging.absolutePath}"
            ).redirectErrorStream(true).start()

            val output = process.inputStream.bufferedReader().use { it.readText() }
            val exitCode = process.waitFor()
            if (exitCode != 0) {
                throw IllegalStateException("External 7z extraction failed with exit code $exitCode\n$output", javaCliFailure ?: cause)
            }

            val extractedRoot = File(staging, sourceDirPathWithoutTrailingSlash)
            val sourceRoot = extractedRoot.takeIf { it.exists() } ?: staging
            var totalBytes = 0L
            sourceRoot.walkTopDown().filter { it.isFile }.forEach { totalBytes += it.length() }
            sourceRoot.copyRecursively(outDir, overwrite = true)
            return totalBytes
        } finally {
            staging.deleteRecursively()
        }
    }

    private fun extractDirectoryFrom7zWithJavaCli(
        archive: File,
        sourceDir: String,
        outDir: File,
        cause: Throwable
    ): Long? {
        val ownCodeSource = SevenZExtractor::class.java.protectionDomain.codeSource?.location
            ?.toURI()
            ?.let(::File)
            ?.takeIf { it.exists() }
            ?: return null
        val commonsCompressJar = SevenZFile::class.java.protectionDomain.codeSource?.location
            ?.toURI()
            ?.let(::File)
            ?.takeIf { it.exists() }
            ?: return null
        val xzJar = findXzJar() ?: return null
        val commonsIoJar = findGradleModuleJar("commons-io/commons-io", "commons-io", "2.20.0")
            ?: return null
        val commonsLangJar = findGradleModuleJar("org.apache.commons/commons-lang3", "commons-lang3", "3.18.0")
        val javaExecutable = File(
            File(System.getProperty("java.home"), "bin"),
            if (System.getProperty("os.name").contains("Windows", ignoreCase = true)) "java.exe" else "java"
        )

        if (outDir.exists()) outDir.deleteRecursively()
        outDir.mkdirs()

        val classpath = listOf(
            ownCodeSource.absolutePath,
            commonsCompressJar.absolutePath,
            commonsIoJar.absolutePath,
            commonsLangJar?.absolutePath,
            xzJar.absolutePath,
        ).filterNotNull().joinToString(File.pathSeparator)

        println("Bundled 7z extractor failed; using isolated Java extractor with xz: ${xzJar.absolutePath}")
        val process = ProcessBuilder(
            javaExecutable.absolutePath,
            "-cp",
            classpath,
            "org.autojs.build.SevenZExtractorCli",
            archive.absolutePath,
            sourceDir,
            outDir.absolutePath
        ).redirectErrorStream(true).start()

        val output = process.inputStream.bufferedReader().use { it.readText() }
        val exitCode = process.waitFor()
        if (exitCode != 0) {
            throw IllegalStateException("Isolated Java 7z extraction failed with exit code $exitCode\n$output", cause)
        }

        return outDir.walkTopDown().filter { it.isFile }.sumOf { it.length() }
    }

    private fun findXzJar(): File? {
        findGradleModuleJar("org.tukaani/xz", "xz", "1.10")?.let { return it }
        return findGradleModuleJar("org.tukaani/xz", "xz")
    }

    private fun findGradleModuleJar(
        groupPath: String,
        artifact: String,
        preferredVersion: String? = null
    ): File? {
        val gradleUserHome = System.getenv("GRADLE_USER_HOME")
            ?.takeUnless { it.isBlank() }
            ?.let(::File)
            ?: File(System.getProperty("user.home"), ".gradle")
        val moduleRoot = File(gradleUserHome, "caches/modules-2/files-2.1/$groupPath")
        val preferred = preferredVersion?.let { version ->
            File(moduleRoot, version)
                .walkTopDownOrEmpty()
                .firstOrNull { it.isFile && it.name == "$artifact-$version.jar" }
        }
        return preferred ?: moduleRoot.walkTopDownOrEmpty()
            .filter { it.isFile && it.name.matches(Regex("""$artifact-\d+(?:\.\d+)*\.jar""")) }
            .sortedByDescending { it.name }
            .firstOrNull()
    }

    private fun File.walkTopDownOrEmpty(): Sequence<File> =
        if (exists()) walkTopDown().asSequence() else emptySequence()

    private fun findSevenZipExecutable(): String? {
        val commonWindowsPaths = listOf(
            "C:\\Program Files\\7-Zip\\7z.exe",
            "C:\\Program Files (x86)\\7-Zip\\7z.exe",
        ).filter { File(it).isFile }
        val candidates = commonWindowsPaths + listOf("7z", "7zz", "7za")
        return candidates.firstOrNull { canRunSevenZip(it) }
    }

    private fun canRunSevenZip(executable: String): Boolean {
        return try {
            ProcessBuilder(executable, "i")
                .redirectOutput(ProcessBuilder.Redirect.DISCARD)
                .redirectError(ProcessBuilder.Redirect.DISCARD)
                .start()
                .waitFor() == 0
        } catch (_: Throwable) {
            false
        }
    }

    private fun normalizePrefix(path: String): String {
        var p = File(path).path.replace('\\', '/')
        if (p.startsWith("/")) p = p.substring(1)
        if (!p.endsWith("/")) p += "/"
        return p
    }

    private fun trimLeadingSlash(s: String): String =
        if (s.startsWith("/")) s.substring(1) else s

    private fun printProgress(written: Long, total: Long) {
        val pct = if (total > 0) written * 100.0 / total else 0.0
        printProgress(pct)
    }

    private fun printProgress(percent: Double) {
        val width = 30
        val filled = ((percent / 100.0) * width).toInt().coerceIn(0, width)
        val bar = buildString {
            append("[").append("#".repeat(filled)).append("-".repeat(width - filled)).append("]")
        }
        print("\rExtracting... $bar ${"%.2f".format(percent)}%")
        System.out.flush()
    }

}
