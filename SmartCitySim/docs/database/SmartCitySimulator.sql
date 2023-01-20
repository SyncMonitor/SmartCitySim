--
-- PostgreSQL database dump
--

-- Dumped from database version 14.6 (Ubuntu 14.6-1.pgdg20.04+1)
-- Dumped by pg_dump version 15.1 (Ubuntu 15.1-1.pgdg20.04+1)

-- Started on 2023-01-20 09:57:55 CET

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 4 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

-- *not* creating schema, since initdb creates it


ALTER SCHEMA public OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 209 (class 1259 OID 44992)
-- Name: float_sensors; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.float_sensors (
    id bigint NOT NULL,
    address character varying(255) NOT NULL,
    latitude character varying(255) NOT NULL,
    longitude character varying(255) NOT NULL,
    "timestamp" timestamp(6) without time zone NOT NULL,
    value boolean NOT NULL,
    fk_sensor_id bigint NOT NULL
);


ALTER TABLE public.float_sensors OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 45080)
-- Name: float_sensors_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.float_sensors_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.float_sensors_seq OWNER TO postgres;

--
-- TOC entry 210 (class 1259 OID 44999)
-- Name: humidity; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.humidity (
    id bigint NOT NULL,
    address character varying(255) NOT NULL,
    latitude character varying(255) NOT NULL,
    longitude character varying(255) NOT NULL,
    "timestamp" timestamp(6) without time zone NOT NULL,
    value boolean NOT NULL,
    fk_sensor_id bigint NOT NULL
);


ALTER TABLE public.humidity OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 45081)
-- Name: humidity_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.humidity_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.humidity_seq OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 45006)
-- Name: maintainers_registry; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.maintainers_registry (
    id bigint NOT NULL,
    company character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    phone character varying(255) NOT NULL,
    surname character varying(255) NOT NULL
);


ALTER TABLE public.maintainers_registry OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 45082)
-- Name: maintainers_registry_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.maintainers_registry_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.maintainers_registry_seq OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 45013)
-- Name: parking_areas; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.parking_areas (
    id bigint NOT NULL,
    address character varying(255) NOT NULL,
    latitude character varying(255) NOT NULL,
    longitude character varying(255) NOT NULL
);


ALTER TABLE public.parking_areas OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 45083)
-- Name: parking_areas_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.parking_areas_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.parking_areas_seq OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 45020)
-- Name: parking_sensors; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.parking_sensors (
    id bigint NOT NULL,
    address character varying(255) NOT NULL,
    latitude character varying(255) NOT NULL,
    longitude character varying(255) NOT NULL,
    "timestamp" timestamp(6) without time zone NOT NULL,
    value boolean NOT NULL,
    fk_sensor_id bigint NOT NULL
);


ALTER TABLE public.parking_sensors OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 45084)
-- Name: parking_sensors_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.parking_sensors_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.parking_sensors_seq OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 45027)
-- Name: parking_spots; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.parking_spots (
    id bigint NOT NULL,
    latitude character varying(255) NOT NULL,
    longitude character varying(255) NOT NULL,
    fk_parking_area_id bigint NOT NULL
);


ALTER TABLE public.parking_spots OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 45085)
-- Name: parking_spots_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.parking_spots_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.parking_spots_seq OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 45034)
-- Name: particular_matter10; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.particular_matter10 (
    id bigint NOT NULL,
    address character varying(255) NOT NULL,
    latitude character varying(255) NOT NULL,
    longitude character varying(255) NOT NULL,
    "timestamp" timestamp(6) without time zone NOT NULL,
    value boolean NOT NULL,
    fk_sensor_id bigint NOT NULL
);


ALTER TABLE public.particular_matter10 OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 45086)
-- Name: particular_matter10_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.particular_matter10_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.particular_matter10_seq OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 45041)
-- Name: particular_matter25; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.particular_matter25 (
    id bigint NOT NULL,
    address character varying(255) NOT NULL,
    latitude character varying(255) NOT NULL,
    longitude character varying(255) NOT NULL,
    "timestamp" timestamp(6) without time zone NOT NULL,
    value boolean NOT NULL,
    fk_sensor_id bigint NOT NULL
);


ALTER TABLE public.particular_matter25 OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 45087)
-- Name: particular_matter25_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.particular_matter25_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.particular_matter25_seq OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 45048)
-- Name: sensors; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sensors (
    id bigint NOT NULL,
    battery character varying(255) NOT NULL,
    charge character varying(255) NOT NULL,
    is_active boolean NOT NULL,
    last_survey timestamp(6) without time zone NOT NULL,
    name character varying(255) NOT NULL,
    type character varying(255) NOT NULL,
    fk_maintainer_id bigint
);


