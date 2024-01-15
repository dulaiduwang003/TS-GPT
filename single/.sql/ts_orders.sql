create table ts_orders
(
    orders_id    varchar(100)                                                 not null
        primary key,
    user_id      bigint                                                       not null,
    product_name varchar(100)                                                 not null,
    price        double                                                       not null,
    days         bigint                                                       not null,
    status       enum ('SUCCEED', 'WAIT', 'CANCEL') default 'WAIT'            not null,
    created_time datetime                           default CURRENT_TIMESTAMP not null,
    update_time  datetime                           default CURRENT_TIMESTAMP not null
)
    charset = utf8mb4;

create index ts_orders_status_index
    on ts_orders (status);

