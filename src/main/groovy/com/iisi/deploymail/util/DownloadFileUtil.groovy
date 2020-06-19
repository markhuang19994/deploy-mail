package com.iisi.deploymail.util

import org.slf4j.LoggerFactory

final class DownloadFileUtil {
    private static final LOGGER = LoggerFactory.getLogger(DownloadFileUtil.class)

    private DownloadFileUtil() {
        throw new AssertionError()
    }

    static Map<String, File> downloadFiles(List<String> sourceUrls, File destDir) {
        def result = new LinkedHashMap()
        sourceUrls.each { sourceUrl ->
            def filename = sourceUrl.tokenize('/')[-1]
            try {
                def file = new File("$destDir", "$filename")

                def protocolUrlTokens = sourceUrl.tokenize(':')
                def protocolTokenLen = protocolUrlTokens.size()
                def sourceUrlAsURI = new URI(
                        protocolUrlTokens[0],
                        protocolUrlTokens[1..(protocolTokenLen - 1)].join(":"), ""
                )

                def fos
                def out
                try {
                    fos = new FileOutputStream(file)
                    out = new BufferedOutputStream(fos)

                    out << sourceUrlAsURI.toURL().openStream()
                    result[filename] = file
                    LOGGER.debug("${sourceUrlAsURI.toURL().toString()} download success.")
                } finally {
                    if (fos) {
                        fos.close()
                    }
                    if (out) {
                        out.close()
                    }
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e)
                result[filename] = null
            }
        }
        result
    }

    static void main(String[] args) {
        def files = downloadFiles([
                "http://macaque:6080/jenkins/view/CITI_UAT/job/Deploy/job/CITI_PCL_PHASE_TEST/112/artifact/deploy/mailTemplate.txt",
        ], FileUtil.getTempFolder())
        println files
    }
}
