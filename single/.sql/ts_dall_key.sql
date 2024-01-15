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

INSERT INTO ts_bdth.ts_dall_key (dall_key_id, open_ai_key, created_time, update_time) VALUES (1, 'sk-kZqbOwgefql76eJ8FcAc260d0a9d4dA3919c58A555917c62', '2024-01-12 11:38:00', '2024-01-12 11:38:00');
