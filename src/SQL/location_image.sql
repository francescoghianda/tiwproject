CREATE TABLE public.location_image
(
    id integer NOT NULL DEFAULT nextval('campaign_image_id_seq'::regclass),
    campaign_id integer NOT NULL,
    location geography NOT NULL,
    municipality text COLLATE pg_catalog."default" NOT NULL,
    region text COLLATE pg_catalog."default" NOT NULL,
    source text COLLATE pg_catalog."default" NOT NULL DEFAULT 'Unknown'::text,
    date date NOT NULL,
    resolution gml_enum NOT NULL,
    image text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT campaign_image_pkey PRIMARY KEY (id),
    CONSTRAINT campaign_image_campaign_id_fkey FOREIGN KEY (campaign_id)
        REFERENCES public.campaign (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)