# Описание
Телеграм-бот, который рассказывает истории о выбранном городе, показывает фотографии интересных мест и отображает погоду.  
Ссылка на бота:  
[![Ссылка на бота](https://img.shields.io/badge/Telegram-@Borovsk__bot-blue?logo=telegram)](https://t.me/Borovsk_bot)

# Стек 
- **Java 17** - основной язык разработки
- **Spring Boot 3** - фреймворк
- **PostgreSQL** - база данных
- **Cloudinary** - хостинг изображений
- **Railway** - хостинг и deployment
- **Telegram Bot API** - интеграция с Telegram

# Структура базы данных
```mermaid
erDiagram
	cities {
	    bigint id PK
	    character_varying callback
	    character_varying description
	    character_varying name
	    character_varying name_eng
	    character_varying photo
	}
	
	photos {
	    integer id PK
	    bigint file_size
	    character_varying media_type
	    bigint place_id FK
	    character_varying public_id
	    character_varying url
	}
	
	places {
	    bigint id PK
	    bigint city_id FK
	    character_varying description
	    character_varying name
	}
	
	stories {
	    integer id PK
	    character_varying body
	    bigint city_id FK
	}
	
	users {
	    bigint chat_id PK
	    text bio
	    text description
	    boolean embedded_joke
	    character_varying first_name
	    character_varying language_code
	    double_precision latitude
	    character_varying last_name
	    double_precision longitude
	    character_varying phone_number
	    text pinned_message
	    timestamp_without_time_zone registered_at
	    character_varying user_name
	}
	
	 admin_users {
        bigint id PK
        character_varying username
        character_varying password_hash
        character_varying role
    }
	
	places }o--|| cities : "city_id"
	stories }o--|| cities : "city_id"
	photos }o--|| places : "place_id"
```
