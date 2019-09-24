package com.iisi.deploymail.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.iisi.deploymail.service.BgImageService
import org.springframework.stereotype.Service

import javax.net.ssl.HttpsURLConnection

@Service
class BgImageServiceImpl implements BgImageService {

    byte[] getBgImage(imgSrc) {
        HttpsURLConnection conn = new URL(imgSrc).openConnection() as HttpsURLConnection
        conn.setRequestProperty('Accept', 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3')
        conn.setRequestProperty('accept-encoding', 'gzip, deflate, br')
        conn.setRequestProperty('accept-language', 'zh-TW,zh;q=0.9,en-US;q=0.8,en;q=0.7')
        conn.setRequestProperty('user-agent', 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.75 Safari/537.36')
        conn.inputStream.getBytes()
    }

    @Override
    String getBgImageDetailsJson(urlStr) {
        HttpsURLConnection conn = new URL(urlStr).openConnection() as HttpsURLConnection
        conn.setRequestProperty('Accept', '*/*')
        conn.setRequestProperty('Authorization', '563492ad6f91700001000001eff56780807c4bdebf8e0335f9af3f20')
        conn.setRequestProperty('Origin', 'http://localhost:8080')
        conn.setRequestProperty('Referer', 'http://localhost:8080/index')
        conn.setRequestProperty('Sec-Fetch', 'cors')
        conn.setRequestProperty('User-Agent', 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.75 Safari/537.36')
        def inp = conn.inputStream
        inp.getText()
    }

    @Override
    List<String> getImageSrcList(String imgDetailJson) {
        ObjectMapper om = new ObjectMapper()
        def m = om.readValue(imgDetailJson, Map.class)

        m['photos'].findAll {
            def width = it['width']
            def height = it['height']
            def src = it['src'] ?: [:]
            def ori = src['original']
            (width && height && ori
                    && width / (double) height > 1.5
                    && width / (double) height < 2.1)
        }.collect {
            it['src']['original']
        } as List<String>
    }
}