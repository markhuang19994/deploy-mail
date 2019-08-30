USE dbo;

drop table if exists DEPLOY_MAIL_USER;

create table DEPLOY_MAIL_USER
(
    `ENG_NAME`        varchar(32) UNIQUE NOT NULL,
    `CHECKIN_CONFIG`  varchar(4000),
    `CHECKSUM_CONFIG` varchar(4000),
    `CHECKOUT_CONFIG` varchar(4000),
    PRIMARY KEY (`ENG_NAME`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;