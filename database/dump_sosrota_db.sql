--
-- PostgreSQL database dump
--

\restrict RXPO6YTGjh23pZgCJ4Kc265Wn7K0q4Aefpq6gAtqZF0AaTTWX3srvTg5jD9kYuL

-- Dumped from database version 18.0
-- Dumped by pg_dump version 18.0

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: ambulancias; Type: TABLE; Schema: public; Owner: sosrota_user
--

CREATE TABLE public.ambulancias (
    id bigint NOT NULL,
    placa character varying(255) NOT NULL,
    tipo character varying(50) NOT NULL,
    status character varying(50) NOT NULL,
    base_id bigint NOT NULL
);


ALTER TABLE public.ambulancias OWNER TO sosrota_user;

--
-- Name: ambulancias_id_seq; Type: SEQUENCE; Schema: public; Owner: sosrota_user
--

CREATE SEQUENCE public.ambulancias_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.ambulancias_id_seq OWNER TO sosrota_user;

--
-- Name: ambulancias_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: sosrota_user
--

ALTER SEQUENCE public.ambulancias_id_seq OWNED BY public.ambulancias.id;


--
-- Name: atendimentos; Type: TABLE; Schema: public; Owner: sosrota_user
--

CREATE TABLE public.atendimentos (
    id bigint NOT NULL,
    ocorrencia_id bigint NOT NULL,
    ambulancia_id bigint NOT NULL,
    data_hora_despacho timestamp without time zone NOT NULL,
    data_hora_chegada timestamp without time zone,
    distancia_km double precision NOT NULL
);


ALTER TABLE public.atendimentos OWNER TO sosrota_user;

--
-- Name: atendimentos_id_seq; Type: SEQUENCE; Schema: public; Owner: sosrota_user
--

CREATE SEQUENCE public.atendimentos_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.atendimentos_id_seq OWNER TO sosrota_user;

--
-- Name: atendimentos_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: sosrota_user
--

ALTER SEQUENCE public.atendimentos_id_seq OWNED BY public.atendimentos.id;


--
-- Name: bairros; Type: TABLE; Schema: public; Owner: sosrota_user
--

CREATE TABLE public.bairros (
    id bigint NOT NULL,
    nome character varying(255) NOT NULL
);


ALTER TABLE public.bairros OWNER TO sosrota_user;

--
-- Name: bairros_id_seq; Type: SEQUENCE; Schema: public; Owner: sosrota_user
--

CREATE SEQUENCE public.bairros_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.bairros_id_seq OWNER TO sosrota_user;

--
-- Name: bairros_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: sosrota_user
--

ALTER SEQUENCE public.bairros_id_seq OWNED BY public.bairros.id;


--
-- Name: bases; Type: TABLE; Schema: public; Owner: sosrota_user
--

CREATE TABLE public.bases (
    id bigint NOT NULL,
    nome character varying(255) NOT NULL,
    endereco character varying(255) NOT NULL,
    bairro_id bigint NOT NULL
);


ALTER TABLE public.bases OWNER TO sosrota_user;

--
-- Name: bases_id_seq; Type: SEQUENCE; Schema: public; Owner: sosrota_user
--

CREATE SEQUENCE public.bases_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.bases_id_seq OWNER TO sosrota_user;

--
-- Name: bases_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: sosrota_user
--

ALTER SEQUENCE public.bases_id_seq OWNED BY public.bases.id;


--
-- Name: conexoes; Type: TABLE; Schema: public; Owner: sosrota_user
--

CREATE TABLE public.conexoes (
    id bigint NOT NULL,
    bairro_origem_id bigint NOT NULL,
    bairro_destino_id bigint NOT NULL,
    distancia_km double precision NOT NULL
);


ALTER TABLE public.conexoes OWNER TO sosrota_user;

--
-- Name: conexoes_id_seq; Type: SEQUENCE; Schema: public; Owner: sosrota_user
--

CREATE SEQUENCE public.conexoes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.conexoes_id_seq OWNER TO sosrota_user;

--
-- Name: conexoes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: sosrota_user
--

ALTER SEQUENCE public.conexoes_id_seq OWNED BY public.conexoes.id;


--
-- Name: equipe_profissional; Type: TABLE; Schema: public; Owner: sosrota_user
--

CREATE TABLE public.equipe_profissional (
    id bigint NOT NULL,
    equipe_id bigint NOT NULL,
    profissional_id bigint NOT NULL
);


