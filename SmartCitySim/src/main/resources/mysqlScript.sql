drop table if exists float_sensors;
drop table if exists humidity;
drop table if exists particular_matter;
drop table if exists temperature;
DROP TABLE IF EXISTS parking_area;
DROP TABLE IF EXISTS sensors;
 
CREATE TABLE `sensors` (
  `id` bigint NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `battery` varchar(255) DEFAULT NULL,
  `charge` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `parking_area` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `fk_sensor_id` bigint DEFAULT NULL,
  `last_update` datetime DEFAULT NULL,
  `latitude` varchar(255) DEFAULT NULL,
  `longitude` varchar(255) DEFAULT NULL,
  `value` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `parking_area_UniqueFkSensorId` (`fk_sensor_id`),
  UNIQUE KEY `parking_area_UniqueLatitudeAndLongitude` (`latitude`,`longitude`)
);

CREATE TABLE `temperature` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `fk_sensor_id` bigint DEFAULT NULL,
  `last_update` datetime DEFAULT NULL,
  `latitude` varchar(255) DEFAULT NULL,
  `longitude` varchar(255) DEFAULT NULL,
  `value` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `temperature_UniqueFkSensorId` (`fk_sensor_id`),
  UNIQUE KEY `temperature_UniqueLatitudeAndLongitude` (`latitude`,`longitude`)
);

CREATE TABLE `particular_matter` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `fk_sensor_id` bigint DEFAULT NULL,
  `last_update` datetime DEFAULT NULL,
  `latitude` varchar(255) DEFAULT NULL,
  `longitude` varchar(255) DEFAULT NULL,
  `value` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `particular_matter_UniqueFkSensorId` (`fk_sensor_id`),
  UNIQUE KEY `particular_matter_UniqueLatitudeAndLongitude` (`latitude`,`longitude`)
);

CREATE TABLE `humidity` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `fk_sensor_id` bigint DEFAULT NULL,
  `last_update` datetime DEFAULT NULL,
  `latitude` varchar(255) DEFAULT NULL,
  `longitude` varchar(255) DEFAULT NULL,
  `value` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `humidity_UniqueFkSensorId` (`fk_sensor_id`),
  UNIQUE KEY `humidity_UniqueLatitudeAndLongitude` (`latitude`,`longitude`)
);

CREATE TABLE `float_sensors` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `fk_sensor_id` bigint DEFAULT NULL,
  `last_update` datetime DEFAULT NULL,
  `latitude` varchar(255) DEFAULT NULL,
  `longitude` varchar(255) DEFAULT NULL,
  `value` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `float_sensors_UniqueFkSensorId` (`fk_sensor_id`),
  UNIQUE KEY `float_sensors_UniqueLatitudeAndLongitude` (`latitude`,`longitude`)
);

CREATE TABLE `parking_area_stats` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fk_sensor_id` bigint DEFAULT NULL,
  `last_update` datetime DEFAULT NULL,
  `value` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKmi3i3g9dxyxi62b5mj0y4dn6d` (`fk_sensor_id`,`last_update`)
);

CREATE TABLE `sensors_maintainer` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `company` varchar(255) DEFAULT NULL,
  `fk_sensor_id` bigint DEFAULT NULL,
  `mail` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `to_be_charged` bit(1) DEFAULT NULL,
  `to_be_repaired` bit(1) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKn4dduerppxaj92kipagnlwh70` (`fk_sensor_id`)
);