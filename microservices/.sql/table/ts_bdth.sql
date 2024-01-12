create table if not exists ts_bdth.ts_dall_key
(
    dall_key_id  int auto_increment
        primary key,
    open_ai_key  varchar(100) not null,
    created_time datetime     not null,
    update_time  datetime     not null
);

create index ts_dall_key_open_ai_key_index
    on ts_bdth.ts_dall_key (open_ai_key);

create table if not exists ts_bdth.ts_dialogue_drawing
(
    dialogue_drawing_id int auto_increment
        primary key,
    url                 varchar(255)                       not null,
    created_time        datetime default CURRENT_TIMESTAMP not null,
    update_time         datetime default CURRENT_TIMESTAMP not null
)
    charset = utf8mb4;

create table if not exists ts_bdth.ts_drawing_prompt
(
    drawing_prompt_id bigint auto_increment
        primary key,
    prompt            text                               not null,
    type              enum ('SD', 'DALL')                not null,
    created_time      datetime default CURRENT_TIMESTAMP not null,
    update_time       datetime default CURRENT_TIMESTAMP not null
)
    charset = utf8mb4;

create table if not exists ts_bdth.ts_exception
(
    exception_id int auto_increment
        primary key,
    server_name  enum ('CHAT', 'AUTH', 'DRAWING')                           not null,
    level        enum ('LOW', 'MIDDLE', 'HEIGHT') default 'LOW'             not null,
    cause        text                                                       not null,
    created_time datetime                         default CURRENT_TIMESTAMP not null,
    update_time  datetime                         default CURRENT_TIMESTAMP not null
);

create table if not exists ts_bdth.ts_generate_drawing
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
    on ts_bdth.ts_generate_drawing (status);

create index ts_generate_drawing_type_index
    on ts_bdth.ts_generate_drawing (type);

create index ts_generate_drawing_user_id_index
    on ts_bdth.ts_generate_drawing (user_id);

create table if not exists ts_bdth.ts_gpt_key
(
    gpt_key_id      int auto_increment
        primary key,
    open_ai_key     varchar(150)         not null,
    is_senior_model tinyint(1) default 0 not null,
    created_time    datetime             not null,
    update_time     datetime             not null
);

create index ts_gpt_key_is_senior_model_index
    on ts_bdth.ts_gpt_key (is_senior_model);

create index ts_gpt_key_open_ai_key_index
    on ts_bdth.ts_gpt_key (open_ai_key);

create table if not exists ts_bdth.ts_gpt_model
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

create table if not exists ts_bdth.ts_micro_app
(
    micro_app_id      bigint auto_increment
        primary key,
    micro_category_id bigint                             not null,
    icon              varchar(100)                       not null,
    title             varchar(100)                       not null,
    introduce         varchar(100)                       not null,
    chinese_issue     text                               not null,
    english_issue     text                               not null,
    chinese_answer    text                               not null,
    english_answer    text                               not null,
    created_time      datetime default CURRENT_TIMESTAMP not null,
    update_time       datetime default CURRENT_TIMESTAMP not null
)
    charset = utf8mb4;

create index ts_micro_app_micro_category_id_index
    on ts_bdth.ts_micro_app (micro_category_id);

create index ts_micro_app_title_index
    on ts_bdth.ts_micro_app (title);

create table if not exists ts_bdth.ts_micro_category
(
    micro_category_id bigint auto_increment
        primary key,
    el_icon           varchar(50)                        not null,
    category_name     varchar(100)                       not null,
    created_time      datetime default CURRENT_TIMESTAMP not null,
    update_time       datetime default CURRENT_TIMESTAMP not null
)
    charset = utf8mb4;

create table if not exists ts_bdth.ts_orders
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
    on ts_bdth.ts_orders (status);

create table if not exists ts_bdth.ts_product_card
(
    product_card_id int auto_increment
        primary key,
    product_name    varchar(100)                       not null,
    days            int                                not null,
    price           double                             not null,
    created_time    datetime default CURRENT_TIMESTAMP not null,
    update_time     datetime default CURRENT_TIMESTAMP not null
);

create table if not exists ts_bdth.ts_redeem_code
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
    on ts_bdth.ts_redeem_code (redeem_code);

create table if not exists ts_bdth.ts_user
(
    user_id         int auto_increment
        primary key,
    open_id         varchar(150)                                     null,
    is_sign_in      int                    default 0                 not null,
    avatar          varchar(255)                                     null,
    email           varchar(50)                                      null,
    type            enum ('USER', 'ADMIN') default 'USER'            not null,
    created_time    datetime               default CURRENT_TIMESTAMP not null,
    expiration_time datetime                                         null,
    update_time     datetime               default CURRENT_TIMESTAMP not null,
    nick_name       varchar(100)                                     null
)
    charset = utf8mb4;

create index ts_user_email_index
    on ts_bdth.ts_user (email);

create index ts_user_open_id_index
    on ts_bdth.ts_user (open_id);

