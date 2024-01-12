create table ts_redeem_code
(
    redeem_code_id bigint auto_increment
        primary key,
    redeem_code    varchar(30)                        not null,
    currency       bigint                             not null,
    created_time   datetime default CURRENT_TIMESTAMP not null,
    update_time    datetime default CURRENT_TIMESTAMP not null
)
    charset = utf8mb4;

create index ts_redeem_code_redeem_code_index
    on ts_redeem_code (redeem_code);

