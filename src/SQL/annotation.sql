CREATE TABLE public.annotation
(
    id integer NOT NULL DEFAULT nextval('annotation_id_seq'::regclass),
    user_id integer NOT NULL,
    image_id integer NOT NULL,
    date date NOT NULL,
    valid boolean NOT NULL,
    trust gml_enum NOT NULL,
    notes text COLLATE pg_catalog."default",
    CONSTRAINT annotation_pkey PRIMARY KEY (id),
    CONSTRAINT user_image_unique UNIQUE (user_id, image_id),
    CONSTRAINT annotation_image_id_fkey FOREIGN KEY (image_id)
        REFERENCES public.location_image (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT annotation_user_id_fkey FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)