ALTER TABLE public.equipe_profissional OWNER TO sosrota_user;

--
-- Name: equipe_profissional_id_seq; Type: SEQUENCE; Schema: public; Owner: sosrota_user
--

CREATE SEQUENCE public.equipe_profissional_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.equipe_profissional_id_seq OWNER TO sosrota_user;

--
-- Name: equipe_profissional_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: sosrota_user
--

ALTER SEQUENCE public.equipe_profissional_id_seq OWNED BY public.equipe_profissional.id;


--
-- Name: equipes; Type: TABLE; Schema: public; Owner: sosrota_user
--

CREATE TABLE public.equipes (
    id bigint NOT NULL,
    descricao character varying(255) NOT NULL,
    ambulancia_id bigint NOT NULL
);


ALTER TABLE public.equipes OWNER TO sosrota_user;

--
-- Name: equipes_id_seq; Type: SEQUENCE; Schema: public; Owner: sosrota_user
--

CREATE SEQUENCE public.equipes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.equipes_id_seq OWNER TO sosrota_user;

--
-- Name: equipes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: sosrota_user
--

ALTER SEQUENCE public.equipes_id_seq OWNED BY public.equipes.id;


--
-- Name: ocorrencias; Type: TABLE; Schema: public; Owner: sosrota_user
--

CREATE TABLE public.ocorrencias (
    id bigint NOT NULL,
    tipo character varying(255) NOT NULL,
    gravidade character varying(50) NOT NULL,
    local character varying(255) NOT NULL,
    data_hora_abertura timestamp without time zone NOT NULL,
    status character varying(50) NOT NULL,
    observacao text,
    bairro_id bigint NOT NULL
);


ALTER TABLE public.ocorrencias OWNER TO sosrota_user;

--
-- Name: ocorrencias_id_seq; Type: SEQUENCE; Schema: public; Owner: sosrota_user
--

CREATE SEQUENCE public.ocorrencias_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.ocorrencias_id_seq OWNER TO sosrota_user;

--
-- Name: ocorrencias_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: sosrota_user
--

ALTER SEQUENCE public.ocorrencias_id_seq OWNED BY public.ocorrencias.id;


--
-- Name: profissionais; Type: TABLE; Schema: public; Owner: sosrota_user
--

CREATE TABLE public.profissionais (
    id bigint NOT NULL,
    nome character varying(255) NOT NULL,
    funcao character varying(255) NOT NULL,
    contato character varying(255) NOT NULL,
    ativo boolean DEFAULT true NOT NULL
);


ALTER TABLE public.profissionais OWNER TO sosrota_user;

--
-- Name: profissionais_id_seq; Type: SEQUENCE; Schema: public; Owner: sosrota_user
--

CREATE SEQUENCE public.profissionais_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.profissionais_id_seq OWNER TO sosrota_user;

--
-- Name: profissionais_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: sosrota_user
--

ALTER SEQUENCE public.profissionais_id_seq OWNED BY public.profissionais.id;


--
-- Name: usuarios; Type: TABLE; Schema: public; Owner: sosrota_user
--

CREATE TABLE public.usuarios (
    id bigint NOT NULL,
    login character varying(255) NOT NULL,
    senha_hash character varying(255) NOT NULL,
    perfil character varying(50) NOT NULL
);


ALTER TABLE public.usuarios OWNER TO sosrota_user;

--
-- Name: usuarios_id_seq; Type: SEQUENCE; Schema: public; Owner: sosrota_user
--

CREATE SEQUENCE public.usuarios_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.usuarios_id_seq OWNER TO sosrota_user;

--
-- Name: usuarios_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: sosrota_user
--

ALTER SEQUENCE public.usuarios_id_seq OWNED BY public.usuarios.id;


--
-- Name: ambulancias id; Type: DEFAULT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.ambulancias ALTER COLUMN id SET DEFAULT nextval('public.ambulancias_id_seq'::regclass);


--
-- Name: atendimentos id; Type: DEFAULT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.atendimentos ALTER COLUMN id SET DEFAULT nextval('public.atendimentos_id_seq'::regclass);


--
-- Name: bairros id; Type: DEFAULT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.bairros ALTER COLUMN id SET DEFAULT nextval('public.bairros_id_seq'::regclass);


--
-- Name: bases id; Type: DEFAULT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.bases ALTER COLUMN id SET DEFAULT nextval('public.bases_id_seq'::regclass);