ALTER TABLE public.sensors OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 45055)
-- Name: sensors_maintenance; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sensors_maintenance (
    id bigint NOT NULL,
    is_updating boolean NOT NULL,
    to_be_charged boolean NOT NULL,
    to_be_repaired boolean NOT NULL,
    fk_sensor_id bigint NOT NULL
);


ALTER TABLE public.sensors_maintenance OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 45088)
-- Name: sensors_maintenance_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sensors_maintenance_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sensors_maintenance_seq OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 45060)
-- Name: sensors_parking_spots; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sensors_parking_spots (
    sensors_id bigint NOT NULL,
    parking_spots_id bigint NOT NULL
);


ALTER TABLE public.sensors_parking_spots OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 45063)
-- Name: temperature; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.temperature (
    id bigint NOT NULL,
    address character varying(255) NOT NULL,
    latitude character varying(255) NOT NULL,
    longitude character varying(255) NOT NULL,
    "timestamp" timestamp(6) without time zone NOT NULL,
    value boolean NOT NULL,
    fk_sensor_id bigint NOT NULL
);


ALTER TABLE public.temperature OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 45089)
-- Name: temperature_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.temperature_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.temperature_seq OWNER TO postgres;

--
-- TOC entry 3409 (class 0 OID 44992)
-- Dependencies: 209
-- Data for Name: float_sensors; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.float_sensors (id, address, latitude, longitude, "timestamp", value, fk_sensor_id) FROM stdin;
\.


--
-- TOC entry 3410 (class 0 OID 44999)
-- Dependencies: 210
-- Data for Name: humidity; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.humidity (id, address, latitude, longitude, "timestamp", value, fk_sensor_id) FROM stdin;
\.


--
-- TOC entry 3411 (class 0 OID 45006)
-- Dependencies: 211
-- Data for Name: maintainers_registry; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.maintainers_registry (id, company, email, name, phone, surname) FROM stdin;
102	S. Mat f.lli	fllimat@gmail.com	luigi	3282222222	gialli
104	Sensori s.r.l.	mrossi@sensorisrl.com	Mario	3299999999	Rossi
106	SensorTec	gianni@sensortec.it	Gianni	3291111111	Verdi
\.


--
-- TOC entry 3412 (class 0 OID 45013)
-- Dependencies: 212
-- Data for Name: parking_areas; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.parking_areas (id, address, latitude, longitude) FROM stdin;
1	Via Roma 2	45.97765385	46.9879664
3	Via Roma 3	45.977653853	46.9879664
4	Via Milano 3	25.9653853	36.9819664
5	Via Padova 13	25.9053853	6.9819554
\.


--
-- TOC entry 3413 (class 0 OID 45020)
-- Dependencies: 213
-- Data for Name: parking_sensors; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.parking_sensors (id, address, latitude, longitude, "timestamp", value, fk_sensor_id) FROM stdin;
252	Padova Galleria Spagna	45.389040	11.928577	2023-01-20 09:47:08.026	f	1
253	Padova Galleria Spagna	45.389029	11.928598	2023-01-20 09:47:08.029	t	2
254	Padova Galleria Spagna	45.389028	11.928631	2023-01-20 09:47:08.03	f	3
255	Via Forcellini	45.392648	11.904846	2023-01-20 09:47:08.03	f	4
256	Via Forcellini	45.392618	11.904963	2023-01-20 09:47:08.03	t	5
257	Via Forcellini	45.392622	11.904921	2023-01-20 09:47:08.031	t	6
258	Piazza Capitaniato	45.407958	11.872594	2023-01-20 09:47:08.031	f	7
259	Piazza Capitaniato	45.407936	11.872584	2023-01-20 09:47:08.031	t	8
260	Piazza Capitaniato	45.407913	11.872580	2023-01-20 09:47:08.032	f	9
261	Prato della valle	45.397848	11.875080	2023-01-20 09:47:08.032	t	10
262	Prato della valle	45.397838	11.875102	2023-01-20 09:47:08.032	f	11
263	Prato della valle	45.397822	11.875126	2023-01-20 09:47:08.032	f	12
264	Via Sorio	45.400546	11.855238	2023-01-20 09:47:08.033	f	13
265	Via Sorio	45.400547	11.855173	2023-01-20 09:47:08.033	f	14
266	Via Sorio	45.400546	11.855015	2023-01-20 09:47:08.033	t	15
\.


