package com.iisi.deploymail.model.prop.mail

abstract class MailProp {
    String mailAccount
    String mailAccountAlias
    String mailPassword
    String projectName
    String lacrNo
    String jenkinsJobName
    String jenkinsBuildNum
    String senderName
    String note
    List<String> to;
    List<String> cc;
}
