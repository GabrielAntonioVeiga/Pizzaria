CREATE TABLE cliente (
                         id bigserial NOT NULL,
                         nome varchar(100) NULL,
                         sobrenome varchar(100) NULL,
                         telefone varchar(20) NULL,
                         CONSTRAINT cliente_pkey PRIMARY KEY (id)
);

CREATE TABLE pedido (
                        id bigserial NOT NULL,
                        id_cliente int4 NOT NULL,
                        status varchar(50) NULL,
                        preco_total numeric(10, 2) NULL,
                        CONSTRAINT pedido_pkey PRIMARY KEY (id),
                        CONSTRAINT fk_pedido_cliente FOREIGN KEY (id_cliente) REFERENCES public.cliente(id) ON DELETE CASCADE
);

CREATE TABLE pizza (
                       id bigserial NOT NULL,
                       forma varchar(20) NULL,
                       raio numeric(5, 2) NULL,
                       lado numeric(5, 2) NULL,
                       id_pedido int4 NOT NULL,
                       CONSTRAINT pizza_pkey PRIMARY KEY (id),
                       CONSTRAINT fk_pizza_pedido FOREIGN KEY (id_pedido) REFERENCES public.pedido(id) ON DELETE CASCADE
);

CREATE TABLE pizza_sabor (
                        id_pizza int4 NOT NULL,
                        id_sabor int4 NOT NULL,
                        CONSTRAINT pizza_sabor_pkey PRIMARY KEY (id_pizza, id_sabor),
                        CONSTRAINT fk_pizza_sabor_pizza FOREIGN KEY (id_pizza) REFERENCES public.pizza(id) ON DELETE CASCADE,
                        CONSTRAINT fk_pizza_sabor_sabor FOREIGN KEY (id_sabor) REFERENCES public.sabor(id)
);

CREATE TABLE sabor (
                       id bigserial NOT NULL,
                       nome varchar(100) NULL,
                       tipo varchar(50) NULL,
                       preco_cm2 numeric(10, 4) NULL,
                       CONSTRAINT sabor_pkey PRIMARY KEY (id)
);

create table tipo_sabor (
                        tipo VARCHAR(50) not null unique,
                        preco_cm2 numeric(10,4)
)

    insert into tipo_sabor values
('SIMPLES', 0.19),
('ESPECIAL', 0.25),
('PREMIUM', 0.30)

insert into sabor(nome, tipo, preco_cm2) values
('Pepperoni', 'SIMPLES', 0.19),
('Calabresa', 'ESPECIAL', 0.25),
('Portuguesa', 'PREMIUM', 0.30)