--
-- TOC entry 3414 (class 0 OID 45027)
-- Dependencies: 214
-- Data for Name: parking_spots; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.parking_spots (id, latitude, longitude, fk_parking_area_id) FROM stdin;
1	13.855173	12.555173	1
3	13.9855173	12.555173	1
4	13.9855173	123.555173	3
\.


--
-- TOC entry 3415 (class 0 OID 45034)
-- Dependencies: 215
-- Data for Name: particular_matter10; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.particular_matter10 (id, address, latitude, longitude, "timestamp", value, fk_sensor_id) FROM stdin;
\.


--
-- TOC entry 3416 (class 0 OID 45041)
-- Dependencies: 216
-- Data for Name: particular_matter25; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.particular_matter25 (id, address, latitude, longitude, "timestamp", value, fk_sensor_id) FROM stdin;
\.


--
-- TOC entry 3417 (class 0 OID 45048)
-- Dependencies: 217
-- Data for Name: sensors; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sensors (id, battery, charge, is_active, last_survey, name, type, fk_maintainer_id) FROM stdin;
2	3,7V	3	t	2023-01-19 09:39:25.418	156A2A71	ParkingSensor	\N
3	3,7V	3	t	2023-01-19 09:39:25.419	156A2B71	ParkingSensor	\N
4	3,7V	3	t	2023-01-19 09:39:25.42	156A2C72	ParkingSensor	\N
5	3,7V	3	t	2023-01-19 09:39:25.422	156A2A73	ParkingSensor	\N
6	3,7V	3	t	2023-01-19 09:39:25.423	156A2B74	ParkingSensor	\N
7	3,7V	3	t	2023-01-19 09:39:25.424	156A2C75	ParkingSensor	\N
8	3,7V	3	t	2023-01-19 09:39:25.424	156A2A76	ParkingSensor	\N
9	3,7V	3	t	2023-01-19 09:39:25.425	156A2B77	ParkingSensor	\N
10	3,7V	3	t	2023-01-19 09:39:25.426	156A2C78	ParkingSensor	\N
11	3,7V	3	t	2023-01-19 09:39:25.427	156A2A79	ParkingSensor	\N
12	3,7V	3	t	2023-01-19 09:39:25.427	156A2B710	ParkingSensor	\N
13	3,7V	3	t	2023-01-19 09:39:25.428	156A2C711	ParkingSensor	\N
14	3,7V	3	t	2023-01-19 09:39:25.429	156A2A712	ParkingSensor	\N
15	3,7V	3	t	2023-01-19 09:42:46.298	156A2B7113	ParkingSensor	\N
1	3,7V	3	t	2023-01-19 14:41:31.058	156A2C71	ParkingSensor	\N
\.


--
-- TOC entry 3418 (class 0 OID 45055)
-- Dependencies: 218
-- Data for Name: sensors_maintenance; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sensors_maintenance (id, is_updating, to_be_charged, to_be_repaired, fk_sensor_id) FROM stdin;
1	t	t	t	1
8	f	t	t	8
52	f	f	f	2
53	f	f	f	3
55	f	f	f	5
56	f	f	f	6
57	f	f	f	7
58	f	f	f	9
59	f	f	f	10
60	f	f	f	11
61	f	f	f	12
62	f	f	f	13
63	f	f	f	14
64	f	f	f	15
102	f	f	f	4
\.


--
-- TOC entry 3419 (class 0 OID 45060)
-- Dependencies: 219
-- Data for Name: sensors_parking_spots; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sensors_parking_spots (sensors_id, parking_spots_id) FROM stdin;
\.


--
-- TOC entry 3420 (class 0 OID 45063)
-- Dependencies: 220
-- Data for Name: temperature; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.temperature (id, address, latitude, longitude, "timestamp", value, fk_sensor_id) FROM stdin;
\.


--
-- TOC entry 3437 (class 0 OID 0)
-- Dependencies: 221
-- Name: float_sensors_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.float_sensors_seq', 1, false);


--
-- TOC entry 3438 (class 0 OID 0)
-- Dependencies: 222
-- Name: humidity_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.humidity_seq', 1, false);


--
-- TOC entry 3439 (class 0 OID 0)
-- Dependencies: 223
-- Name: maintainers_registry_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.maintainers_registry_seq', 151, true);


