package com.iisi.deploymail.util

import net.lingala.zip4j.core.ZipFile
import net.lingala.zip4j.exception.ZipException
import sun.misc.BASE64Encoder

import java.nio.charset.StandardCharsets
import java.nio.file.Files

class FileUtil {

    private static final TEMP_FOLDER = new File(System.properties['java.io.tmpdir'] as String)

    static File createTempFile(String content, String charset) {
        def tempFile = new File(TEMP_FOLDER, "temp_${System.currentTimeMillis()}")
        tempFile.write(content, charset)
        tempFile
    }

    static File createTempFile(String content) {
        createTempFile(content, StandardCharsets.UTF_8.name())
    }

    static File getTempFolder() {
        TEMP_FOLDER
    }

    static String toBase64Encoding(File file) throws IOException {
        new BASE64Encoder().encode(Files.readAllBytes(file.toPath()))
    }

    static List<File> getAllFilesInDirectory(File dir) {
        if (dir == null || !dir.isDirectory()) return new ArrayList<>()
        List<File> result = new ArrayList<>()
        File[] files = dir.listFiles()
        if (files != null) {
            for (File file : files) {
                if (file.isFile())
                    result.add(file)
                else if (file.isDirectory())
                    result.addAll(getAllFilesInDirectory(file))
            }
        }
        result
    }

    static File unZipProtectedFiles(File sourceFile, File destFile, String password) throws ZipException {
        ZipFile zipFile = new ZipFile(sourceFile)
        if (zipFile.isEncrypted()) {
            zipFile.setPassword(password)
        }
        zipFile.extractAll(destFile.getAbsolutePath())
        return destFile
    }

}
