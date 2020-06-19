CREATE TABLE public.campaign
(
    id integer NOT NULL DEFAULT nextval('campaign_id_seq'::regclass),
    manager_id integer NOT NULL,
    name text COLLATE pg_catalog."default" NOT NULL,
    client text COLLATE pg_catalog."default" NOT NULL,
    status campaign_status NOT NULL DEFAULT 'CREATED'::campaign_status,
    CONSTRAINT campaign_pkey PRIMARY KEY (id),
    CONSTRAINT campaign_name_key UNIQUE (name),
    CONSTRAINT campaign_manager_id_fkey FOREIGN KEY (manager_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)