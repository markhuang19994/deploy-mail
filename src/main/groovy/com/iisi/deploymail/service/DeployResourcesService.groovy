package com.iisi.deploymail.service

import com.iisi.deploymail.model.prop.mail.CheckinMailProp
import com.iisi.deploymail.model.prop.mail.CheckoutMailProp
import com.iisi.deploymail.model.prop.mail.ChecksumMailProp
import com.iisi.deploymail.model.resources.CheckinResources
import com.iisi.deploymail.model.resources.CheckoutResources
import com.iisi.deploymail.model.resources.ChecksumResources

interface DeployResourcesService {

    CheckoutResources downloadCheckoutResources(CheckoutMailProp checkoutMailProp)

    CheckinResources downloadCheckinResources(CheckinMailProp checkinMailProp)

    ChecksumResources downloadChecksumResources(ChecksumMailProp checksumMailProp)
}