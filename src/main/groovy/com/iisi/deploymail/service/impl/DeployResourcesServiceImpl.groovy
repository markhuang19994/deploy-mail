package com.iisi.deploymail.service.impl

import com.iisi.deploymail.model.prop.mail.CheckinMailProp
import com.iisi.deploymail.model.prop.mail.CheckoutMailProp
import com.iisi.deploymail.model.prop.mail.ChecksumMailProp
import com.iisi.deploymail.model.resources.CheckinResources
import com.iisi.deploymail.model.resources.CheckoutResources
import com.iisi.deploymail.model.resources.ChecksumResources
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
    CheckoutResources downloadCheckoutResources(CheckoutMailProp checkoutMailProp) {
        def projectName = checkoutMailProp.projectName
        def jobName = checkoutMailProp.jenkinsJobName
        def buildNum = checkoutMailProp.jenkinsBuildNum

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
    CheckinResources downloadCheckinResources(CheckinMailProp checkinMailProp) {
        def projectName = checkinMailProp.projectName
        def jobName = checkinMailProp.jenkinsJobName
        def buildNum = checkinMailProp.jenkinsBuildNum

        def checkinResourcesList = [
                "${projectName}_diff_split.zip", 'changeForm.doc'
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
        checkinResources
    }

    @Override
    ChecksumResources downloadChecksumResources(ChecksumMailProp checksumMailProp) {
        def projectName = checksumMailProp.projectName
        def jobName = checksumMailProp.jenkinsJobName
        def buildNum = checksumMailProp.jenkinsBuildNum

        def checkinResourcesList = ['ChkSrcApp.csv']

        def urls = checkinResourcesList.collect {
            getResourceUrl(jobName, buildNum, it)
        }

        def temp = FileUtil.getTempFolder()
        LOGGER.debug("Start download files ${urls.toList().toString()}")
        def downloadFiles = DownloadFileUtil.downloadFiles(urls, temp)

        def checksumResources = new ChecksumResources()
        checksumResources.checksum = downloadFiles[checkinResourcesList[0]]
        checksumResources
    }

    private getResourceUrl(String jobName, String buildNum, String fileName) {
        def jenkinsBaseUrl = env.getProperty('jenkins.base.url')
        def jenkinsJobUrl = env.getProperty('jenkins.job.url')

        "${jenkinsBaseUrl + jenkinsJobUrl}/$jobName/$buildNum/artifact/deploy/$fileName"
    }
}