package com.iisi.deploymail.job

import com.iisi.deploymail.constant.Constants
import com.iisi.deploymail.service.BgImageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

import static com.iisi.deploymail.service.impl.BgImageServiceImpl.ImageQuality

@Component
class BgImageUpdateJob {

    @Autowired
    private BgImageService bgImageService
    private List<String> srcList = []

    @Scheduled(cron = "0 0 8,10,12,14,16,18 * * ?")
    updateBgImage() {
        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY || srcList.size() < 2) {
            srcList = []
        }
        updateChoiceSrcList()
        updateSrcListByRandomKeyword()
        updateStoreImageFile(srcList)
    }

    private updateChoiceSrcList() {
        def imgDetailsJson = bgImageService.getBgImageDetailsJson(
                'https://api.pexels.com/v1/curated?per_page=50&page=1')
        srcList.addAll(bgImageService.getImageSrcList(imgDetailsJson, ImageQuality.ORIGINAL))
    }

    private updateSrcListByRandomKeyword() {
        def keyword = new ArrayList(Constants.IMAGE_SEARCH_KEYWORD)
        Collections.shuffle(keyword)
        for (int i = 0; i < 2; i++) {
            def imgDetailsJson = bgImageService.getBgImageDetailsJson(
                    "https://api.pexels.com/v1/search?query=${keyword[i]}&per_page=80&page=1")
            srcList.addAll(bgImageService.getImageSrcList(imgDetailsJson, ImageQuality.ORIGINAL))
        }
    }

    private updateStoreImageFile(List<String> srcList) {
        Collections.shuffle(srcList)
        bgImageService.storeImageFile(srcList)
    }
}
