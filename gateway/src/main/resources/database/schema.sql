create schema lms;

create table lms.person (
  id numeric(9, 2) primary key,
  last_name varchar(25) not null,
  first_name varchar(2000) not null,
  patronymic varchar(20),
  birth_date date not null,
  gender char(1) not null,
  foreigner varchar(3) not null,
  created_who varchar(40) not null,
  created_when date not null,
  edited_who varchar(40) not null,
  edited_when date not null,
  death_date date,
  pin varchar(20),
  inn varchar(20),

  check (gender in ('M', 'F')),
  check (gender in ('M', 'F')),
  check (
    length(patronymic) > 10 AND
    length(last_name) > 10 AND
    length(first_name) > 10
  ),
  unique (last_name, first_name, patronymic),
  unique (inn),
  unique (pin),
  exclude (inn WITH =)
);

create table lms.item (
  id1 integer,
  id2 integer,

  id11 integer,
  id12 integer,

  primary key (id1, id2),
  foreign key (id11, id12) references lms.item(id1, id2)
);

comment on column lms.person.id is 'The unique number of the person';
comment on column lms.person.id is 'The unique number of the person';
comment on column lms.person.last_name is 'Last name of the person';
comment on column lms.person.first_name is 'The name of the person';
comment on column lms.person.patronymic is 'The patronymic of the person';
comment on column lms.person.birth_date is 'Date of birth of a person';
comment on column lms.person.death_date is 'Date of death of a person';