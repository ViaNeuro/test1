# Внутренний портал заявок предприятия

Полноценный монорепозиторий: backend на Java 17/Spring Boot, PostgreSQL с Flyway-миграциями и frontend на React/Vite. Система рассчитана на запуск внутри локальной сети предприятия.

## Архитектура

- **Backend**: Spring Boot REST API, Spring Security Basic Auth, роли `USER`, `IT_SPECIALIST`, `ADMIN`.
- **Database**: PostgreSQL, схема создаётся Flyway-миграцией `V1__init.sql`.
- **Frontend**: React SPA для создания заявок пользователями и отдельной IT-панели.
- **Тесты**: backend интеграционные тесты через MockMvc + H2 в режиме PostgreSQL; frontend smoke-test через Vitest.

## Учётные записи для демо

| Логин | Пароль | Роль |
|---|---|---|
| `user` | `password` | USER |
| `it` | `password` | IT_SPECIALIST |
| `admin` | `password` | ADMIN |

## Запуск

```bash
docker compose up -d postgres
./mvnw spring-boot:run
cd frontend && npm install && npm run dev
```

Если Maven Wrapper недоступен, используйте установленный Maven:

```bash
mvn spring-boot:run
```

Frontend по умолчанию работает на `http://localhost:5173`, backend — на `http://localhost:8080`.

## Проверки

```bash
mvn test
cd frontend && npm install && npm run build && npm test
```

## REST API

- `GET /api/me` — текущий пользователь.
- `POST /api/tickets` — создать заявку.
- `GET /api/tickets` — список своих заявок.
- `GET /api/tickets/{id}` — просмотр своей заявки.
- `GET /api/it/tickets` — IT-панель: все заявки.
- `PUT /api/it/tickets/{id}` — изменить статус и/или исполнителя.
- `POST /api/it/tickets/{id}/comments` — добавить комментарий.
- `GET /api/it/specialists` — список возможных исполнителей.
