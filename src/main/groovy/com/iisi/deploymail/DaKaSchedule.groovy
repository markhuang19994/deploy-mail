package com.iisi.deploymail

import com.iisi.deploymail.freemarker.FtlProvider
import com.iisi.deploymail.service.DeployMailUserService
import com.iisi.deploymail.service.impl.AbstractDeployMailService
import com.iisi.deploymail.service.impl.MailHelper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.core.io.ClassPathResource
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

import javax.mail.Message
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

/**
 * @since 9/1/20
 * @author Mark Huang
 */
//@Component
class DaKaSchedule {
    private static final Logger LOGGER = LoggerFactory.getLogger(DaKaSchedule.class)
    private final Environment env;
    private final FtlProvider ftlProvider;
    private final DeployMailUserService mailUserService

    @Autowired
    DaKaSchedule(Environment env, FtlProvider ftlProvider, DeployMailUserService mailUserService) {
        this.env = env
        this.ftlProvider = ftlProvider
        this.mailUserService = mailUserService
    }

    @Scheduled(cron = "0 28 9,18 * * ?")
    sendDaKaNotice() {
        TimeUnit.SECONDS.sleep(Math.round(Math.random() * 240))
        def mailUser = mailUserService.getDeployMailUserByEngName("Mark")
        def mailAccount = mailUser.mailAccount ?: env.getProperty('mail.user.name')
        def mailAccountAlias = mailUser.mailAccountAlias ?: mailAccount
        def password = MailHelper.decodeMailUserPwd(mailUser.mailPassword)

        def session = MailHelper.getMailSession(env)

        def bodyPart = new MimeBodyPart()
        bodyPart.setContent(generateMailHtml(), AbstractDeployMailService.HTML_MAIL_CONTENT_TYPE)

        def multipart = new MimeMultipart()
        multipart.addBodyPart(bodyPart)

        def mailAddressTo = getMailAddressToList()
        def message = new MimeMessage(session)
        message.setFrom(new InternetAddress(mailAccountAlias))
        message.addRecipients(Message.RecipientType.TO, mailAddressTo as InternetAddress[])
        message.setSubject("[打卡] 各位同學記得打${getWorkTime()}卡")
        message.setContent(multipart, AbstractDeployMailService.HTML_MAIL_CONTENT_TYPE)

        session.getTransport("smtp").withCloseable { transport ->
            transport.connect(mailAccount, password)
            LOGGER.debug("Start send mail to ${mailAddressTo.toString()}.")
            transport.sendMessage(message, message.getAllRecipients())
            LOGGER.debug('Send mail success.')
        }
    }

    private static String generateMailHtml() {
        def cpr = new ClassPathResource('/images/daka.jpeg')
        def b64DaKaImg = ""
        if (cpr.exists()) {
            def bs = cpr.getInputStream().bytes
            b64DaKaImg = new String(Base64.getEncoder().encode(bs), StandardCharsets.UTF_8)
        }
        """
            <body>
                <img src="data:image/png;base64,${b64DaKaImg}" alt="img" />
                <p style="color:red">各位同學記得打${getWorkTime()}卡</p>                
            </body>
        """
    }

    private static List<InternetAddress> getMailAddressToList() {
        [
                'jiaying.peng@iisigroup.com',
                'tim.chiang@iisigroup.com',
                'smano.huang@iisigroup.com',
                'johnson.ho@iisigroup.com',
                'roger.lin@iisigroup.com',
                'cathy.chang@iisigroup.com',
                'andy.chen@iisigroup.com',
                'mingchung.chen@iisigroup.com',
                'richard.yeh@iisigroup.com',
                'russel.chua@iisigroup.com',
                'mark.huang@iisigroup.com',
        ].collect { new InternetAddress(it) }
    }

    private static String getWorkTime() {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH");
        String str = df.format(date);
        int a = Integer.parseInt(str);
        if (a >= 8 && a < 12) {
            return "上班"
        } else {
            return "下班"
        }
    }
}