--
-- TOC entry 3440 (class 0 OID 0)
-- Dependencies: 224
-- Name: parking_areas_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.parking_areas_seq', 51, true);


--
-- TOC entry 3441 (class 0 OID 0)
-- Dependencies: 225
-- Name: parking_sensors_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.parking_sensors_seq', 301, true);


--
-- TOC entry 3442 (class 0 OID 0)
-- Dependencies: 226
-- Name: parking_spots_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.parking_spots_seq', 101, true);


--
-- TOC entry 3443 (class 0 OID 0)
-- Dependencies: 227
-- Name: particular_matter10_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.particular_matter10_seq', 1, false);


--
-- TOC entry 3444 (class 0 OID 0)
-- Dependencies: 228
-- Name: particular_matter25_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.particular_matter25_seq', 1, false);


--
-- TOC entry 3445 (class 0 OID 0)
-- Dependencies: 229
-- Name: sensors_maintenance_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sensors_maintenance_seq', 151, true);


--
-- TOC entry 3446 (class 0 OID 0)
-- Dependencies: 230
-- Name: temperature_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.temperature_seq', 1, false);


--
-- TOC entry 3228 (class 2606 OID 44998)
-- Name: float_sensors float_sensors_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.float_sensors
    ADD CONSTRAINT float_sensors_pkey PRIMARY KEY (id);


--
-- TOC entry 3230 (class 2606 OID 45005)
-- Name: humidity humidity_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.humidity
    ADD CONSTRAINT humidity_pkey PRIMARY KEY (id);


--
-- TOC entry 3232 (class 2606 OID 45012)
-- Name: maintainers_registry maintainers_registry_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.maintainers_registry
    ADD CONSTRAINT maintainers_registry_pkey PRIMARY KEY (id);


--
-- TOC entry 3240 (class 2606 OID 45019)
-- Name: parking_areas parking_areas_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parking_areas
    ADD CONSTRAINT parking_areas_pkey PRIMARY KEY (id);


--
-- TOC entry 3244 (class 2606 OID 45026)
-- Name: parking_sensors parking_sensors_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parking_sensors
    ADD CONSTRAINT parking_sensors_pkey PRIMARY KEY (id);


--
-- TOC entry 3246 (class 2606 OID 45033)
-- Name: parking_spots parking_spots_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parking_spots
    ADD CONSTRAINT parking_spots_pkey PRIMARY KEY (id);


--
-- TOC entry 3250 (class 2606 OID 45040)
-- Name: particular_matter10 particular_matter10_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.particular_matter10
    ADD CONSTRAINT particular_matter10_pkey PRIMARY KEY (id);


--
-- TOC entry 3252 (class 2606 OID 45047)
-- Name: particular_matter25 particular_matter25_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.particular_matter25
    ADD CONSTRAINT particular_matter25_pkey PRIMARY KEY (id);


--
-- TOC entry 3256 (class 2606 OID 45059)
-- Name: sensors_maintenance sensors_maintenance_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sensors_maintenance
    ADD CONSTRAINT sensors_maintenance_pkey PRIMARY KEY (id);


--
-- TOC entry 3254 (class 2606 OID 45054)
-- Name: sensors sensors_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sensors
    ADD CONSTRAINT sensors_pkey PRIMARY KEY (id);


--
-- TOC entry 3258 (class 2606 OID 45069)
-- Name: temperature temperature_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.temperature
    ADD CONSTRAINT temperature_pkey PRIMARY KEY (id);


--
-- TOC entry 3234 (class 2606 OID 45075)
-- Name: maintainers_registry uk_3rhxiju1fhi5ml9nq624mu2kx; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.maintainers_registry
    ADD CONSTRAINT uk_3rhxiju1fhi5ml9nq624mu2kx UNIQUE (phone);


--
-- TOC entry 3236 (class 2606 OID 45071)
-- Name: maintainers_registry uk_l58slnyjksv4lgmd7x1ddiy5; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.maintainers_registry
    ADD CONSTRAINT uk_l58slnyjksv4lgmd7x1ddiy5 UNIQUE (company);


--
-- TOC entry 3238 (class 2606 OID 45073)
-- Name: maintainers_registry uk_pjnrroln9fu01drgbx27w98b1; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.maintainers_registry
    ADD CONSTRAINT uk_pjnrroln9fu01drgbx27w98b1 UNIQUE (email);


