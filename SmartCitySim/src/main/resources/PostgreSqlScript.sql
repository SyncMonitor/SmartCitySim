drop table if exists float_sensors;
drop table if exists humidity;
drop table if exists particular_matter;
drop table if exists temperature;
drop table if exists parking_area;
drop table if exists sensors;


create table sensors (
	id int not null,
	name varchar(255),
	battery varchar(255),
	type varchar(255),
	is_active boolean,
	primary key (id)
);

create table parking_area (
	fk_sensor_id int,
	id int,
	latitude varchar(255),
	longitude varchar(255),
	address varchar(255),
	value boolean,
	primary key (id),
	unique (latitude,longitude)
);

create table temperature (
	fk_sensor_id int,
	id int,
	latitude varchar(255),
	longitude varchar(255),
	address varchar(255),
	value varchar(255),
	primary key (id),
	unique (latitude,longitude)
);

create table particular_matter (
	fk_sensor_id int,
	id int,
	latitude varchar(255),
	longitude varchar(255),
	address varchar(255),
	value varchar(255),
	primary key (id),
	unique (latitude,longitude)
);

create table humidity (
	fk_sensor_id int,
	id int,
	latitude varchar(255),
	longitude varchar(255),
	address varchar(255) ,
	value varchar(255),
	primary key (id),
	unique (latitude,longitude)
);

create table float_sensors (
	fk_sensor_id int,
	id int,
	latitude varchar(255),
	longitude varchar(255),
	address varchar(255),
	value varchar(255),
	primary key (id),
	unique (latitude,longitude)
);

ALTER TABLE parking_area
ADD FOREIGN KEY (fk_sensor_id) REFERENCES sensors(id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE temperature
ADD FOREIGN KEY (fk_sensor_id) REFERENCES sensors(id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE particular_matter
ADD FOREIGN KEY (fk_sensor_id) REFERENCES sensors(id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE Humidity
ADD FOREIGN KEY (fk_sensor_id) REFERENCES sensors(id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE float_sensors
ADD FOREIGN KEY (fk_sensor_id) REFERENCES sensors(id) ON DELETE CASCADE ON UPDATE CASCADE;