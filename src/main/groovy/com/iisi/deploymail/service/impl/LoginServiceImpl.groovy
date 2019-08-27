package com.iisi.deploymail.service.impl

import com.iisi.deploymail.model.config.CheckinConfig
import com.iisi.deploymail.model.config.CheckoutConfig
import com.iisi.deploymail.model.db.DeployMailUser
import com.iisi.deploymail.service.LoginService
import com.iisi.deploymail.util.JsonUtil
import groovy.sql.Sql
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LoginServiceImpl implements LoginService {

    @Autowired
    Sql gSql

    @Override
    DeployMailUser login(String engName) {
        def columnMap = gSql.firstRow("SELECT * FROM DEPLOY_MAIL_USER d WHERE d.ENG_NAME = '$engName'") as Map

        if (columnMap == null) {
            throw new Exception("User: $engName not found in table DEPLOY_MAIL_USER")
        }

        def checkinConfigJson = columnMap.get('CHECKIN_CONFIG')
        def checkinConfig = JsonUtil.parseJson(String.valueOf(checkinConfigJson), CheckinConfig.class)

        def checkoutConfigJson = columnMap.get('CHECKOUT_CONFIG')
        def checkoutConfig = JsonUtil.parseJson(String.valueOf(checkoutConfigJson), CheckoutConfig.class)

        new DeployMailUser(
                engName: engName,
                checkinConfig: checkinConfig,
                checkoutConfig: checkoutConfig
        )
    }
}