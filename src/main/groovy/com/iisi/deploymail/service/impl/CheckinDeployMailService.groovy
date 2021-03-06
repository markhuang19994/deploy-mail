package com.iisi.deploymail.service.impl


import com.iisi.deploymail.freemarker.FtlProvider
import com.iisi.deploymail.model.prop.mail.CheckinMailProp
import com.iisi.deploymail.util.DateUtil
import com.iisi.deploymail.util.FileUtil
import com.iisi.deploymail.util.FreemarkerUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service

import javax.mail.Message
import javax.mail.Multipart
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage

@Service('CheckinMailService')
class CheckinDeployMailService extends AbstractDeployMailService<CheckinMailProp> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckinDeployMailService.class)

    @Autowired
    CheckinDeployMailService(Environment env, FtlProvider ftlProvider) {
        super(env, ftlProvider)
    }

    @Override
    Map<String, Object> sendMail(CheckinMailProp checkinMailProp) {
        def warnMessages = []

        def checkinResources = checkinMailProp.checkinResources
        def checkinFiles = []

        if (!checkinMailProp.noSends?.contains('changeForm')) {
            if (checkinResources.changeForm) {
                checkinFiles << checkinResources.changeForm
            } else {
                warnMessages << 'changeForm is not found.'
            }
        }

        if (!checkinMailProp.noSends?.contains('changeFormSql')) {
            if (checkinResources.changeFormSql) {
                checkinFiles << checkinResources.changeFormSql
            } else {
                warnMessages << 'changeFormSql is not found.'
            }
        }

        def diffFiles = getDiffFiles(checkinResources.diffZip)
        List<File> replyFiles = []
        if (diffFiles && diffFiles.size() > 0) {
            if (diffFiles.size() > 1) {
                replyFiles = diffFiles.reverse()
            } else {
                checkinFiles << diffFiles[0]
            }
        } else {
            warnMessages << 'diffFiles is not found.'
        }

        if (checkinResources.otherFiles) {
            checkinFiles += checkinResources.otherFiles
        }

        String mailAccount = checkinMailProp.mailAccount ?: env.getProperty('mail.user.name')
        String mailAccountAlias = checkinMailProp.mailAccountAlias ?: mailAccount
        String password = checkinMailProp.mailPassword ?: env.getProperty('mail.user.pwd')
        def session = getMailSession()

        def mailAddress = getMailAddress(checkinMailProp.to, checkinMailProp.cc, false)
        MimeMessage message = new MimeMessage(session)
        message.setFrom(new InternetAddress(mailAccountAlias))
        message.addRecipients(Message.RecipientType.TO, mailAddress.to)
        if (mailAddress.cc) {
            message.addRecipients(Message.RecipientType.CC, mailAddress.cc)
        }
        message.setSubject(getSubject(checkinMailProp.lacrNo, checkinMailProp.projectName))

        Multipart multipart = generateMultipart(checkinFiles as File[])
        MimeBodyPart messageBodyPart = new MimeBodyPart()
        messageBodyPart.setContent(generateMailHtml(checkinMailProp), HTML_MAIL_CONTENT_TYPE)
        multipart.addBodyPart(messageBodyPart)
        message.setContent(multipart, HTML_MAIL_CONTENT_TYPE)

        session.getTransport("smtp").withCloseable { transport ->
            transport.connect(mailAccount, password)
            LOGGER.debug("Start send mail to ${mailAddress.toString()}")
            transport.sendMessage(message, message.getAllRecipients())
            LOGGER.debug("send First message success...")

            if (replyFiles.size() > 0) {
                Message reply = message.reply(true)
                for (int i = 0; i < replyFiles.size(); i++) {
                    File partialFile = replyFiles.get(i)
                    Multipart mPart = generateMultipart(partialFile)
                    reply.setFrom(new InternetAddress(mailAccountAlias))
                    reply.setReplyTo(message.getReplyTo())
                    reply.setContent(mPart)
                    MimeBodyPart bodyPart = new MimeBodyPart()
                    bodyPart.setText("Attach file " + (i + 1) + "/" + replyFiles.size() + ".")
                    mPart.addBodyPart(bodyPart)
                    transport.sendMessage(reply, reply.getAllRecipients())
                    LOGGER.debug("Send reply mail ${partialFile.getName()} success...")
                    reply = reply.reply(true)
                }
            }
            LOGGER.debug('Send checkin mail success')
        }
        [warnMessages: warnMessages]
    }

    private String generateMailHtml(CheckinMailProp checkinMailProp) {
        def template = ftlProvider.getFreeMarkerTemplate('/ftl/mail/checkin.ftl')
        def m = [
                projectName: checkinMailProp.projectName,
                lacrNo     : checkinMailProp.lacrNo,
                senderName : checkinMailProp.senderName,
                note       : checkinMailProp.note
        ]
        FreemarkerUtil.processTemplateToString(template, m)
    }

    private static String getSubject(String lacrNo, String projectName) {
        def dateStr = DateUtil.formatNowUtc8Date('MM-dd HH:mm')
        "[COLA-$lacrNo] Checkin [$projectName] [$dateStr]"
    }


    private static List<File> getDiffFiles(File diffZip) {
        def tempFolder = new File(FileUtil.getTempFolder(), "/${System.currentTimeMillis()}")
        FileUtil.unZipProtectedFiles(diffZip, tempFolder, null).listFiles()?.toList()
    }

}
