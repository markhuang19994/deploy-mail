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
        gSql.rows("SELECT ENG_NAME FROM DEPLOY_MAIL_USER").collect { it.values()[0] } as List<String>
    }

    @Override
    int updateDeployMailUser(DeployMailUser deployMailUser) {
        def checkinConfigJson = JsonUtil.stringify(deployMailUser.checkinConfig, false)
        def checkoutConfigJson = JsonUtil.stringify(deployMailUser.checkoutConfig, false)
        def checksumConfigJson = JsonUtil.stringify(deployMailUser.checksumConfig, false)

        def updateCount = gSql.executeUpdate('''
            UPDATE DEPLOY_MAIL_USER  SET 
            CHECKIN_CONFIG =  ?,
            CHECKOUT_CONFIG = ?,
            CHECKSUM_CONFIG=  ?
            WHERE ENG_NAME = ?
        ''', [checkinConfigJson, checkoutConfigJson, checksumConfigJson, deployMailUser.engName])
        gSql.commit()
        updateCount
    }
}
