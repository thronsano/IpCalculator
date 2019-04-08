DROP DATABASE ipCalculator;

CREATE DATABASE ipCalculator;

USE ipCalculator;

CREATE TABLE users
(
    email    VARCHAR(100) PRIMARY KEY NOT NULL,
    password VARCHAR(100)             NOT NULL,
    username VARCHAR(50)              NOT NULL
);

create table authorities
(
    email     VARCHAR(100) NOT NULL,
    authority VARCHAR(50)  NOT NULL,
    CONSTRAINT fk_authorities_users FOREIGN KEY (email) REFERENCES users (email) ON DELETE CASCADE
);

-- Sample user's password '$2a$10$9d5AC2CrUGaWSgwRHbtZV.TbKiuixWQh3EzJhZ7tHt0AeifE2AxCq' is a hashed version of password 'password'
INSERT INTO users (email, password, username)
VALUES ('admin@gmail.com', '$2a$10$9d5AC2CrUGaWSgwRHbtZV.TbKiuixWQh3EzJhZ7tHt0AeifE2AxCq', 'Piotr');

INSERT INTO users (email, password, username)
VALUES ('sdoe@gmail.com', '$2a$10$9d5AC2CrUGaWSgwRHbtZV.TbKiuixWQh3EzJhZ7tHt0AeifE2AxCq', 'Piotr');

INSERT INTO authorities (email, authority)
VALUES ('admin@gmail.com', 'ROLE_USER');

INSERT INTO authorities (email, authority)
VALUES ('sdoe@gmail.com', 'ROLE_USER');
