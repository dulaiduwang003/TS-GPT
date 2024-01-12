create table ts_gpt_key
(
    gpt_key_id      int auto_increment
        primary key,
    open_ai_key     varchar(150)         not null,
    is_senior_model tinyint(1) default 0 not null,
    created_time    datetime             not null,
    update_time     datetime             not null
);

create index ts_gpt_key_is_senior_model_index
    on ts_gpt_key (is_senior_model);

create index ts_gpt_key_open_ai_key_index
    on ts_gpt_key (open_ai_key);

