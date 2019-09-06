package com.iisi.deploymail.dao

import com.iisi.deploymail.model.db.DeployMailUser

interface DeployMailUserDao {

    List<String> getAllDeployMailUserNames()

    int updateDeployMailUser(DeployMailUser deployMailUser)

    int updateDeployMailUserSetting(DeployMailUser deployMailUser)

    DeployMailUser getDeployMailUserByEngName(String engName)
}
