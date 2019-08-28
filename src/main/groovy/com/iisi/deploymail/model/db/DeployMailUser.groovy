package com.iisi.deploymail.model.db

import com.iisi.deploymail.model.config.CheckinConfig
import com.iisi.deploymail.model.config.CheckoutConfig
import com.iisi.deploymail.model.config.ChecksumConfig

class DeployMailUser {
    String engName
    CheckinConfig checkinConfig
    ChecksumConfig checksumConfig
    CheckoutConfig checkoutConfig
}
