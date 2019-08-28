package com.iisi.deploymail.dao.impl

import com.iisi.deploymail.dao.DeployMailUserDao
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
}
