
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_profile (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255),
    profile_picture BYTEA,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

create table bank_account (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    account_number VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    currency_code VARCHAR(3) NOT NULL,
    sold DECIMAL NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE partner (
     id SERIAL PRIMARY KEY,
     name VARCHAR(255) NOT NULL,
     email VARCHAR(255),
     phone_number VARCHAR(255),
     website VARCHAR(255),
     reference VARCHAR(255),
     profile_picture BYTEA,
     CUI VARCHAR(20) NOT NULL UNIQUE,
     currency_code VARCHAR(3) NOT NULL,
     address VARCHAR(255) NOT NULL,
     city VARCHAR(255) NOT NULL,
     county VARCHAR(255),
     country VARCHAR(255) NOT NULL,
     postal_code VARCHAR(255) NOT NULL,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE category (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    color_code VARCHAR(255) NOT NULL,
    parent_category_id INT,
    FOREIGN KEY (parent_category_id) REFERENCES category(id)
);

create table tax (
    id serial primary key,
    name varchar(255) not null,
    rate float not null
);

CREATE TABLE element (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    acquisition_price FLOAT NOT NULL,
    selling_price FLOAT NOT NULL,
    category_id INT,
    tax_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES category(id),
    FOREIGN KEY (tax_id) REFERENCES tax(id)
);

CREATE TABLE invoice (
    id SERIAL PRIMARY KEY,
    partner_id INT NOT NULL,
    invoice_number VARCHAR(255) NOT NULL UNIQUE ,
    order_number VARCHAR(255) NOT NULL,
    invoice_date DATE NOT NULL,
    due_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (partner_id) REFERENCES partner(id)
);

CREATE UNIQUE INDEX invoice_number_index ON invoice (invoice_number);

CREATE TABLE invoice_element (
    id SERIAL PRIMARY KEY,
    invoice_id INT NOT NULL,
    element_id INT NOT NULL,
    tax_id INT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (invoice_id) REFERENCES invoice(id),
    FOREIGN KEY (element_id) REFERENCES element(id),
    FOREIGN KEY (tax_id) REFERENCES tax(id)
);

CREATE TABLE invoice_payment (
    id SERIAL PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    amount FLOAT NOT NULL,
    payment_date DATE NOT NULL,
    payment_method VARCHAR(255) NOT NULL,
    invoice_id VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    bank_account_id INT,
    partner_id INT,
    reference VARCHAR(255),
    FOREIGN KEY (invoice_id) REFERENCES invoice(id),
    FOREIGN KEY (bank_account_id) REFERENCES bank_account(id),
    FOREIGN KEY (partner_id) REFERENCES partner(id)
);

CREATE TABLE TRANSFER (
    id SERIAL PRIMARY KEY,
    amount DECIMAL NOT NULL,
    from_bank_account_id INT NOT NULL,
    to_bank_account_id INT NOT NULL,
    transfer_date DATE NOT NULL,
    description VARCHAR(255),
    payment_method VARCHAR(255) NOT NULL,
    reference VARCHAR(255),
    FOREIGN KEY (from_bank_account_id) REFERENCES bank_account(id),
    FOREIGN KEY (to_bank_account_id) REFERENCES bank_account(id)
);