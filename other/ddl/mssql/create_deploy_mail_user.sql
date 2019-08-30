create table DEPLOY_MAIL_USER
(
    ENG_NAME        varchar(80) not null
        constraint P_DEPLOY_MAIL_USER primary key,
    CHECKIN_CONFIG  nvarchar(max),
    CHECKSUM_CONFIG nvarchar(max),
    CHECKOUT_CONFIG nvarchar(max)
)

INSERT INTO dbo.DEPLOY_MAIL_USER (ENG_NAME) VALUES ('Mark');
INSERT INTO dbo.DEPLOY_MAIL_USER (ENG_NAME) VALUES ('Jonson');
INSERT INTO dbo.DEPLOY_MAIL_USER (ENG_NAME) VALUES ('Minchung');
INSERT INTO dbo.DEPLOY_MAIL_USER (ENG_NAME) VALUES ('Andy');
INSERT INTO dbo.DEPLOY_MAIL_USER (ENG_NAME) VALUES ('Roger');