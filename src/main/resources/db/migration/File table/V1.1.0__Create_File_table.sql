create table file (
    id serial primary key,
    filename varchar(32) not null,
    file bytea
);