create table DEPLOY_MAIL_USER
(
    ENG_NAME        varchar(80) not null
        constraint P_DEPLOY_MAIL_USER primary key,
    CHECKIN_CONFIG  nvarchar(max),
    CHECKSUM_CONFIG nvarchar(max),
    CHECKOUT_CONFIG nvarchar(max)
)
