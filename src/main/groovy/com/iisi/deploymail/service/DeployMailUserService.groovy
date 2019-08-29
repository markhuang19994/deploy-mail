package com.iisi.deploymail.service

import com.iisi.deploymail.model.db.DeployMailUser


interface DeployMailUserService {

    List<String> getAllDeployMailUserNames()

    int updateDeployMailUser(DeployMailUser deployMailUser)

}
