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

INSERT INTO ts_bdth.ts_generate_drawing (generate_drawing_id, prompt, user_id, status, url, type, created_time, update_time) VALUES ('683d3ba6-3980-4c8a-b5a4-979d776e3237', 'pig', 1, 'SUCCEED', '/drawing/424a29f0-2222-4119-a7a1-cb032dd26489.jpg', 'SD', '2024-01-15 15:49:05', '2024-01-15 15:52:36');
