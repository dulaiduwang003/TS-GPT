create table ts_micro_category
(
    micro_category_id bigint auto_increment
        primary key,
    el_icon           varchar(50)                        not null,
    category_name     varchar(100)                       not null,
    created_time      datetime default CURRENT_TIMESTAMP not null,
    update_time       datetime default CURRENT_TIMESTAMP not null
)
    charset = utf8mb4;

INSERT INTO ts_bdth.ts_micro_category (micro_category_id, el_icon, category_name, created_time, update_time) VALUES (1, 'Odometer', '游戏', '2024-01-12 01:08:19', '2024-01-12 01:08:19');
