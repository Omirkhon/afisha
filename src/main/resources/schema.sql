CREATE TABLE IF NOT EXISTS users (
    id serial primary key,
    email varchar(254) not null,
    name varchar(250) not null
);

CREATE TABLE IF NOT EXISTS events (
    id serial primary key,
    title varchar(120) not null,
    description varchar(7000) not null,
    event_date timestamp without time zone not null,
    created_on timestamp without time zone not null,
    published_on timestamp without time zone,
    paid boolean not null,
    request_moderation boolean not null,
    annotation varchar(2000),
    participant_limit int not null,
    state varchar not null,
    initiator_id int references users(id),
    views int,
    confirmed_requests int
);

CREATE TABLE IF NOT EXISTS categories (
    id serial primary key,
    name varchar(50) unique not null ,
    event_id int references events(id)
);

CREATE TABLE IF NOT EXISTS compilations (
    id serial primary key,
    title varchar(50) not null,
    pinned boolean not null,
    event_id int references events(id)
);

CREATE TABLE IF NOT EXISTS locations (
    id serial primary key,
    lon float not null,
    lat float not null,
    event_id int references events(id) not null
);

CREATE TABLE IF NOT EXISTS requests (
    id serial primary key,
    created timestamp without time zone not null,
    event_id int references events(id),
    requester_id int references users(id),
    status varchar not null
);