create table ts_product_card
(
    product_card_id int auto_increment
        primary key,
    product_name    varchar(100)                       not null,
    days            int                                not null,
    price           double                             not null,
    created_time    datetime default CURRENT_TIMESTAMP not null,
    update_time     datetime default CURRENT_TIMESTAMP not null
);

