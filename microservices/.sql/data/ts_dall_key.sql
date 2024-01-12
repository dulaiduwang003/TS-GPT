create table ts_dall_key
(
    dall_key_id  int auto_increment
        primary key,
    open_ai_key  varchar(100) not null,
    created_time datetime     not null,
    update_time  datetime     not null
);

create index ts_dall_key_open_ai_key_index
    on ts_dall_key (open_ai_key);

