-- liquibase formatted sql

-- changeset gusainovt:1
create table photos
(
    id        serial primary key,
    file_path varchar(255) not null,
    file_size bigint       not null,
    media_type varchar(50),
    data      oid,
    place_id  bigint
);

-- -- changeset gusainovt:2
-- alter table photos
--     add constraint fk_place
--         foreign key (place_id) references places (id) on delete cascade;