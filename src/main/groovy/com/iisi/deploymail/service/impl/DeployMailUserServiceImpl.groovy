package com.iisi.deploymail.service.impl

import com.iisi.deploymail.dao.DeployMailUserDao
import com.iisi.deploymail.model.db.DeployMailUser
import com.iisi.deploymail.service.DeployMailUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DeployMailUserServiceImpl implements DeployMailUserService {

    @Autowired
    DeployMailUserDao deployMailUserDao


    @Override
    List<String> getAllDeployMailUserNames() {
        deployMailUserDao.getAllDeployMailUserNames()
    }

    @Override
    int updateDeployMailUser(DeployMailUser deployMailUser) {
        deployMailUserDao.updateDeployMailUser(deployMailUser)
    }
}
