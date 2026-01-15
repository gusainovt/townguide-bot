-- liquibase formatted sql

-- changeset gusainovt:001-create-admin-users
CREATE TABLE admin_users (
         id BIGSERIAL PRIMARY KEY,
         username VARCHAR(255) NOT NULL UNIQUE,
         password_hash VARCHAR(255) NOT NULL,
         role VARCHAR(50) NOT NULL
);

-- changeset gusainovt:002-insert-default-admin
INSERT INTO admin_users (username, password_hash, role)
VALUES (
       'admin',
       '$2b$10$L5ckv09GxZRZLV/QVTiOL/QheASB56v9A87EqNbqyxi03lFVNhv1D',
       'ADMIN'
       );