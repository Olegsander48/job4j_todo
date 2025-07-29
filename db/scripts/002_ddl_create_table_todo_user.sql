CREATE TABLE todo_user (
    id       SERIAL PRIMARY KEY,
    name     TEXT,
    login    TEXT UNIQUE,
    password TEXT
);