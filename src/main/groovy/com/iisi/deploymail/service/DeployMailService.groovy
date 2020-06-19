package com.iisi.deploymail.service


import com.iisi.deploymail.model.prop.mail.MailProp

interface DeployMailService<T extends MailProp> {

    Map<String, Object> sendMail(T mailProp)

}