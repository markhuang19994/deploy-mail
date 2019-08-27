package com.iisi.deploymail.model.prop.mail

abstract class MailProp {
    String projectName
    String lacrNo
    String jenkinsJobName
    String jenkinsBuildNum
    String senderName
    List<String> to;
    List<String> cc;
}