--
-- Name: conexoes id; Type: DEFAULT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.conexoes ALTER COLUMN id SET DEFAULT nextval('public.conexoes_id_seq'::regclass);


--
-- Name: equipe_profissional id; Type: DEFAULT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.equipe_profissional ALTER COLUMN id SET DEFAULT nextval('public.equipe_profissional_id_seq'::regclass);


--
-- Name: equipes id; Type: DEFAULT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.equipes ALTER COLUMN id SET DEFAULT nextval('public.equipes_id_seq'::regclass);


--
-- Name: ocorrencias id; Type: DEFAULT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.ocorrencias ALTER COLUMN id SET DEFAULT nextval('public.ocorrencias_id_seq'::regclass);


--
-- Name: profissionais id; Type: DEFAULT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.profissionais ALTER COLUMN id SET DEFAULT nextval('public.profissionais_id_seq'::regclass);


--
-- Name: usuarios id; Type: DEFAULT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.usuarios ALTER COLUMN id SET DEFAULT nextval('public.usuarios_id_seq'::regclass);


--
-- Data for Name: ambulancias; Type: TABLE DATA; Schema: public; Owner: sosrota_user
--

COPY public.ambulancias (id, placa, tipo, status, base_id) FROM stdin;
\.


--
-- Data for Name: atendimentos; Type: TABLE DATA; Schema: public; Owner: sosrota_user
--

COPY public.atendimentos (id, ocorrencia_id, ambulancia_id, data_hora_despacho, data_hora_chegada, distancia_km) FROM stdin;
\.


--
-- Data for Name: bairros; Type: TABLE DATA; Schema: public; Owner: sosrota_user
--

COPY public.bairros (id, nome) FROM stdin;
\.


--
-- Data for Name: bases; Type: TABLE DATA; Schema: public; Owner: sosrota_user
--

COPY public.bases (id, nome, endereco, bairro_id) FROM stdin;
\.


--
-- Data for Name: conexoes; Type: TABLE DATA; Schema: public; Owner: sosrota_user
--

COPY public.conexoes (id, bairro_origem_id, bairro_destino_id, distancia_km) FROM stdin;
\.


--
-- Data for Name: equipe_profissional; Type: TABLE DATA; Schema: public; Owner: sosrota_user
--

COPY public.equipe_profissional (id, equipe_id, profissional_id) FROM stdin;
\.


--
-- Data for Name: equipes; Type: TABLE DATA; Schema: public; Owner: sosrota_user
--

COPY public.equipes (id, descricao, ambulancia_id) FROM stdin;
\.


--
-- Data for Name: ocorrencias; Type: TABLE DATA; Schema: public; Owner: sosrota_user
--

COPY public.ocorrencias (id, tipo, gravidade, local, data_hora_abertura, status, observacao, bairro_id) FROM stdin;
\.


--
-- Data for Name: profissionais; Type: TABLE DATA; Schema: public; Owner: sosrota_user
--

COPY public.profissionais (id, nome, funcao, contato, ativo) FROM stdin;
\.


--
-- Data for Name: usuarios; Type: TABLE DATA; Schema: public; Owner: sosrota_user
--

COPY public.usuarios (id, login, senha_hash, perfil) FROM stdin;
1	admin	$2a$10$q3Nc2Si4i43TBRNhBwI31.nukBJtWQynRicT/yhnNGvvJawXHz.Hm	GESTOR
2	operador	$2a$10$MxOMmfUbIY.GMGH5hk9g2.nrLOAS4M2S1dyGN9S02tpxQTrtu7FkO	REGULADOR
\.


--
-- Name: ambulancias_id_seq; Type: SEQUENCE SET; Schema: public; Owner: sosrota_user
--

SELECT pg_catalog.setval('public.ambulancias_id_seq', 1, false);


--
-- Name: atendimentos_id_seq; Type: SEQUENCE SET; Schema: public; Owner: sosrota_user
--

SELECT pg_catalog.setval('public.atendimentos_id_seq', 1, false);


--
-- Name: bairros_id_seq; Type: SEQUENCE SET; Schema: public; Owner: sosrota_user
--

SELECT pg_catalog.setval('public.bairros_id_seq', 1, false);


--
-- Name: bases_id_seq; Type: SEQUENCE SET; Schema: public; Owner: sosrota_user
--

SELECT pg_catalog.setval('public.bases_id_seq', 1, false);


