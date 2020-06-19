CREATE TABLE public.worker
(
    user_id integer NOT NULL,
    experience gml_enum NOT NULL,
    photo text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT worker_pkey PRIMARY KEY (user_id),
    CONSTRAINT worker_user_id_fkey FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)