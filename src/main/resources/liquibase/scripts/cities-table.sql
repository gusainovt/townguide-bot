-- liquibase formatted sql

-- changeset gusainovt:001-create-table-cities
create table cities
(
    id    bigserial primary key,
    name  varchar(255) not null,
    photo varchar(255)
);

-- changeset gusainovt:002-create-fk-for-places
alter table places
    add column city_id bigint not null,
    add constraint fk_places_city
        foreign key (city_id) references cities (id) on delete cascade;

-- changeset gusainovt:002-create-fk-for-stories
alter table stories
    add column city_id bigint not null,
    add constraint fk_stories_city
        foreign key (city_id) references cities (id) on delete cascade;