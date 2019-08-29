package com.iisi.deploymail.constant

final class Constants {
    private Constants() {
        new AssertionError()
    }

    static final String USER_ENG_NAME = 'userEngName'
    static final String CHECKIN_MAIL_PROP = 'checkinMailProp'
    static final String CHECKOUT_MAIL_PROP = 'checkoutMailProp'

    static class Flag {
        static final String IS_LOGIN = 'isLogin'
    }


}
