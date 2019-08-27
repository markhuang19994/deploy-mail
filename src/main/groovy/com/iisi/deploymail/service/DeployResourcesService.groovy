package com.iisi.deploymail.service

import com.iisi.deploymail.model.prop.mail.CommonMailProp
import com.iisi.deploymail.model.resources.CheckinResources
import com.iisi.deploymail.model.resources.CheckoutResources

interface DeployResourcesService {

    CheckoutResources downloadCheckoutResources(CommonMailProp commonMailProp)

    CheckinResources downloadCheckinResources(CommonMailProp commonMailProp)

}