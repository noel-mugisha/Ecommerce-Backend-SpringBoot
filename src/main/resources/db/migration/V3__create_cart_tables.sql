create table carts
(
    id           uuid default gen_random_uuid() not null
        constraint carts_pk
            primary key,
    date_created date default current_date      not null
);

create table cart_items
(
    id         bigserial
        constraint cart_items_pk
            primary key,
    cart_id    uuid          not null
        constraint cart_items_carts_id_fk
            references carts (id)
            on delete cascade,
    product_id bigint        not null
        constraint cart_items_products_id_fk
            references products
            on delete cascade,
    quantity   int default 1 not null,
    constraint cart_items_cart_product_unique
        unique (cart_id, product_id)
);

