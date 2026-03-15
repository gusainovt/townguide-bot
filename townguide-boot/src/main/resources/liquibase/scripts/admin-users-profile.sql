-- liquibase formatted sql

-- changeset codex:003-add-admin-profile-columns
alter table if exists admin_users
    add column if not exists login varchar(255),
    add column if not exists name varchar(255),
    add column if not exists full_name varchar(255);

-- changeset codex:004-fill-default-admin-profile
update admin_users
set login = coalesce(login, username),
    name = coalesce(name, 'Иван'),
    full_name = coalesce(full_name, 'Иван Петров')
where username = 'admin';
