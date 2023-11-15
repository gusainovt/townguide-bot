-- liquibase formatted sql

-- changeset gusainovt:1
create table ads
(
    id serial primary key,
    ad text not null
);