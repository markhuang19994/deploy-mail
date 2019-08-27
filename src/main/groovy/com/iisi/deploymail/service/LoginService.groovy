package com.iisi.deploymail.service

import com.iisi.deploymail.model.db.DeployMailUser

interface LoginService {
   DeployMailUser login(String engName)
}