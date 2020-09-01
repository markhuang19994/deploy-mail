package com.iisi.deploymail.service.impl

import com.iisi.deploymail.model.db.DeployMailUser
import com.iisi.deploymail.util.KeyUtil
import org.springframework.core.env.Environment
import org.springframework.core.io.ClassPathResource

import javax.mail.Session

/**
 * @since 9/1/20
 * @author Mark Huang
 */
class MailHelper {
    static Session getMailSession(Environment env) {
        Properties props = [:] as Properties
        props['mail.smtp.auth'] = env.getProperty('mail.smtp.auth')
        props['mail.smtp.starttls.enable'] = env.getProperty('mail.smtp.starttls.enable')
        props['mail.smtp.host'] = env.getProperty('mail.smtp.host')
        props['mail.smtp.port'] = env.getProperty('mail.smtp.port')
        Session.getDefaultInstance(props)
    }

    static String decodeMailUserPwd(String encodePwd) {
        def decodePwdByte = Base64.getDecoder().decode(encodePwd)

        def priKey = KeyUtil.getKey(new ClassPathResource('key/privateKey.key').inputStream)
        def decodePwd = KeyUtil.decrypt(decodePwdByte, priKey, 'RSA')
        new String(decodePwd)
    }
}
