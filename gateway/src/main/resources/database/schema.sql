CREATE SCHEMA lms;

CREATE TABLE lms.note (
    id      serial  PRIMARY KEY,
    content text    NOT NULL
);
