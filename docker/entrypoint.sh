#!/bin/sh
set -e

DB_HOST="${DB_HOST:-postgres}"
DB_PORT="${DB_PORT:-5432}"
DB_USER="${DB_USER:-fitness}"

echo "Waiting for PostgreSQL at ${DB_HOST}:${DB_PORT}..."

until pg_isready -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER"
do
    sleep 1
done

echo "PostgreSQL is ready."

exec ./mvnw spring-boot:run
