
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    user_KC_guid VARCHAR(200) NOT NULL,
    email VARCHAR(200) NOT NULL,
    roles VARCHAR(200) NOT NULL,
    status VARCHAR(200) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_profile (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    first_name VARCHAR(200) NOT NULL,
    last_name VARCHAR(200) NOT NULL,
    phone_number VARCHAR(200),
    profile_picture BYTEA,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

create table bank_account (
    id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    account_number VARCHAR(200) NOT NULL,
    description VARCHAR(200),
    sold DECIMAL NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE partner (
     id SERIAL PRIMARY KEY,
     name VARCHAR(200) NOT NULL,
     email VARCHAR(200),
     phone_number VARCHAR(20),
     website VARCHAR(200),
     reference VARCHAR(200),
     profile_picture BYTEA,
     CUI VARCHAR(20) NOT NULL UNIQUE,
     reg_com VARCHAR(20) NOT NULL,
     currency_code VARCHAR(3) DEFAULT 'RON',
     address VARCHAR(200) NOT NULL,
     city VARCHAR(200) NOT NULL,
     county VARCHAR(200),
     country VARCHAR(200) NOT NULL,
     postal_code VARCHAR(20) NOT NULL,
     deleted BOOLEAN DEFAULT FALSE,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE category (
    id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    type VARCHAR(200) NOT NULL,
    color_code VARCHAR(200) NOT NULL,
    deleted BOOLEAN DEFAULT FALSE
);

create table tax (
    id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    type VARCHAR(200),
    rate FLOAT NOT NULL,
    deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE element (
    id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    type VARCHAR(200) NOT NULL,
    description VARCHAR(200),
    acquisition_price FLOAT,
    selling_price FLOAT,
    category_id INT,
    tax_id INT,
    deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES category(id),
    FOREIGN KEY (tax_id) REFERENCES tax(id)
);

CREATE TABLE invoice (
    id SERIAL PRIMARY KEY,
    total_amount FLOAT NOT NULL,
    remaining_amount FLOAT NOT NULL,
    partner_id INT NOT NULL,
    category_id INT,
    invoice_number VARCHAR(200) NOT NULL UNIQUE ,
    order_number VARCHAR(200),
    direction VARCHAR(200) NOT NULL,
    invoice_date DATE NOT NULL,
    due_date DATE NOT NULL,
    completed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (partner_id) REFERENCES partner(id),
    FOREIGN KEY (category_id) REFERENCES category(id)
);

CREATE UNIQUE INDEX invoice_number_index ON invoice (invoice_number);

CREATE TABLE invoice_element (
    id SERIAL PRIMARY KEY,
    invoice_id INT NOT NULL,
    element_id INT NOT NULL,
    tax FLOAT NOT NULL,
    quantity INT NOT NULL,
    price FLOAT NOT NULL,
    FOREIGN KEY (invoice_id) REFERENCES invoice(id),
    FOREIGN KEY (element_id) REFERENCES element(id)
);

CREATE TABLE invoice_payment (
    id SERIAL PRIMARY KEY,
    type VARCHAR(200) NOT NULL,
    amount FLOAT NOT NULL,
    payment_date DATE NOT NULL,
    payment_method VARCHAR(200) NOT NULL,
    direction VARCHAR(200) NOT NULL,
    invoice_id INTEGER,
    category_id INT,
    reference VARCHAR(200),
    description VARCHAR(200),
    bank_account_id INT,
    FOREIGN KEY (invoice_id) REFERENCES invoice(id),
    FOREIGN KEY (category_id) REFERENCES category(id),
    FOREIGN KEY (bank_account_id) REFERENCES bank_account(id)
);

CREATE INDEX invoice_id_index ON invoice_payment (reference);

CREATE TABLE TRANSFER (
    id SERIAL PRIMARY KEY,
    amount DECIMAL NOT NULL,
    from_bank_account_id INT NOT NULL,
    to_bank_account_id INT NOT NULL,
    transfer_date DATE NOT NULL,
    description VARCHAR(200),
    payment_method VARCHAR(200) NOT NULL,
    reference VARCHAR(200),
    FOREIGN KEY (from_bank_account_id) REFERENCES bank_account(id),
    FOREIGN KEY (to_bank_account_id) REFERENCES bank_account(id)
);