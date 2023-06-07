--liquibase formatted sql

--changeset gracjan.pasik:1 labels:example-label context:example-context
--comment: Add created_at date for comments
ALTER TABLE IF EXISTS public.comment
    ADD COLUMN created_at date;
--rollback ALTER TABLE IF EXISTS public.comment DROP COLUMN IF EXISTS created_at;

--changeset gracjan.pasik:2 labels:example-label context:example-context
--comment: Increase text column size
ALTER TABLE IF EXISTS public.comment
    ALTER COLUMN text TYPE VARCHAR(10000);
--rollback ALTER TABLE IF EXISTS public.comment DROP COLUMN IF EXISTS created_at;
