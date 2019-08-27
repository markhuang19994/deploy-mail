package com.iisi.deploymail.tool

import org.springframework.core.io.ClassPathResource

class HtmlResource {

    def final prefix
    def final suffix

    HtmlResource(prefix, suffix) {
        this.prefix = prefix
        this.suffix = suffix
    }

    def get(String name) {
        new ClassPathResource("${prefix}/${name}${suffix}").getFile().text
    }
}