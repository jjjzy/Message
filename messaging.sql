CREATE USER 'messaging_u'@'%' IDENTIFIED BY 'messaging_password_2022';

GRANT ALL PRIVILEGES ON *.* TO messaging_u@'%';

CREATE DATABASE `messaging_test`;

USE `messaging_test`;
CREATE TABLE users (
  id int(11) NOT NULL AUTO_INCREMENT,
  username varchar(128) CHARACTER SET utf8 DEFAULT NULL,
  password varchar(128) CHARACTER SET utf8 DEFAULT NULL,
  email varchar(128) DEFAULT NULL,
  nickname varchar(128) DEFAULT NULL,
  gender varchar(128) DEFAULT NULL,
  address varchar(128) DEFAULT NULL,
  register_time datetime DEFAULT NULL,
  login_token varchar(128) CHARACTER SET utf8 DEFAULT NULL,
  last_login_time datetime DEFAULT NULL,
  is_valid tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

CREATE TABLE user_validation_code (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `validation_code` varchar(128) CHARACTER SET utf8 DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `friend_invitation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `from_user_id` int(11) NOT NULL,
  `to_user_id` int(11) NOT NULL,
  `send_time` datetime DEFAULT NULL,
  `message` varchar(128) CHARACTER SET utf8 DEFAULT NULL,
  `status` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `conversation_users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `conversation_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `conversations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(128) CHARACTER SET utf8 DEFAULT NULL,
  `notice` varchar(128) CHARACTER SET utf8 DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
