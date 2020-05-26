package com.iisi.deploymail.job

import com.iisi.deploymail.constant.Constants
import com.iisi.deploymail.service.BgImageService
import com.tinify.Tinify
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
        def dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        if (dayOfWeek in [Calendar.SATURDAY, Calendar.SUNDAY]) {
            return
        }
        if (dayOfWeek == Calendar.MONDAY) {
            srcList = []
        }
//        updateChoiceSrcList()
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
        for (int i = 0; i < 3; i++) {
            def imgDetailsJson = bgImageService.getBgImageDetailsJson(
                    "https://api.pexels.com/v1/search?query=${keyword[i]}&per_page=50&page=1")
            srcList.addAll(bgImageService.getImageSrcList(imgDetailsJson, ImageQuality.ORIGINAL))
        }
    }

    private updateStoreImageFile(List<String> srcList) {
        Collections.shuffle(srcList)
        def imgByteList = []
        for (int i = 1; i <= 2; i++) {
            def src = srcList.get(i)
            def bgImg = bgImageService.getBgImage(src)
            try {
                Tinify.setKey("ZHsKFvWtd4GmPRmzlHM6F3lQ1vsKV4VY");
                bgImg = Tinify.fromBuffer(bgImg).toBuffer();
            } catch (Exception e) {
                e.printStackTrace()
            }
            imgByteList << bgImg
        }
        bgImageService.storeImageFile(imgByteList)
        srcList = srcList.subList(3, srcList.size())
    }
}
