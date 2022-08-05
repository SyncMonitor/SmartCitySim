drop table if exists float_sensors;
drop table if exists humidity;
drop table if exists particular_matter;
drop table if exists temperature;
DROP TABLE IF EXISTS parking_area;
DROP TABLE IF EXISTS sensors;

CREATE TABLE `sensors` (
  `id` bigint NOT NULL,
  `battery` varchar(255) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `parking_area` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `fk_sensor_id` bigint DEFAULT NULL,
  `latitude` varchar(255) DEFAULT NULL,
  `longitude` varchar(255) DEFAULT NULL,
  `value` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKco1484bbwa2jmrrccirtcb3eb` (`fk_sensor_id`)
  --,
  --CONSTRAINT UniqueLatitudeAndLongitude UNIQUE (latitude(125),longitude(125))
) ENGINE=MyISAM AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `temperature` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `fk_sensor_id` bigint DEFAULT NULL,
  `latitude` varchar(255) DEFAULT NULL,
  `longitude` varchar(255) DEFAULT NULL,
  `value` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKco1484bbwa2jmrrccirtcb3eb` (`fk_sensor_id`)
  --,
  --CONSTRAINT UniqueLatitudeAndLongitude UNIQUE (latitude(125),longitude(125))
) ENGINE=MyISAM AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `particular_matter` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `fk_sensor_id` bigint DEFAULT NULL,
  `latitude` varchar(255) DEFAULT NULL,
  `longitude` varchar(255) DEFAULT NULL,
  `value` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKco1484bbwa2jmrrccirtcb3eb` (`fk_sensor_id`)
  --,
  --CONSTRAINT UniqueLatitudeAndLongitude UNIQUE (latitude(125),longitude(125))
) ENGINE=MyISAM AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `humidity` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `fk_sensor_id` bigint DEFAULT NULL,
  `latitude` varchar(255) DEFAULT NULL,
  `longitude` varchar(255) DEFAULT NULL,
  `value` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKco1484bbwa2jmrrccirtcb3eb` (`fk_sensor_id`)
  --,
  --CONSTRAINT UniqueLatitudeAndLongitude UNIQUE (latitude(125),longitude(125))
) ENGINE=MyISAM AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `float_sensors` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `fk_sensor_id` bigint DEFAULT NULL,
  `latitude` varchar(255) DEFAULT NULL,
  `longitude` varchar(255) DEFAULT NULL,
  `value` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKco1484bbwa2jmrrccirtcb3eb` (`fk_sensor_id`)
  --,
  --CONSTRAINT UniqueLatitudeAndLongitude UNIQUE (latitude(125),longitude(125))
) ENGINE=MyISAM AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;




