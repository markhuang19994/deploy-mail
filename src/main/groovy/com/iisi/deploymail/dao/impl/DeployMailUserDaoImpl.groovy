package com.iisi.deploymail.dao.impl

import com.iisi.deploymail.dao.DeployMailUserDao
import com.iisi.deploymail.model.db.DeployMailUser
import com.iisi.deploymail.util.JsonUtil
import groovy.sql.Sql
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class DeployMailUserDaoImpl implements DeployMailUserDao {
    @Autowired
    Sql gSql

    @Override
    List<String> getAllDeployMailUserNames() {
        gSql.rows("SELECT eng_name FROM DEPLOY_MAIL_USER").collect { it.values()[0] } as List<String>
    }

    @Override
    int updateDeployMailUser(DeployMailUser deployMailUser) {
        def checkinConfigJson = JsonUtil.stringify(deployMailUser.checkinConfig, false)
        def checkoutConfigJson = JsonUtil.stringify(deployMailUser.checkoutConfig, false)
        def checksumConfigJson = JsonUtil.stringify(deployMailUser.checksumConfig, false)

        def updateCount = gSql.executeUpdate('''
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
        def pwd = deployMailUser.mailPassword

        def updateCount = -1
        if (pwd == null || pwd == '') {
            updateCount = gSql.executeUpdate('''
                UPDATE DEPLOY_MAIL_USER  SET 
                MAIL_ACCOUNT =  ?
                WHERE eng_name = ?
            ''', [account, engName])
        } else {
            updateCount = gSql.executeUpdate('''
                UPDATE DEPLOY_MAIL_USER  SET 
                MAIL_ACCOUNT =  ?,
                MAIL_PASSWORD = ?
                WHERE eng_name = ?
            ''', [account, pwd, engName])
        }
        updateCount
    }
}
