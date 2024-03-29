create table users_test
(
    id         bigserial
        primary key,
    email      varchar(255)
        constraint uk6dotkott2kjsp8vw4d0m25fb3
            unique,
    first_name varchar(255),
    last_name  varchar(255),
    password   varchar(255),
    username   varchar(255)
        constraint ukr43af9ap4edm43mmtq01oddj5
            unique
);

alter table users_test
    owner to postgres;

create table task_test
(
    id          bigserial
        primary key,
    created     timestamp(6),
    data        varchar(255),
    deadline    timestamp(6),
    modified    timestamp(6),
    user_id     bigint
        constraint fk6s1ob9k4ihi75xbxe2w0ylsdh
            references users,
    description text,
    status      text
);

alter table task
    owner to postgres;

create table role_test
(
    id   serial
        primary key,
    name varchar(20)
);

alter table role_test
    owner to postgres;

create table user_role_test
(
    user_id bigint  not null
        constraint fkhfh9dx7w3ubf1co1vdev94g7e
            references users_test,
    role_id integer not null
        constraint fkh8ciramu9cc9q3qcqiv4ue81q
            references role_test,
    primary key (user_id, role_id)
);

alter table user_role
    owner to postgres;

create table device_test
(
    id           bigserial
        primary key,
    created      timestamp(6) with time zone,
    device_token uuid,
    last_login   timestamp(6) with time zone,
    user_id      bigint
        constraint fkrslsigi6stt67elpy0ury5g75
            references users_test
);

alter table device_test
    owner to postgres;

create table refresh_token_test
(
    id        bigserial
        primary key,
    token     varchar(255) not null
        constraint uk_or156wbneyk8noo4jstv55ij8
            unique,
    device_id bigint
        constraint fk34fnpyfu8kncujt018v6r9rq
            references device_test,
    user_id   bigint
        constraint fka652xrdji49m4isx38pp4p81u
            references users_test
);

alter table refresh_token_test
    owner to postgres;
