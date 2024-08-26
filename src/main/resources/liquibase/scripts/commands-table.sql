-- liquibase formatted sql

-- changeset gusainovt:001-create-commands-table
CREATE TABLE commands (
      id BIGSERIAL PRIMARY KEY,
      command_type VARCHAR(50) NOT NULL,
      description VARCHAR(255) NOT NULL
);

-- changeset gusainovt:002-insert-default-values
INSERT INTO commands (command_type, description) VALUES
     ('START', 'запустить бота'),
     ('MY_DATA', 'посмотреть свои данные'),
     ('DELETE_DATA', 'удалить свои данные'),
     ('HELP', 'информация как пользоваться этим ботом'),
     ('SETTING', 'настройки'),
     ('REGISTER', 'регистрация'),
     ('JOKE', 'рандомная шутка'),
     ('WEATHER', 'погода в Боровске');