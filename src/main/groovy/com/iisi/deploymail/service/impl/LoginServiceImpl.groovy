package com.iisi.deploymail.service.impl


import com.iisi.deploymail.model.db.DeployMailUser
import com.iisi.deploymail.service.DeployMailUserService
import com.iisi.deploymail.service.LoginService
import groovy.sql.Sql
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LoginServiceImpl implements LoginService {

    @Autowired
    DeployMailUserService deployMailUserService

    @Override
    DeployMailUser login(String engName) {
        deployMailUserService.getDeployMailUserByEngName(engName)
    }
}
