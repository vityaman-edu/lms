CREATE SCHEMA lms;

CREATE TABLE lms.note (
    id      bigserial PRIMARY KEY,
    content text      NOT NULL
);
