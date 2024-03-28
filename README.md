# Learning Management System

A simple learning management system.

## Build & Run

### Build the Botalka Service

```bash
gradle :botalka:build
```

### Start infrastructure

```bash
docker compose down
docker compose up --build --force-recreate
```

### Connect to LMS Database

```bash
docker exec -it lms-database bash
psql -h localhost -p 5432 -d $POSTGRES_DB -U $POSTGRES_USER
```
