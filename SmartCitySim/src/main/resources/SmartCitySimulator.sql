drop table if exists Float;
drop table if exists Humidity;
drop table if exists Particular_matter;
drop table if exists Temperature;
drop table if exists Parking_area;
drop table if exists Sensors;


create table Sensors (
	id int,
	name varchar(255),
	battery varchar(255) not null,
	type varchar(255) not null,
	is_active boolean not null,
	primary key (id)
);

create table Parking_area (
	fk_sensor_id int,
	latitude varchar(255),
	longitude varchar(255),
	address varchar(255) not null,
	value varchar(255) not null,
	primary key (latitude, longitude)
);

create table Temperature (
	fk_sensor_id int,
	latitude varchar(255),
	longitude varchar(255),
	address varchar(255) not null,
	value varchar(255) not null,
	primary key (latitude, longitude)
);

create table Particular_matter (
	fk_sensor_id int,
	latitude varchar(255),
	longitude varchar(255),
	address varchar(255) not null,
	value varchar(255) not null,
	primary key (latitude, longitude)
);

create table Humidity (
	fk_sensor_id int,
	latitude varchar(255),
	longitude varchar(255),
	address varchar(255) not null,
	value varchar(255) not null,
	primary key (latitude, longitude)
);

create table Float (
	fk_sensor_id int,
	latitude varchar(255),
	longitude varchar(255),
	address varchar(255) not null,
	value varchar(255) not null,
	primary key (latitude, longitude)
);

ALTER TABLE Parking_area
ADD FOREIGN KEY (fk_sensor_id)
REFERENCES Sensors(id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE Temperature
ADD FOREIGN KEY (fk_sensor_id)
REFERENCES Sensors(id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE Particular_matter
ADD FOREIGN KEY (fk_sensor_id)
REFERENCES Sensors(id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE Humidity
ADD FOREIGN KEY (fk_sensor_id)
REFERENCES Sensors(id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE Float
ADD FOREIGN KEY (fk_sensor_id)
REFERENCES Sensors(id) ON DELETE CASCADE ON UPDATE CASCADE;


insert into Sensors Values 
	(1,'Name1','3.7V','Type1',true),
	(2,'Name2','3.8V','Type2',false),
	(3,'Name3','3.9V','Type3',true);