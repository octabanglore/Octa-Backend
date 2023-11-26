--
-- PostgreSQL database dump
--

-- Dumped from database version 15.3
-- Dumped by pg_dump version 15.3

-- Started on 2023-11-26 13:00:37

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
-- TOC entry 3438 (class 0 OID 17989)
-- Dependencies: 234
-- Data for Name: USER_PASSWORD_TOKENS; Type: TABLE DATA; Schema: octa_admin; Owner: postgres
--

COPY octa_admin."USER_PASSWORD_TOKENS" ("PWD_TO_ID", "USER_ID", "PWD_TOK") FROM stdin;
\.


--
-- TOC entry 3432 (class 0 OID 17919)
-- Dependencies: 226
-- Data for Name: admin_activity_log; Type: TABLE DATA; Schema: octa_admin; Owner: postgres
--

COPY octa_admin.admin_activity_log (user_id, activity_datetime, session_id, activity_type_id, activity_message) FROM stdin;
\.


--
-- TOC entry 3423 (class 0 OID 17811)
-- Dependencies: 217
-- Data for Name: admin_connection; Type: TABLE DATA; Schema: octa_admin; Owner: postgres
--

COPY octa_admin.admin_connection (connection_id, connection_name, connection_type, connection_url, schema_name, min_pool, max_pool, active) FROM stdin;
1	MASTER	MASTER	jdbc:postgresql://localhost:5432/abc_admin	octa_admin	5	10	1
2	CUSTOMER	CUSTOMER	jdbc:postgresql://localhost:5432/abc_admin	octa_customer_01	5	10	1
\.


--
-- TOC entry 3426 (class 0 OID 17835)
-- Dependencies: 220
-- Data for Name: admin_group; Type: TABLE DATA; Schema: octa_admin; Owner: postgres
--

COPY octa_admin.admin_group (group_id, group_name, group_description, created_by, created_datetime, modified_by, modified_datetime, active, deleted) FROM stdin;
\.


--
-- TOC entry 3430 (class 0 OID 17875)
-- Dependencies: 224
-- Data for Name: admin_map_user_group; Type: TABLE DATA; Schema: octa_admin; Owner: postgres
--

COPY octa_admin.admin_map_user_group (group_id, created_datetime, user_id) FROM stdin;
\.


--
-- TOC entry 3429 (class 0 OID 17858)
-- Dependencies: 223
-- Data for Name: admin_map_user_role; Type: TABLE DATA; Schema: octa_admin; Owner: postgres
--

COPY octa_admin.admin_map_user_role (user_id, role_id, created_datetime) FROM stdin;
\.


--
-- TOC entry 3431 (class 0 OID 17892)
-- Dependencies: 225
-- Data for Name: admin_map_user_role_priv; Type: TABLE DATA; Schema: octa_admin; Owner: postgres
--

COPY octa_admin.admin_map_user_role_priv (role_id, user_id, utility_id, module_id) FROM stdin;
\.


--
-- TOC entry 3427 (class 0 OID 17844)
-- Dependencies: 221
-- Data for Name: admin_module; Type: TABLE DATA; Schema: octa_admin; Owner: postgres
--

COPY octa_admin.admin_module (module_id, module_name, module_description, created_by, created_datetime, modifiied_by, modified_datetime, active, deleted) FROM stdin;
\.


--
-- TOC entry 3425 (class 0 OID 17828)
-- Dependencies: 219
-- Data for Name: admin_role; Type: TABLE DATA; Schema: octa_admin; Owner: postgres
--

COPY octa_admin.admin_role (role_id, role_name, role_description, created_by, created_datetime, modified_by, modified_datetime, active, deleted) FROM stdin;
\.


--
-- TOC entry 3422 (class 0 OID 17804)
-- Dependencies: 216
-- Data for Name: admin_tenant; Type: TABLE DATA; Schema: octa_admin; Owner: postgres
--

