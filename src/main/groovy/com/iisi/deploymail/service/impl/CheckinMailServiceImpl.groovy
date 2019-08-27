package com.iisi.deploymail.service.impl

import com.iisi.deploymail.freemarker.FtlProvider
import com.iisi.deploymail.model.prop.mail.CheckinMailProp
import com.iisi.deploymail.model.prop.mail.CommonMailProp
import com.iisi.deploymail.util.FileUtil
import com.iisi.deploymail.util.FreemarkerUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service

import javax.mail.Message
import javax.mail.Multipart
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import java.text.SimpleDateFormat

@Service('CheckinMailService')
class CheckinMailServiceImpl extends AbstractMailServiceImpl<CheckinMailProp> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckinMailServiceImpl.class)

    @Autowired
    CheckinMailServiceImpl(Environment env, FtlProvider ftlProvider) {
        super(env, ftlProvider)
    }

    @Override
    void sendMail(CommonMailProp commonMailProp, CheckinMailProp checkinMailProp) {
        def checkinResources = checkinMailProp.checkinResources
        def diffFiles = getDiffFiles(checkinResources.diffZip)
        def checkinFiles = [checkinResources.checksum, checkinResources.changeForm] << diffFiles[0]
        def replyFiles = diffFiles.size() > 1 ? diffFiles[1..diffFiles.size() - 1] : []

        String username = env.getProperty('mail.user.name')
        String password = env.getProperty('mail.user.pwd')
        def session = getMailSession()

        def mailAddress = getMailAddress(['mark.huang@iisigroup.com'], ['mark.huang@iisigroup.com'], false)
        MimeMessage message = new MimeMessage(session)
        message.setFrom(new InternetAddress(username))
        message.addRecipients(Message.RecipientType.TO, mailAddress.to)
        message.addRecipients(Message.RecipientType.CC, mailAddress.cc)
        message.setSubject(getSubject(commonMailProp.projectName, commonMailProp.lacrNo))

        Multipart multipart = generateMultipart(checkinFiles as File[])
        MimeBodyPart messageBodyPart = new MimeBodyPart()
        messageBodyPart.setContent(generateMailHtml(commonMailProp, checkinMailProp), HTML_MAIL_CONTENT_TYPE)
        multipart.addBodyPart(messageBodyPart)
        message.setContent(multipart, HTML_MAIL_CONTENT_TYPE)

        Transport transport = session.getTransport("smtp")
        transport.connect(username, password)
        LOGGER.debug("Start send mail to ${mailAddress.toString()}")
        transport.sendMessage(message, message.getAllRecipients())
        LOGGER.debug("send First message success...")

        if (replyFiles.size() > 0) {
            Message reply = message.reply(true)
            for (int i = 0; i < replyFiles.size(); i++) {
                File partialFile = replyFiles.get(i)
                Multipart mPart = generateMultipart(partialFile)
                reply.setFrom(new InternetAddress(username))
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
        transport.close()
    }

    private String generateMailHtml(CommonMailProp commonMailProp, CheckinMailProp checkinMailProp) {
        def template = ftlProvider.getFreeMarkerTemplate('/ftl/mail/checkin.ftl')
        def m = [
                projectName: commonMailProp.projectName,
                lacrNo     : commonMailProp.lacrNo
        ]
        FreemarkerUtil.processTemplateToString(template, m)
    }

    private static String getSubject(String lacrNo, String projectName) {
        def dateStr = new SimpleDateFormat('MM-dd HH:mm').format(new Date())
        "[COLA-$lacrNo] Checkin [$projectName] [$dateStr]"
    }


    private static List<File> getDiffFiles(File diffZip) {
        def tempFolder = new File(FileUtil.getTempFolder(), "/${System.currentTimeMillis()}")
        FileUtil.unZipProtectedFiles(diffZip, tempFolder, null).listFiles()?.toList()
    }

}