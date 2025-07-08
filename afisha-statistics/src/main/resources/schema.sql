DROP TABLE IF EXISTS visits;

CREATE TABLE IF NOT EXISTS visits (
    id serial primary key,
    app varchar(255) not null,
    uri varchar(255) not null,
    ip varchar(100) not null,
    timestamp timestamp without time zone not null
);