--
-- Name: conexoes_id_seq; Type: SEQUENCE SET; Schema: public; Owner: sosrota_user
--

SELECT pg_catalog.setval('public.conexoes_id_seq', 1, false);


--
-- Name: equipe_profissional_id_seq; Type: SEQUENCE SET; Schema: public; Owner: sosrota_user
--

SELECT pg_catalog.setval('public.equipe_profissional_id_seq', 1, false);


--
-- Name: equipes_id_seq; Type: SEQUENCE SET; Schema: public; Owner: sosrota_user
--

SELECT pg_catalog.setval('public.equipes_id_seq', 1, false);


--
-- Name: ocorrencias_id_seq; Type: SEQUENCE SET; Schema: public; Owner: sosrota_user
--

SELECT pg_catalog.setval('public.ocorrencias_id_seq', 1, false);


--
-- Name: profissionais_id_seq; Type: SEQUENCE SET; Schema: public; Owner: sosrota_user
--

SELECT pg_catalog.setval('public.profissionais_id_seq', 1, false);


--
-- Name: usuarios_id_seq; Type: SEQUENCE SET; Schema: public; Owner: sosrota_user
--

SELECT pg_catalog.setval('public.usuarios_id_seq', 2, true);


--
-- Name: ambulancias ambulancias_pkey; Type: CONSTRAINT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.ambulancias
    ADD CONSTRAINT ambulancias_pkey PRIMARY KEY (id);


--
-- Name: ambulancias ambulancias_placa_key; Type: CONSTRAINT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.ambulancias
    ADD CONSTRAINT ambulancias_placa_key UNIQUE (placa);


--
-- Name: atendimentos atendimentos_pkey; Type: CONSTRAINT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.atendimentos
    ADD CONSTRAINT atendimentos_pkey PRIMARY KEY (id);


--
-- Name: bairros bairros_nome_key; Type: CONSTRAINT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.bairros
    ADD CONSTRAINT bairros_nome_key UNIQUE (nome);


--
-- Name: bairros bairros_pkey; Type: CONSTRAINT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.bairros
    ADD CONSTRAINT bairros_pkey PRIMARY KEY (id);


--
-- Name: bases bases_pkey; Type: CONSTRAINT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.bases
    ADD CONSTRAINT bases_pkey PRIMARY KEY (id);


--
-- Name: conexoes conexoes_pkey; Type: CONSTRAINT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.conexoes
    ADD CONSTRAINT conexoes_pkey PRIMARY KEY (id);


--
-- Name: equipe_profissional equipe_profissional_pkey; Type: CONSTRAINT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.equipe_profissional
    ADD CONSTRAINT equipe_profissional_pkey PRIMARY KEY (id);


--
-- Name: equipes equipes_pkey; Type: CONSTRAINT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.equipes
    ADD CONSTRAINT equipes_pkey PRIMARY KEY (id);


--
-- Name: ocorrencias ocorrencias_pkey; Type: CONSTRAINT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.ocorrencias
    ADD CONSTRAINT ocorrencias_pkey PRIMARY KEY (id);


--
-- Name: profissionais profissionais_pkey; Type: CONSTRAINT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.profissionais
    ADD CONSTRAINT profissionais_pkey PRIMARY KEY (id);


--
-- Name: usuarios usuarios_login_key; Type: CONSTRAINT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_login_key UNIQUE (login);


--
-- Name: usuarios usuarios_pkey; Type: CONSTRAINT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_pkey PRIMARY KEY (id);


--
-- Name: idx_ambulancia_base; Type: INDEX; Schema: public; Owner: sosrota_user
--

CREATE INDEX idx_ambulancia_base ON public.ambulancias USING btree (base_id);


--
-- Name: idx_atendimento_ambulancia; Type: INDEX; Schema: public; Owner: sosrota_user
--

CREATE INDEX idx_atendimento_ambulancia ON public.atendimentos USING btree (ambulancia_id);


--
-- Name: idx_atendimento_ocorrencia; Type: INDEX; Schema: public; Owner: sosrota_user
--

CREATE INDEX idx_atendimento_ocorrencia ON public.atendimentos USING btree (ocorrencia_id);


--
-- Name: idx_base_bairro; Type: INDEX; Schema: public; Owner: sosrota_user
--

CREATE INDEX idx_base_bairro ON public.bases USING btree (bairro_id);


--
-- Name: idx_conexao_destino; Type: INDEX; Schema: public; Owner: sosrota_user
--

