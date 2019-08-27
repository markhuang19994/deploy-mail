package com.iisi.deploymail.service.impl

import com.iisi.deploymail.service.IndexService
import groovy.sql.Sql
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class IndexServiceImpl implements IndexService{

    @Autowired
    Sql gSql

    @Override
    List<String> getAllDeployUsers() {
        gSql.rows("SELECT ENG_NAME FROM DEPLOY_MAIL_USER").collect{it.values()[0]} as List<String>
    }
}