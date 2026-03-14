-- liquibase formatted sql

-- changeset gusainovt:1
create table stories
(
    id       serial primary key,
    body     varchar(2550000)
);