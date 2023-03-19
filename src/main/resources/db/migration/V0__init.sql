create table if not exists roles
(
    id   serial
    primary key,
    name varchar(20)
    );

create table if not exists users
(
    id       bigserial
    primary key,
    email    varchar(255)
    constraint uk6dotkott2kjsp8vw4d0m25fb7
    unique,
    password varchar(255),
    username varchar(255)
    constraint ukr43af9ap4edm43mmtq01oddj6
    unique
    );

create table if not exists refreshtoken
(
    id          bigint                      not null
    primary key,
    expiry_date timestamp(6) with time zone not null,
                                 token       varchar(255)                not null
    constraint uk_or156wbneyk8noo4jstv55ii3
    unique,
    user_id     bigint
    constraint fka652xrdji49m4isx38pp4p80p
    references users
    );

create table if not exists tasks
(
    id       bigserial
    primary key,
    checkbox boolean,
    created  timestamp(6),
    data     varchar(255),
    deadline timestamp(6),
    modified timestamp(6),
    user_id  bigint
    constraint fk6s1ob9k4ihi75xbxe2w0ylsdh
    references users
    );


alter table users
    owner to postgres;
alter table tasks
    owner to postgres;
alter table refreshtoken
    owner to postgres;
alter table roles
    owner to postgres;