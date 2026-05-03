-- liquibase formatted sql

-- changeset gusainovt:001-add-audit-columns-to-main-tables
alter table if exists ads
    add column if not exists created_at timestamp not null default current_timestamp,
    add column if not exists updated_at timestamp not null default current_timestamp;

alter table if exists cities
    add column if not exists created_at timestamp not null default current_timestamp,
    add column if not exists updated_at timestamp not null default current_timestamp;

alter table if exists photos
    add column if not exists created_at timestamp not null default current_timestamp,
    add column if not exists updated_at timestamp not null default current_timestamp;

alter table if exists places
    add column if not exists created_at timestamp not null default current_timestamp,
    add column if not exists updated_at timestamp not null default current_timestamp;

alter table if exists stories
    add column if not exists created_at timestamp not null default current_timestamp,
    add column if not exists updated_at timestamp not null default current_timestamp;

alter table if exists users
    add column if not exists created_at timestamp not null default current_timestamp,
    add column if not exists updated_at timestamp not null default current_timestamp;