COPY octa_admin.admin_tenant (tenant_id, tenant_name, tenant_description, login_rul, connection_id, tenant_scope, active, created_date) FROM stdin;
1	Octa Master	Octa Master	http://localhost	1	MASTER	1	\N
2	Octa Customer	Octa Customer	http://localho:8080	2	CUSTOMER	1	\N
\.


--
-- TOC entry 3424 (class 0 OID 17818)
-- Dependencies: 218
-- Data for Name: admin_user; Type: TABLE DATA; Schema: octa_admin; Owner: postgres
--

COPY octa_admin.admin_user (user_id, login_name, mobile_number, email_address, created_by, created_datetime, modified_by, modified_datetime, active, deleted, locked, first_name, last_name, password, reset_password_token, token_expiration) FROM stdin;
17	rajkumar.g@gmail.com	\N	rajkumar.g@gmail.com	\N	\N	\N	\N	0	0	0	raj	kumar	$2a$10$QUxe70BWHj8MehCZo4s4Z.CR6VuGcX0jEP07PMbNfozLQ2sXiEwPe	\N	\N
16	grkumar81@gmail.com	\N	grkumar81@gmail.com	\N	\N	\N	\N	0	0	0	raj	kumar	$2a$10$VmxFCNO16Oz8ZYAwoGFMxOjFAUdTXtzgl2SD4vmJ5D0oSjEnEEpSe	d9f5a0e3-f7c7-3a0a-9dd3-037c93dca0ea	2023-11-27 11:04:55.402
\.


--
-- TOC entry 3428 (class 0 OID 17851)
-- Dependencies: 222
-- Data for Name: admin_utility; Type: TABLE DATA; Schema: octa_admin; Owner: postgres
--

COPY octa_admin.admin_utility (utility_id, utlity_name, utility_description, created_by, created_datetime, modified_by, modified_datetime, active, deleted) FROM stdin;
\.


--
-- TOC entry 3433 (class 0 OID 17940)
-- Dependencies: 228
-- Data for Name: audit_login; Type: TABLE DATA; Schema: octa_admin; Owner: postgres
--

COPY octa_admin.audit_login (id, login_name) FROM stdin;
1	testuser
2	testuser
3	testuser
4	testuser
5	testuser
6	testuserc
7	testuserc
8	testuserc
9	testusercdddd
10	testuser
11	testuser
12	testuser
13	testuser
14	testuser
15	testuser
16	testuser
\.


--
-- TOC entry 3436 (class 0 OID 17983)
-- Dependencies: 232
-- Data for Name: jwt_token; Type: TABLE DATA; Schema: octa_admin; Owner: postgres
--

