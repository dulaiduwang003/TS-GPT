create table ts_drawing_prompt
(
    drawing_prompt_id bigint auto_increment
        primary key,
    prompt            text                               not null,
    type              enum ('SD', 'DALL')                not null,
    created_time      datetime default CURRENT_TIMESTAMP not null,
    update_time       datetime default CURRENT_TIMESTAMP not null
)
    charset = utf8mb4;

