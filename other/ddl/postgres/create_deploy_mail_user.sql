create schema dbo;

create table dbo.DEPLOY_MAIL_USER
(
    ENG_NAME varchar(80)
        constraint deploy_mail_user_pk
            primary key,
    CHECKIN_CONFIG varchar(4000),
    CHECKOUT_CONFIG varchar(4000),
    CHECKSUM_CONFIG varchar(4000)
);

