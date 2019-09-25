package com.iisi.deploymail.service

import com.iisi.deploymail.service.impl.BgImageServiceImpl

interface BgImageService {
    byte[] getBgImage(imgSrc)

    String getBgImageDetailsJson(urlStr)

    List<String> getImageSrcList(String imgDetailJson, BgImageServiceImpl.ImageQuality quality)

    void storeImageFile(List<String> srcList)

    File getBackgroundImageDir()

    File getBackgroundImageRootDir()

    File getDefaultBackgroundImage()
}