drop table if exists float_sensors;
drop table if exists humidity;
drop table if exists particular_matter;
drop table if exists temperature;
drop table if exists parking_area_stats;
drop table if exists parking_area;
drop table if exists sensors_maintainer;
drop table if exists sensors;


CREATE TABLE IF NOT EXISTS sensors (
	id bigint not null,
	name varchar(255),
	battery varchar(255),
	charge varchar(255),
	type varchar(255),
	is_active boolean,
	CONSTRAINT sensors_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS parking_area (
	id bigint NOT NULL,
	fk_sensor_id bigint,
	address varchar(255),
	latitude varchar(255),
	longitude varchar(255),
	value boolean,
	last_update timestamp without time zone,
	CONSTRAINT parking_area_pkey PRIMARY KEY (id),
	CONSTRAINT parking_area_uniquefksensorid UNIQUE (fk_sensor_id),
    CONSTRAINT parking_area_foreignKey FOREIGN KEY (fk_sensor_id) REFERENCES public.sensors (id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT parking_area_uniqueLatitudeAndLongitude UNIQUE (latitude,longitude)
);

CREATE TABLE IF NOT EXISTS temperature (
	id bigint NOT NULL,
	fk_sensor_id bigint,
	address varchar(255),
	latitude varchar(255),
	longitude varchar(255),
	value varchar(255),
	CONSTRAINT temperature_pkey PRIMARY KEY (id),
	CONSTRAINT temperature_uniquefksensorid UNIQUE (fk_sensor_id),
    CONSTRAINT temperature_foreignKey FOREIGN KEY (fk_sensor_id) REFERENCES public.sensors (id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT temperature_uniqueLatitudeAndLongitude UNIQUE (latitude,longitude)
);

CREATE TABLE IF NOT EXISTS particular_matter (
	id bigint NOT NULL,
	fk_sensor_id bigint,
	address varchar(255),
	latitude varchar(255),
	longitude varchar(255),
	value varchar(255),
	CONSTRAINT particular_matter_pkey PRIMARY KEY (id),
	CONSTRAINT particular_matter_uniquefksensorid UNIQUE (fk_sensor_id),
    CONSTRAINT particular_matter_foreignKey FOREIGN KEY (fk_sensor_id) REFERENCES public.sensors (id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT particular_matter_uniqueLatitudeAndLongitude UNIQUE (latitude,longitude)
);

CREATE TABLE IF NOT EXISTS humidity (
	id bigint NOT NULL,
	fk_sensor_id bigint,
	address varchar(255),
	latitude varchar(255),
	longitude varchar(255),
	value varchar(255),
	CONSTRAINT humidity_pkey PRIMARY KEY (id),
	CONSTRAINT humidity_uniquefksensorid UNIQUE (fk_sensor_id),
    CONSTRAINT humidity_foreignKey FOREIGN KEY (fk_sensor_id) REFERENCES public.sensors (id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT humidity_uniqueLatitudeAndLongitude UNIQUE (latitude,longitude)
);

CREATE TABLE IF NOT EXISTS float_sensors (
	id bigint NOT NULL,
	fk_sensor_id bigint,
	address varchar(255),
	latitude varchar(255),
	longitude varchar(255),
	value varchar(255),
	CONSTRAINT float_sensors_pkey PRIMARY KEY (id),
	CONSTRAINT float_sensors_uniquefksensorid UNIQUE (fk_sensor_id),
    CONSTRAINT float_sensors_foreignKey FOREIGN KEY (fk_sensor_id) REFERENCES public.sensors (id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT float_sensors_uniqueLatitudeAndLongitude UNIQUE (latitude,longitude)
);

CREATE TABLE IF NOT EXISTS parking_area_stats
(
    id bigint NOT NULL,
    fk_sensor_id bigint,
    last_update timestamp without time zone,
    value boolean NOT NULL,
    CONSTRAINT parking_area_stats_pkey PRIMARY KEY (id),
    CONSTRAINT parking_area_stats_uniqueFkSensorIdAndLastUpdate UNIQUE (fk_sensor_id, last_update),
    CONSTRAINT parking_area_stats_foreign_key FOREIGN KEY (fk_sensor_id) REFERENCES parking_area (id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS sensors_maintainer
(
    id bigint NOT NULL,
    fk_sensor_id bigint,
    company character varying(255),
    mail character varying(255),
    name character varying(255),
    surname character varying(255),
    phone character varying(255),
    to_be_charged boolean,
    to_be_repaired boolean,
    type character varying(255),
    CONSTRAINT sensors_maintainer_pkey PRIMARY KEY (id),
    CONSTRAINT sensors_maintainer_uniqueForeignKeyAndType UNIQUE (fk_sensor_id, type),
    CONSTRAINT sensors_maintainer_unique_foreign_key FOREIGN KEY (fk_sensor_id) REFERENCES sensors (id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE
);

