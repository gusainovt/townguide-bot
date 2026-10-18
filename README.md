# TownGuide Telegram Bot
Телеграм-бот, который рассказывает исторические факты о городах, показывает фотографии достопримечательностей и отображает текущую погоду.  
[![Ссылка на бота](https://img.shields.io/badge/Telegram-@Borovsk__bot-blue?logo=telegram)](https://t.me/Borovsk_bot)

## Содержание

- [Админ-панель](#админ-панель)
- [Андроид-прилжение](#андроид-прилжение)
- [Технологический стек](#технологический-стек)
- [Запуск приложения через Docker](#запуск-приложения-через-docker)
  - [Требования](#требования)
  - [Запуск](#запуск)
- [Тесты](#тесты)
  - [Интеграционные тесты (Testcontainers + WireMock)](#интеграционные-тесты-testcontainers--wiremock)

# Админ-панель

Android приложение для управления контентом бота  
[![Admin App](https://img.shields.io/badge/GitHub-Admin_App-black?logo=github)](https://github.com/gusainovt/townguide-android)

# Андроид-прилжение
Android приложение для пользвателей  
[![Android App](https://img.shields.io/badge/GitHub-Android_App-black?logo=github)](https://github.com/gusainovt/townguide)

# Технологический стек

| Java 17 | основной язык разработки |
|-----------|-------------|
| Spring Boot | backend framework |
| PostgreSQL | база данных |
| Cloudinary | хранение изображений |
| Railway | деплой |

# Запуск приложения через Docker

## Требования

Перед запуском должны быть установлены:

- **Docker**
- **Docker Compose**
- **Git**

Проверить установку можно командами:

```bash
docker -v
docker compose -v
git -v
```

## Запуск

1. Клонировать репозиторий
`git clone https://github.com/gusainovt/townguide-bot.git`
2. Перейти в директорию проекта
`cd townguide-bot`
3. Запустить контейнеры
`docker compose up --build`

Docker поднимет два контейнера (приложение и БД) 

# Тесты

## Интеграционные тесты (Testcontainers + WireMock)

- Запуск: `mvn test -Dtest=*IT`
- Нужен запущенный Docker (Testcontainers поднимет PostgreSQL и WireMock в контейнерах).
