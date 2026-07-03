package org.autojs.build;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public final class SevenZExtractorCli {

    private SevenZExtractorCli() {
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            throw new IllegalArgumentException("Usage: SevenZExtractorCli <archive> <sourceDir> <outDir>");
        }
        extractDirectory(new File(args[0]), args[1], new File(args[2]));
    }

    private static void extractDirectory(File archive, String sourceDir, File outDir) throws Exception {
        if (!archive.isFile()) {
            throw new IllegalArgumentException("7z archive not found: " + archive.getAbsolutePath());
        }
        if (!outDir.exists() && !outDir.mkdirs()) {
            throw new IllegalStateException("Failed to create output directory: " + outDir.getAbsolutePath());
        }

        String sourceDirPath = normalizePrefix(sourceDir);
        byte[] buffer = new byte[64 * 1024];

        try (SevenZFile sevenZFile = SevenZFile.builder().setFile(archive).get()) {
            for (SevenZArchiveEntry entry : sevenZFile.getEntries()) {
                String entryPath = entry.getName().replace('\\', '/');
                String relative;
                if (entryPath.startsWith(sourceDirPath)) {
                    relative = entryPath.substring(sourceDirPath.length());
                } else if (entryPath.startsWith(trimLeadingSlash(sourceDirPath))) {
                    relative = entryPath.substring(trimLeadingSlash(sourceDirPath).length());
                } else {
                    continue;
                }

                while (relative.startsWith("/") || relative.startsWith("\\")) {
                    relative = relative.substring(1);
                }
                if (relative.isEmpty()) {
                    continue;
                }

                File outFile = safeResolve(outDir, relative);
                if (entry.isDirectory()) {
                    outFile.mkdirs();
                    continue;
                }
                if (!entry.hasStream()) {
                    continue;
                }
                File parent = outFile.getParentFile();
                if (parent != null) {
                    parent.mkdirs();
                }
                try (
                        InputStream input = sevenZFile.getInputStream(entry);
                        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(outFile))
                ) {
                    int read;
                    while ((read = input.read(buffer)) != -1) {
                        output.write(buffer, 0, read);
                    }
                }
                if (entry.getSize() >= 0 && outFile.length() != entry.getSize()) {
                    throw new IllegalStateException(
                            "Extracted file size mismatch for: " + entry.getName()
                                    + ", expected=" + entry.getSize()
                                    + ", actual=" + outFile.length()
                    );
                }
            }
        }
    }

    private static File safeResolve(File outDir, String relative) throws Exception {
        File outFile = new File(outDir, relative);
        String outDirPath = outDir.getCanonicalPath();
        String outFilePath = outFile.getCanonicalPath();
        if (!outFilePath.equals(outDirPath) && !outFilePath.startsWith(outDirPath + File.separator)) {
            throw new IllegalStateException("Refusing to extract outside output directory: " + relative);
        }
        return outFile;
    }

    private static String normalizePrefix(String path) {
        String p = new File(path).getPath().replace('\\', '/');
        if (p.startsWith("/")) {
            p = p.substring(1);
        }
        if (!p.endsWith("/")) {
            p += "/";
        }
        return p;
    }

    private static String trimLeadingSlash(String value) {
        return value.startsWith("/") ? value.substring(1) : value;
    }
}
