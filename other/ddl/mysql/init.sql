#Create employ salary system
CREATE DATABASE dbo;

#Create user sa
CREATE USER 'sa'@'localhost' IDENTIFIED BY 'p@ssw0rd';

#Allow sa privileges
grant all privileges on dbo.* to 'sa'@'localhost';
FLUSH PRIVILEGES;

#Allow sa privileges and allow all host connect to sa
GRANT ALL PRIVILEGES ON *.* TO 'sa'@'%' IDENTIFIED BY 'p@ssw0rd' WITH GRANT OPTION;
FLUSH   PRIVILEGES;