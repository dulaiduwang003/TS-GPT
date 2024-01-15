create table ts_dialogue_drawing
(
    dialogue_drawing_id int auto_increment
        primary key,
    url                 varchar(255)                       not null,
    created_time        datetime default CURRENT_TIMESTAMP not null,
    update_time         datetime default CURRENT_TIMESTAMP not null
)
    charset = utf8mb4;

