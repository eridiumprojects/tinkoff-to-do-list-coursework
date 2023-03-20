create table public.users
(
    id         bigserial
        primary key,
    email      varchar(255)
        constraint uk6dotkott2kjsp8vw4d0m25fb7
            unique,
    first_name varchar(255),
    last_name  varchar(255),
    password   varchar(255),
    username   varchar(255)
        constraint ukr43af9ap4edm43mmtq01oddj6
            unique
);

create table public.device
(
    id           bigserial
        primary key,
    created      timestamp(6) with time zone,
    device_token uuid,
    last_login   timestamp(6) with time zone,
    user_id      bigint
        constraint fkrslsigi6stt67elpy0ury5g29
            references public.users
);

create table public.roles
(
    id   serial
        primary key,
    name varchar(20)
);

create table public.tasks
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
            references public.users
);

create table public.refreshtoken
(
    id        bigserial       not null
        primary key,
    token     varchar(255) not null
        constraint uk_or156wbneyk8noo4jstv55ii3
            unique,
    device_id bigint
        constraint fk34fnpyfu8kncujt018v6r9rr
            references public.device,
    user_id   bigint
        constraint fka652xrdji49m4isx38pp4p80p
            references public.users
);

create table public.user_roles
(
    user_id bigint  not null
        constraint fkhfh9dx7w3ubf1co1vdev94g3f
            references public.users,
    role_id integer not null
        constraint fkh8ciramu9cc9q3qcqiv4ue8a6
            references public.roles,
    primary key (user_id, role_id)
);

alter table public.device
    owner to postgres;
alter table public.users
    owner to postgres;
alter table public.roles
    owner to postgres;
alter table public.tasks
    owner to postgres;
alter table public.refreshtoken
    owner to postgres;
alter table public.user_roles
    owner to postgres;