COPY octa_admin.jwt_token (token_id, user_token, token_type, token_revoked, token_expired, user_id) FROM stdin;
1	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJncmt1bWFyODFAZ21haWwuY29tIiwiaWF0IjoxNzAwODI4NTcwLCJleHAiOjE3MDA5MTQ5NzB9.N9gozOYNm_9DsfH_A-1XTq2p-WB5HBhOVgK4FrbLGhRK-WroZ_68JN0Tvzgruv1c	BEARER	0	0	16
2	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJyYWprdW1hci5nQGdtYWlsLmNvbSIsImlhdCI6MTcwMDgzMjE0NCwiZXhwIjoxNzAwOTE4NTQ0fQ.MaX-mIdXQ7Cg-rigyhxqpvUZkYDV5Te5-Rm9xcakUxm8T7KSB0eOS3aQ9B1Vfeky	BEARER	0	0	17
3	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJncmt1bWFyODFAZ21haWwuY29tIiwiaWF0IjoxNzAwODMyODgwLCJleHAiOjE3MDA5MTkyODB9.RdS_P9dn06PInvMUr3zdRSlGo5fMD4YgUo6CqJ7fkJCnPAWMXSuS_wj9eBqqNUeI	BEARER	0	0	16
4	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJncmt1bWFyODFAZ21haWwuY29tIiwiaWF0IjoxNzAwODM1NjkwLCJleHAiOjE3MDA5MjIwOTB9.G5kK2rzRN0zuT20du8-kOJijAc5at4WMTNYN2e9KpRn04yQgCZ47O8yN4pMEypR2	BEARER	0	0	16
5	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJncmt1bWFyODFAZ21haWwuY29tIiwiaWF0IjoxNzAwODg1Mjk3LCJleHAiOjE3MDA5NzE2OTd9.i6uNx0p--Co4erGeMUrInjO0BBR_bwkPr56gGYDSmpoJz6L48GIYnw7FY3abvHNZ	BEARER	0	0	16
6	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJncmt1bWFyODFAZ21haWwuY29tIiwiaWF0IjoxNzAwODg1Mjk3LCJleHAiOjE3MDA5NzE2OTd9.i6uNx0p--Co4erGeMUrInjO0BBR_bwkPr56gGYDSmpoJz6L48GIYnw7FY3abvHNZ	BEARER	0	0	16
7	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJncmt1bWFyODFAZ21haWwuY29tIiwiaWF0IjoxNzAwOTIzMTQ5LCJleHAiOjE3MDEwMDk1NDl9.x1kAfvjJG9EYSuepsw_esNpptH8xK-qVzs8mHww9C4gDWDlvUHdaLOjJHQfQiMDv	BEARER	0	0	16
8	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJncmt1bWFyODFAZ21haWwuY29tIiwiaWF0IjoxNzAwOTMxNzc2LCJleHAiOjE3MDEwMTgxNzZ9.epPYDKIDN0LKYRb_Av_BqV9WmPIPnZ3Nm-evb1iCuUV29spSAcOg4ATbnqMglLYs	BEARER	0	0	16
9	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJncmt1bWFyODFAZ21haWwuY29tIiwiaWF0IjoxNzAwOTY4NDMzLCJleHAiOjE3MDEwNTQ4MzN9.fbJ72ApQ1-Kmo6CmRUVB4gyDbrv9dKmF925yqSMZuJHu5y5mXk2p9NDLslyNDpWB	BEARER	0	0	16
10	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJncmt1bWFyODFAZ21haWwuY29tIiwiaWF0IjoxNzAwOTc2NDQyLCJleHAiOjE3MDEwNjI4NDJ9.PyVIW4PH5wx5hX9GshLCHXTyNhvh3UyTka9jebhcH4NXFug7ojzmkO4bpfBngvEm	BEARER	0	0	16
11	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJncmt1bWFyODFAZ21haWwuY29tIiwiaWF0IjoxNzAwOTc2NzI4LCJleHAiOjE3MDEwNjMxMjh9.grwcOkblTp4dftO55qRUzLlS1mZ7EAyGhtiLGZw4K8R_6jNOV8Cfz_MOQXMh2YwK	BEARER	0	0	16
12	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJncmt1bWFyODFAZ21haWwuY29tIiwiaWF0IjoxNzAwOTgwNDU4LCJleHAiOjE3MDEwNjY4NTh9.kBzYcbq9IPEvviC-pvk1-6XZzFy1DHONVeGZYsqXAPM4qif0ITQZxVlOi1pwyFAD	BEARER	0	0	16
\.


--
-- TOC entry 3444 (class 0 OID 0)
-- Dependencies: 230
-- Name: admin_user_user_id_seq; Type: SEQUENCE SET; Schema: octa_admin; Owner: postgres
--

SELECT pg_catalog.setval('octa_admin.admin_user_user_id_seq', 17, true);


--
-- TOC entry 3445 (class 0 OID 0)
-- Dependencies: 229
-- Name: audit_login_id_seq; Type: SEQUENCE SET; Schema: octa_admin; Owner: postgres
--

SELECT pg_catalog.setval('octa_admin.audit_login_id_seq', 16, true);


--
-- TOC entry 3446 (class 0 OID 0)
-- Dependencies: 233
-- Name: jwt_token_token_id_seq; Type: SEQUENCE SET; Schema: octa_admin; Owner: postgres
--

SELECT pg_catalog.setval('octa_admin.jwt_token_token_id_seq', 12, true);


-- Completed on 2023-11-26 13:00:37

--
-- PostgreSQL database dump complete
--

