--liquibase formatted sql

--changeset gracjan.pasik:1 labels:example-label context:example-context
--comment: Add created_at date for comments
ALTER TABLE IF EXISTS public.comment
    ADD COLUMN created_at date;
--rollback ALTER TABLE IF EXISTS public.comment DROP COLUMN IF EXISTS created_at;
