-- create databases
CREATE DATABASE IF NOT EXISTS `order_db`;
CREATE DATABASE IF NOT EXISTS `user_db`;
CREATE DATABASE IF NOT EXISTS `shipping_db`;

-- grant rights to root user
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;

-- create order and grant rights
CREATE USER 'order_usr'@'%' IDENTIFIED BY 'order_psswd';
GRANT ALL PRIVILEGES ON order_db.* TO 'order_usr'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;

-- create user and grant rights
CREATE USER 'user_usr'@'%' IDENTIFIED BY 'user_psswd';
GRANT ALL PRIVILEGES ON user_db.* TO 'user_usr'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;

-- create shipping and grant rights
CREATE USER 'shipping_usr'@'%' IDENTIFIED BY 'shipping_psswd';
GRANT ALL PRIVILEGES ON shipping_db.* TO 'shipping_usr'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;

