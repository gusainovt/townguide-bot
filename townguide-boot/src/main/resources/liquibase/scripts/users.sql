-- liquibase formatted sql

-- changeset gusainovt:1
create table users
(
    chat_id        bigint primary key,
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
alter table users
    add column language_code varchar(50);
