create table ts_gpt_model
(
    gpt_model_id    int auto_increment
        primary key,
    model_name      varchar(100)                         not null,
    is_senior_model tinyint(1) default 0                 not null,
    top_p           double                               not null,
    max_tokens      int                                  not null,
    temperature     double                               not null,
    created_time    datetime   default CURRENT_TIMESTAMP not null,
    update_time     datetime   default CURRENT_TIMESTAMP not null
);

INSERT INTO ts_bdth.ts_gpt_model (gpt_model_id, model_name, is_senior_model, top_p, max_tokens, temperature, created_time, update_time) VALUES (1, 'gpt-3.5-turbo-16k-0613', 0, 1, 2048, 1, '2024-01-12 10:10:08', '2024-01-12 10:10:08');
