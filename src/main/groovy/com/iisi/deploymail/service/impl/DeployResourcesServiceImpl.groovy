package com.iisi.deploymail.service.impl

import com.iisi.deploymail.model.prop.mail.CommonMailProp
import com.iisi.deploymail.model.resources.CheckinResources
import com.iisi.deploymail.model.resources.CheckoutResources
import com.iisi.deploymail.service.DeployResourcesService
import com.iisi.deploymail.util.DownloadFileUtil
import com.iisi.deploymail.util.FileUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service

@Service
class DeployResourcesServiceImpl implements DeployResourcesService {
    private static final LOGGER =  LoggerFactory.getLogger(AbstractMailServiceImpl.class)

    @Autowired
    Environment env

    @Override
    CheckoutResources downloadCheckoutResources(CommonMailProp commonMailProp) {
        def projectName = commonMailProp.projectName
        def jobName = commonMailProp.jenkinsJobName
        def buildNum = commonMailProp.jenkinsBuildNum

        def checkoutResourcesList = [
                'checkoutForm.doc', 'checkout.txt'
        ]
        def urls = checkoutResourcesList.collect {
            getResourceUrl(jobName, buildNum, it)
        }

        def temp = FileUtil.getTempFolder()
        LOGGER.debug("Start download files ${urls.toList().toString()}")
        def downloadFiles = DownloadFileUtil.downloadFiles(urls, temp)

        def checkoutResources = new CheckoutResources()
        checkoutResources.checkoutForm = downloadFiles[checkoutResourcesList[0]]
        checkoutResources.checkoutTxt = downloadFiles[checkoutResourcesList[1]]
        checkoutResources
    }

    @Override
    CheckinResources downloadCheckinResources(CommonMailProp commonMailProp) {
        def projectName = commonMailProp.projectName
        def jobName = commonMailProp.jenkinsJobName
        def buildNum = commonMailProp.jenkinsBuildNum

        def checkinResourcesList = [
                "${projectName}_diff_split.zip", 'changeForm.doc', 'ChkSrcApp.csv'
        ]

        def urls = checkinResourcesList.collect {
            getResourceUrl(jobName, buildNum, it)
        }

        def temp = FileUtil.getTempFolder()
        LOGGER.debug("Start download files ${urls.toList().toString()}")
        def downloadFiles = DownloadFileUtil.downloadFiles(urls, temp)

        def checkinResources = new CheckinResources()
        checkinResources.diffZip = downloadFiles[checkinResourcesList[0]]
        checkinResources.changeForm = downloadFiles[checkinResourcesList[1]]
        checkinResources.checksum = downloadFiles[checkinResourcesList[2]]
        checkinResources
    }

    private getResourceUrl(String jobName, String buildNum, String fileName) {
        def jenkinsBaseUrl = env.getProperty('jenkins.base.url')
        def jenkinsJobUrl = env.getProperty('jenkins.job.url')

        "${jenkinsBaseUrl + jenkinsJobUrl}/$jobName/$buildNum/artifact/deploy/$fileName"
    }
}