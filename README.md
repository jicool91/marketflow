# MarketFlow

MarketFlow — платформа для обработки биржевых данных и автоматизации торговых стратегий. Проект состоит из backend-а, frontend-а и набора модулей, объединённых общей системой CI/CD.

## Структура репозитория

- **backend** — Spring Boot приложение со всей бизнес-логикой.
- **frontend** — веб-интерфейс на основе React/Vite (подробнее см. `frontend/README.md`).
- **modules** — дополнительные сервисы, например:
  - `collect-metrics`
  - `strategy-engine`
  - `pdf-generator`
  - `bot-sender`
- **infra** — конфигурация nginx и прочая инфраструктура.
- **jenkins_init** — скрипты и Groovy-файл для автоматического создания Jenkins job'ов.

## Сборка

Для сборки всех модулей выполните:

```bash
mvn clean install
```

Фронтенд можно собрать отдельно из каталога `frontend`:

```bash
npm install
npm run build
```

## Запуск через docker-compose

Проект содержит `docker-compose.yml`, который поднимает Jenkins, Nexus, Postgres и nginx. Запуск:

```bash
docker-compose up -d
```

После этого Jenkins будет доступен на `http://localhost:8080`.

## Jenkins pipelines

В репозитории есть несколько Jenkinsfile:

- `Jenkinsfile.main` — собирает все модули и запускает деплой.
- `modules/ci/*` — отдельные пайплайны для backend, frontend и модулей.
- Groovy-скрипт `jenkins_init/jobs.groovy` создаёт необходимые jobs при старте Jenkins.

Эти pipeline'ы автоматически выполняют сборку и тестирование проекта.

