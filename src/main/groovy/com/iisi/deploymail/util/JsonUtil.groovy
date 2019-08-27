package com.iisi.deploymail.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory

final class JsonUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class)

    private JsonUtil() {
        new AssertionError()
    }

    static <T> T parseJson(String json, Class<T> cls) {
        try {
            return new ObjectMapper().readValue(json, cls)
        } catch (Exception e) {
            e.printStackTrace()
            LOGGER.error('Parse json error:{}\n{}', e.getMessage(), json)
        }
        null
    }
}
