-- liquibase formatted sql

-- changeset gusainovt:1
create table places
(
    id          bigserial primary key,
    name        varchar(255) not null,
    description varchar(25500)
);