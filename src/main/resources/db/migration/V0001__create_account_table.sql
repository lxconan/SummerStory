create table accounts (
    id serial primary key,
    name varchar(13) not null,
    password varchar(128) not null,
    hardware_id varchar(12) not null,
    created_at timestamp not null default current_timestamp,

    unique (name)
);