# MarketFlow

MarketFlow — мульти‑тенантная платформа маркетинговой аналитики в формате Telegram Mini App.
Пользователи подключают рекламные источники, собирают события и получают
воронки, атрибуцию, KPI (CTR, CPC, CPA, ROAS) и PDF‑отчёты с доставкой в Telegram.

## Структура репозитория
- **backend** — Spring Boot сервис с REST API, авторизацией через Telegram и
  изоляцией данных по арендаторам.
- **frontend** — SPA на React/Vite, запускаемая как Telegram WebApp.
- **modules** — вспомогательные сервисы (заготовки под коннекторы, генератор отчётов и т.д.).
- **infra** — конфигурация nginx и окружения.
- **docs/adr** — архитектурные решения.

## Сборка и запуск
Используйте `Makefile` для типичных операций:

```bash
make build      # сборка backend и модулей
make frontend   # сборка фронтенда
make up         # поднять docker-compose окружение
```

Docker Compose поднимает Postgres, Jenkins, Nexus и nginx с фронтендом.

## Документация
- Архитектурные решения: `docs/adr`
- Сводка миграций: `MIGRATION.md`
