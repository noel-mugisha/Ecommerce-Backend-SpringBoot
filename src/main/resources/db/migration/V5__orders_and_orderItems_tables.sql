create table orders
(
    id          bigint generated always as identity
        constraint orders_pk
            primary key,
    status      varchar(20) default 'PENDING'         not null,
    created_at  TIMESTAMP   default CURRENT_TIMESTAMP not null,
    total_price numeric(10, 2)                        not null,
    customer_id bigint                                not null
        constraint orders_users_id_fk
            references users
);

create table order_items
(
    id          bigint generated always as identity
        constraint order_items_pk
            primary key,
    unit_price  numeric(10, 2) not null,
    quantity    integer        not null,
    total_price numeric(10, 2) not null,
    order_id    bigint         not null
        constraint order_items_orders_id_fk
            references orders (id)
            on delete cascade,
    product_id  bigint         not null
        constraint order_items_products_id_fk
            references products
);

