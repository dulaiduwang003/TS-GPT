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

INSERT INTO ts_bdth.ts_gpt_key (gpt_key_id, open_ai_key, is_senior_model, created_time, update_time) VALUES (1, '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', 0, '2024-01-12 11:37:31', '2024-01-15 15:45:39');
