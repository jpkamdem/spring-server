set time zone 'utc';
create extension if not exists "uuid-ossp";

drop table if exists users;
drop type if exists role;

create type role as enum ('superadmin', 'admin', 'user');

create table if not exists users (
  id uuid unique not null default uuid_generate_v4(),
  firstname varchar(55) not null,
  lastname varchar(55) not null,
  email varchar(255) unique not null,
  password varchar(255) not null,
  age integer not null,
  role role not null,
  phone_number varchar(10) unique not null,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);