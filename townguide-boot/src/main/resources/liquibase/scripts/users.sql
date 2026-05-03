-- liquibase formatted sql

-- changeset gusainovt:1
create table if not exists users
(
    id             bigserial primary key,
    chat_id        bigint unique,
    login          varchar(255) unique,
    name           varchar(255),
    full_name      varchar(255),
    password_hash  varchar(255),
    role           varchar(50),
    first_name     varchar(255),
    last_name      varchar(255),
    user_name      varchar(255),
    registered_at  timestamp,
    embedded_joke  boolean,
    phone_number   varchar(50),
    latitude       double precision,
    longitude      double precision,
    bio            text,
    description    text,
    pinned_message text
);

-- changeset gusainovt:add-column-language-code
alter table if exists users
    add column if not exists language_code varchar(50);

-- changeset gusainovt:add-common-auth-columns-to-users
alter table if exists users
    add column if not exists id bigserial,
    add column if not exists login varchar(255),
    add column if not exists name varchar(255),
    add column if not exists full_name varchar(255),
    add column if not exists password_hash varchar(255),
    add column if not exists role varchar(50);

create unique index if not exists users_login_uq on users (login) where login is not null;
create unique index if not exists users_chat_id_uq on users (chat_id) where chat_id is not null;

-- changeset gusainovt:insert-default-admin-user
insert into users (login, name, full_name, password_hash, role)
values (
       'admin',
       'РРІР°РЅ',
       'РРІР°РЅ РџРµС‚СЂРѕРІ',
       '$2b$10$L5ckv09GxZRZLV/QVTiOL/QheASB56v9A87EqNbqyxi03lFVNhv1D',
       'ADMIN'
       )
on conflict (login) do nothing;
