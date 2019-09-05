package com.iisi.deploymail.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DateUtil {

    static String formatDate(String pattern, LocalDateTime localDateTime) {
        DateTimeFormatter.ofPattern(pattern).format(localDateTime)
    }

    static LocalDateTime newLocalDateFormTimeZone(LocalDateTime localDateTime, ZoneId zoneId) {
        LocalDateTime.ofInstant(Instant.now(), zoneId);
    }

    static String formatNowUtc8Date(String pattern) {
        def localTime = newLocalDateFormTimeZone(LocalDateTime.now(), ZoneId.of('UTC+8'))
        formatDate(pattern, localTime)
    }

}