--
-- TOC entry 3248 (class 2606 OID 45079)
-- Name: parking_spots ukfllkiacbngl7ppdt88po9233y; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parking_spots
    ADD CONSTRAINT ukfllkiacbngl7ppdt88po9233y UNIQUE (latitude, longitude);


--
-- TOC entry 3242 (class 2606 OID 45077)
-- Name: parking_areas uki7hb5ekd3x8amr4sch059xpyv; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parking_areas
    ADD CONSTRAINT uki7hb5ekd3x8amr4sch059xpyv UNIQUE (latitude, longitude);


--
-- TOC entry 3265 (class 2606 OID 45120)
-- Name: sensors fk50r8j5yd4mnl3urg3xt4aylgw; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sensors
    ADD CONSTRAINT fk50r8j5yd4mnl3urg3xt4aylgw FOREIGN KEY (fk_maintainer_id) REFERENCES public.maintainers_registry(id);


--
-- TOC entry 3269 (class 2606 OID 45140)
-- Name: temperature fk54edpmnrd32m3kfeiprtxfhri; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.temperature
    ADD CONSTRAINT fk54edpmnrd32m3kfeiprtxfhri FOREIGN KEY (fk_sensor_id) REFERENCES public.sensors(id);


--
-- TOC entry 3262 (class 2606 OID 45105)
-- Name: parking_spots fkaj0bwbbx0uiom6pppskeodh3f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parking_spots
    ADD CONSTRAINT fkaj0bwbbx0uiom6pppskeodh3f FOREIGN KEY (fk_parking_area_id) REFERENCES public.parking_areas(id);


--
-- TOC entry 3263 (class 2606 OID 45110)
-- Name: particular_matter10 fkefyhfpaf1upa6dpffnw6udjnx; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.particular_matter10
    ADD CONSTRAINT fkefyhfpaf1upa6dpffnw6udjnx FOREIGN KEY (fk_sensor_id) REFERENCES public.sensors(id);


--
-- TOC entry 3267 (class 2606 OID 45135)
-- Name: sensors_parking_spots fkijrb5xb8qmb08oidlv78gi4dx; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sensors_parking_spots
    ADD CONSTRAINT fkijrb5xb8qmb08oidlv78gi4dx FOREIGN KEY (sensors_id) REFERENCES public.sensors(id);


--
-- TOC entry 3259 (class 2606 OID 45090)
-- Name: float_sensors fkkabbycu8k7bglcb225ydr9k1v; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.float_sensors
    ADD CONSTRAINT fkkabbycu8k7bglcb225ydr9k1v FOREIGN KEY (fk_sensor_id) REFERENCES public.sensors(id);


--
-- TOC entry 3260 (class 2606 OID 45095)
-- Name: humidity fkl7dggf6h3n1jroglo7kxc1d7w; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.humidity
    ADD CONSTRAINT fkl7dggf6h3n1jroglo7kxc1d7w FOREIGN KEY (fk_sensor_id) REFERENCES public.sensors(id);


--
-- TOC entry 3268 (class 2606 OID 45130)
-- Name: sensors_parking_spots fkl8a83oo5sc30smyxkwmpeowsm; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sensors_parking_spots
    ADD CONSTRAINT fkl8a83oo5sc30smyxkwmpeowsm FOREIGN KEY (parking_spots_id) REFERENCES public.parking_spots(id);


--
-- TOC entry 3264 (class 2606 OID 45115)
-- Name: particular_matter25 fkn06mmrwu29etv8axdsrysybse; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.particular_matter25
    ADD CONSTRAINT fkn06mmrwu29etv8axdsrysybse FOREIGN KEY (fk_sensor_id) REFERENCES public.sensors(id);


--
-- TOC entry 3266 (class 2606 OID 45125)
-- Name: sensors_maintenance fkntkc667mxjio48l8mbcpl14o4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sensors_maintenance
    ADD CONSTRAINT fkntkc667mxjio48l8mbcpl14o4 FOREIGN KEY (fk_sensor_id) REFERENCES public.sensors(id);


--
-- TOC entry 3261 (class 2606 OID 45100)
-- Name: parking_sensors fkqou2h92obtuocups6oudvay7s; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parking_sensors
    ADD CONSTRAINT fkqou2h92obtuocups6oudvay7s FOREIGN KEY (fk_sensor_id) REFERENCES public.sensors(id);


--
-- TOC entry 3436 (class 0 OID 0)
-- Dependencies: 4
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2023-01-20 09:57:55 CET

--
-- PostgreSQL database dump complete
--

