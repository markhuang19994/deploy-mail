package com.iisi.deploymail.job

import com.iisi.deploymail.constant.Constants
import com.iisi.deploymail.service.BgImageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

import java.text.SimpleDateFormat

@Component
class BgImageUpdateJob {

    @Autowired
    private BgImageService bgImageService
    private imgDir;
    private srcList = [];

    BgImageUpdateJob() {
        ClassPathResource cpr = new ClassPathResource('/static/image')
        imgDir = cpr.getFile()
    }

    @Scheduled(cron = "0 0 8 1/1 * ? *")
    updateBgImage() {
        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY || srcList.size() < 2) {
            updateSrcList()
        }
        updateSacListDaily()
        updateStoreImageFile()
    }

    private updateSrcList() {
        def imgDetailsJson = bgImageService.getBgImageDetailsJson(
                'https://api.pexels.com/v1/curated?per_page=80&page=1')
        srcList.addAll(bgImageService.getImageSrcList(imgDetailsJson))
    }

    private updateSacListDaily() {
        def keyword = new ArrayList(Constants.IMAGE_SEARCH_KEYWORD)
        Collections.shuffle(keyword)
        def imgDetailsJson = bgImageService.getBgImageDetailsJson(
                "https://api.pexels.com/v1/search?query=${keyword[0]}&per_page=80&page=1")
        srcList.addAll(bgImageService.getImageSrcList(imgDetailsJson))
    }

    private updateStoreImageFile() {
        SimpleDateFormat sdf = new SimpleDateFormat('EEE')
        Collections.shuffle(srcList)
        def dir = new File(imgDir, 'day_' + sdf.format(new Date()))
        if (!dir.exists()) {
            dir.mkdirs()
        }
        for (int i = 1; i <= 2; i++) {
            def src = srcList.get(i)
            def bgImg = bgImageService.getBgImage(src)
            def imgFile = new File(dir, 'img_' + i)
            imgFile.delete()
            imgFile << bgImg
            srcList.remove(i)
        }
    }
}
