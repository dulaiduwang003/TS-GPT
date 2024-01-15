create table ts_user
(
    user_id         int auto_increment
        primary key,
    open_id         varchar(150)                                     null,
    is_sign_in      int                    default 0                 not null,
    avatar          varchar(255)                                     null,
    email           varchar(50)                                      null,
    type            enum ('USER', 'ADMIN') default 'USER'            not null,
    created_time    datetime               default CURRENT_TIMESTAMP not null,
    expiration_time datetime                                         null,
    update_time     datetime               default CURRENT_TIMESTAMP not null,
    nick_name       varchar(100)                                     null
)
    charset = utf8mb4;

create index ts_user_email_index
    on ts_user (email);

create index ts_user_open_id_index
    on ts_user (open_id);

