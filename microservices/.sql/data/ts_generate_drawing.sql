create table ts_generate_drawing
(
    generate_drawing_id varchar(50)                                                                   not null
        primary key,
    prompt              text                                                                          not null,
    user_id             bigint                                                                        not null,
    status              enum ('DISUSE', 'PENDING', 'PROCESSING', 'SUCCEED') default 'PENDING'         not null,
    url                 varchar(150)                                                                  null,
    type                enum ('SD', 'DALL')                                 default 'SD'              not null,
    created_time        datetime                                            default CURRENT_TIMESTAMP not null,
    update_time         datetime                                            default CURRENT_TIMESTAMP not null
)
    charset = utf8mb4;

create index ts_generate_drawing_status_index
    on ts_generate_drawing (status);

create index ts_generate_drawing_type_index
    on ts_generate_drawing (type);

create index ts_generate_drawing_user_id_index
    on ts_generate_drawing (user_id);