CREATE INDEX idx_conexao_destino ON public.conexoes USING btree (bairro_destino_id);


--
-- Name: idx_conexao_origem; Type: INDEX; Schema: public; Owner: sosrota_user
--

CREATE INDEX idx_conexao_origem ON public.conexoes USING btree (bairro_origem_id);


--
-- Name: idx_equipe_ambulancia; Type: INDEX; Schema: public; Owner: sosrota_user
--

CREATE INDEX idx_equipe_ambulancia ON public.equipes USING btree (ambulancia_id);


--
-- Name: idx_equipe_profissional_equipe; Type: INDEX; Schema: public; Owner: sosrota_user
--

CREATE INDEX idx_equipe_profissional_equipe ON public.equipe_profissional USING btree (equipe_id);


--
-- Name: idx_equipe_profissional_profissional; Type: INDEX; Schema: public; Owner: sosrota_user
--

CREATE INDEX idx_equipe_profissional_profissional ON public.equipe_profissional USING btree (profissional_id);


--
-- Name: idx_ocorrencia_bairro; Type: INDEX; Schema: public; Owner: sosrota_user
--

CREATE INDEX idx_ocorrencia_bairro ON public.ocorrencias USING btree (bairro_id);


--
-- Name: idx_ocorrencia_status; Type: INDEX; Schema: public; Owner: sosrota_user
--

CREATE INDEX idx_ocorrencia_status ON public.ocorrencias USING btree (status);


--
-- Name: ambulancias fk_ambulancia_base; Type: FK CONSTRAINT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.ambulancias
    ADD CONSTRAINT fk_ambulancia_base FOREIGN KEY (base_id) REFERENCES public.bases(id);


--
-- Name: atendimentos fk_atendimento_ambulancia; Type: FK CONSTRAINT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.atendimentos
    ADD CONSTRAINT fk_atendimento_ambulancia FOREIGN KEY (ambulancia_id) REFERENCES public.ambulancias(id);


--
-- Name: atendimentos fk_atendimento_ocorrencia; Type: FK CONSTRAINT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.atendimentos
    ADD CONSTRAINT fk_atendimento_ocorrencia FOREIGN KEY (ocorrencia_id) REFERENCES public.ocorrencias(id);


--
-- Name: bases fk_base_bairro; Type: FK CONSTRAINT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.bases
    ADD CONSTRAINT fk_base_bairro FOREIGN KEY (bairro_id) REFERENCES public.bairros(id);


--
-- Name: conexoes fk_conexao_destino; Type: FK CONSTRAINT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.conexoes
    ADD CONSTRAINT fk_conexao_destino FOREIGN KEY (bairro_destino_id) REFERENCES public.bairros(id);


--
-- Name: conexoes fk_conexao_origem; Type: FK CONSTRAINT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.conexoes
    ADD CONSTRAINT fk_conexao_origem FOREIGN KEY (bairro_origem_id) REFERENCES public.bairros(id);


--
-- Name: equipes fk_equipe_ambulancia; Type: FK CONSTRAINT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.equipes
    ADD CONSTRAINT fk_equipe_ambulancia FOREIGN KEY (ambulancia_id) REFERENCES public.ambulancias(id);


--
-- Name: equipe_profissional fk_equipe_profissional_equipe; Type: FK CONSTRAINT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.equipe_profissional
    ADD CONSTRAINT fk_equipe_profissional_equipe FOREIGN KEY (equipe_id) REFERENCES public.equipes(id);


--
-- Name: equipe_profissional fk_equipe_profissional_profissional; Type: FK CONSTRAINT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.equipe_profissional
    ADD CONSTRAINT fk_equipe_profissional_profissional FOREIGN KEY (profissional_id) REFERENCES public.profissionais(id);


--
-- Name: ocorrencias fk_ocorrencia_bairro; Type: FK CONSTRAINT; Schema: public; Owner: sosrota_user
--

ALTER TABLE ONLY public.ocorrencias
    ADD CONSTRAINT fk_ocorrencia_bairro FOREIGN KEY (bairro_id) REFERENCES public.bairros(id);


--
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: pg_database_owner
--

GRANT ALL ON SCHEMA public TO sosrota_user;


--
-- PostgreSQL database dump complete
--

\unrestrict RXPO6YTGjh23pZgCJ4Kc265Wn7K0q4Aefpq6gAtqZF0AaTTWX3srvTg5jD9kYuL

