package com.iisi.deploymail.service


import com.iisi.deploymail.model.prop.mail.CommonMailProp
import com.iisi.deploymail.model.prop.mail.MailProp

interface DeployMailService<T extends MailProp> {

    void sendMail(CommonMailProp commonMailProp, T mailProp)

}