package com.iisi.deploymail.dao.impl

import com.iisi.deploymail.dao.DeployMailUserDao
import com.iisi.deploymail.model.config.CheckinConfig
import com.iisi.deploymail.model.config.CheckoutConfig
import com.iisi.deploymail.model.config.ChecksumConfig
import com.iisi.deploymail.model.db.DeployMailUser
import com.iisi.deploymail.util.JsonUtil
import com.iisi.deploymail.util.KeyUtil
import groovy.sql.Sql
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

@Component
class DeployMailUserDaoImpl implements DeployMailUserDao {
    @Autowired
    private ApplicationContext ctx;

    private Sql gSql() {
        ctx.getBean('gSql', Sql.class)
    }

    @Override
    List<String> getAllDeployMailUserNames() {
        gSql().rows("SELECT eng_name FROM DEPLOY_MAIL_USER").collect { it.values()[0] } as List<String>
    }

    @Override
    int updateDeployMailUser(DeployMailUser deployMailUser) {
        def checkinConfigJson = JsonUtil.stringify(deployMailUser.checkinConfig, false)
        def checkoutConfigJson = JsonUtil.stringify(deployMailUser.checkoutConfig, false)
        def checksumConfigJson = JsonUtil.stringify(deployMailUser.checksumConfig, false)

        def updateCount = gSql().executeUpdate('''
            UPDATE DEPLOY_MAIL_USER  SET 
            checkin_config =  ?,
            checkout_config = ?,
            checksum_config=  ?
            WHERE eng_name = ?
        ''', [checkinConfigJson, checkoutConfigJson, checksumConfigJson, deployMailUser.engName])
        updateCount
    }

    @Override
    int updateDeployMailUserSetting(DeployMailUser deployMailUser) {
        def engName = deployMailUser.engName
        def account = deployMailUser.mailAccount
        def accountAlias = deployMailUser.mailAccountAlias
        def pwd = deployMailUser.mailPassword

        def updateCount = -1
        if (pwd == null || pwd == '') {
            updateCount = gSql().executeUpdate('''
                UPDATE DEPLOY_MAIL_USER  SET 
                MAIL_ACCOUNT =  ?,
                MAIL_ACCOUNT_ALIAS =  ?
                WHERE eng_name = ?
            ''', [account, accountAlias, engName])
        } else {
            def pubKey = KeyUtil.getKey(new ClassPathResource('key/publicKey.key').inputStream)
            def encodePwd = KeyUtil.encrypt(pwd.getBytes(), pubKey, 'RSA')
            String encodePwdB64 = Base64.getEncoder().encodeToString(encodePwd);
            updateCount = gSql().executeUpdate('''
                UPDATE DEPLOY_MAIL_USER  SET 
                MAIL_ACCOUNT =  ?,
                MAIL_ACCOUNT_ALIAS =  ?,
                MAIL_PASSWORD = ?
                WHERE eng_name = ?
            ''', [account, accountAlias, encodePwdB64, engName])
        }
        updateCount
    }

    @Override
    DeployMailUser getDeployMailUserByEngName(String engName) {
        def columnMap = gSql().firstRow("SELECT * FROM DEPLOY_MAIL_USER d WHERE d.ENG_NAME = '$engName'".toString()) as Map

        if (columnMap == null) {
            throw new Exception("User: $engName not found in table DEPLOY_MAIL_USER")
        }

        def checkinConfigJson = columnMap.get('CHECKIN_CONFIG')
        def checkinConfig = JsonUtil.parseJson(String.valueOf(checkinConfigJson), CheckinConfig.class)

        def checkoutConfigJson = columnMap.get('CHECKOUT_CONFIG')
        def checkoutConfig = JsonUtil.parseJson(String.valueOf(checkoutConfigJson), CheckoutConfig.class)

        def checksumConfigJson = columnMap.get('CHECKSUM_CONFIG')
        def checksumConfig = JsonUtil.parseJson(String.valueOf(checksumConfigJson), ChecksumConfig.class)

        def mailAccount = columnMap.get('MAIL_ACCOUNT') ?: ''
        def mailAccountAlias = columnMap.get('MAIL_ACCOUNT_ALIAS') ?: ''
        def mailPassword = columnMap.get('MAIL_PASSWORD') ?: ''

        new DeployMailUser(
                engName: engName,
                checkinConfig: checkinConfig,
                checksumConfig: checksumConfig,
                checkoutConfig: checkoutConfig,
                mailAccount: mailAccount,
                mailAccountAlias: mailAccountAlias,
                mailPassword: mailPassword
        )
    }
}
