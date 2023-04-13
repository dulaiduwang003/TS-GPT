create table if not exists super_bot.exchange_code
(
    exchange_code_id int auto_increment
        primary key,
    code             varchar(100)  null,
    frequency        int           null,
    del              int default 0 null
);

create table if not exists super_bot.mini_program_user
(
    mini_program_user_id int auto_increment
        primary key,
    open_id              varchar(50)       null,
    share                tinyint default 0 null,
    video                tinyint default 10 null,
    frequency            bigint  default 2 null,
    del                  tinyint default 0 null,
    notify               tinyint default 0 not null
);

