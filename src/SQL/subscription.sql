CREATE TABLE public.subscription
(
    id integer NOT NULL DEFAULT nextval('subscription_id_seq'::regclass),
    user_id integer NOT NULL,
    campaign_id integer NOT NULL,
    CONSTRAINT subscription_pkey PRIMARY KEY (id),
    CONSTRAINT subscription_user_id_campaign_id_key UNIQUE (user_id, campaign_id),
    CONSTRAINT subscription_campaign_id_fkey FOREIGN KEY (campaign_id)
        REFERENCES public.campaign (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT subscription_user_id_fkey FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)