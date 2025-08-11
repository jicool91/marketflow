.PHONY: build frontend up down

build:
	mvn clean install

frontend:
	cd frontend && npm install && npm run build

up:
	docker-compose up -d

down:
	docker-compose down
