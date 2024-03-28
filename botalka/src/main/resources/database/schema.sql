CREATE SCHEMA lms;

CREATE TABLE lms.note (
    id      bigserial PRIMARY KEY,
    content text      NOT NULL
);

CREATE DOMAIN lms.alias AS VARCHAR(32)
CHECK (VALUE ~ '[a-zA-Z''-]{3,31}');

CREATE TABLE lms.user (
    id              serial      PRIMARY KEY,
    alias           lms.alias   UNIQUE NOT NULL,
    creation_moment timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE lms.teacher (
    user_id     integer     PRIMARY KEY REFERENCES lms.user(id)
);

CREATE TABLE lms.student (
    user_id     integer     PRIMARY KEY REFERENCES lms.user(id)
);

CREATE DOMAIN lms.score AS smallint
CHECK (0 < VALUE AND VALUE <= 2000);

CREATE TABLE lms.homework (
    id                  serial      PRIMARY KEY,
    title               varchar(64) NOT NULL,
    description         text        NOT NULL,
    max_score           lms.score   NOT NULL,
    publication_moment  timestamptz NOT NULL,
    creation_moment     timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE lms.homework_submission (
    id              serial      PRIMARY KEY,
    homework_id     integer     NOT NULL REFERENCES lms.homework(id),
    student_id      integer     NOT NULL REFERENCES lms.student(user_id),
    comment         text        NOT NULL,
    creation_moment timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE lms.homework_feedback (
    id              integer     PRIMARY KEY,
    homework_id     integer     NOT NULL REFERENCES lms.homework(id),
    student_id      integer     NOT NULL REFERENCES lms.student(user_id),
    teacher_id      integer     NOT NULL REFERENCES lms.teacher(user_id),
    comment         text        NOT NULL,
    score           lms.score,  -- NULLABLE
    creation_moment timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP
);
