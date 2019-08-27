package com.iisi.deploymail.util

import freemarker.template.Template
import freemarker.template.TemplateException

final class FreemarkerUtil {
    private FreemarkerUtil() {
        new AssertionError()
    }

    private static void processTemplate(Template template, Object dataModel, Writer out) throws IOException, TemplateException {
        try {
            template.process(dataModel, out)
        } finally {
            out.flush()
            out.close()
        }
    }

    static String processTemplateToString(Template template, Object dataModel) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter()
        processTemplate(template, dataModel, stringWriter)
        return stringWriter.toString()
    }
}
