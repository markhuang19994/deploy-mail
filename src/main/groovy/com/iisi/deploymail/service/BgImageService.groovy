package com.iisi.deploymail.service

interface BgImageService {
    byte[] getBgImage(imgSrc)

    String getBgImageDetailsJson(urlStr)

    List<String> getImageSrcList(String imgDetailJson)
}