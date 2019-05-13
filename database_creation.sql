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

CREATE TABLE network
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    addressesAmount VARCHAR(255) NOT NULL,
    networkIp       VARCHAR(255) NOT NULL,
    broadcastIp     VARCHAR(255) NOT NULL,
    routerIp        VARCHAR(255) NOT NULL,
    lastAvailableIp VARCHAR(255) NOT NULL,
    subnetMask      VARCHAR(255) NOT NULL,
    name            VARCHAR(255) NOT NULL
);

CREATE TABLE users_network
(
    user_email  VARCHAR(100),
    networks_id int unique,
    CONSTRAINT pk_users_network PRIMARY KEY (user_email, networks_id),
    CONSTRAINT fk_users_network_employees FOREIGN KEY (user_email) REFERENCES users (email) ON DELETE CASCADE,
    CONSTRAINT fk_users_network_network FOREIGN KEY (networks_id) REFERENCES network (id) ON DELETE CASCADE
);

-- Sample user's password '$2a$10$9d5AC2CrUGaWSgwRHbtZV.TbKiuixWQh3EzJhZ7tHt0AeifE2AxCq' is a hashed version of password 'password'
INSERT INTO users (email, password, username)
VALUES ('sdoe@gmail.com', '$2a$10$9d5AC2CrUGaWSgwRHbtZV.TbKiuixWQh3EzJhZ7tHt0AeifE2AxCq', 'Piotr');

INSERT INTO authorities (email, authority)
VALUES ('sdoe@gmail.com', 'ROLE_USER');
