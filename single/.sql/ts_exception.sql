create table ts_exception
(
    exception_id int auto_increment
        primary key,
    server_name  enum ('CHAT', 'AUTH', 'DRAWING')                           not null,
    level        enum ('LOW', 'MIDDLE', 'HEIGHT') default 'LOW'             not null,
    cause        text                                                       not null,
    created_time datetime                         default CURRENT_TIMESTAMP not null,
    update_time  datetime                         default CURRENT_TIMESTAMP not null
);

