-- liquibase formatted sql

-- changeset gusainovt:005-normalize-common-user-roles
update admin_users
set role = 'USER_FREE'
where role = 'USER';
