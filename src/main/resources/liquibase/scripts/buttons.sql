-- liquibase formatted sql

-- changeset gusainovt:create-buttons-table
create table buttons (
         language_code varchar(10) not null,
         type varchar(50) not null,
         menu_type varchar(50) not null,
         callback varchar(255) not null,
         text_button varchar(255) not null,
         primary key (language_code, type, menu_type)
);
