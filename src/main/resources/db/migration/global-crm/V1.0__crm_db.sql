
CREATE TABLE organization (
      id SERIAL PRIMARY KEY,
      name VARCHAR(200) NOT NULL,
      tenant_id VARCHAR(200) NOT NULL,
      db_schema_name VARCHAR(200) NOT NULL,
      font VARCHAR(200),
      color_code_nav_bar VARCHAR(200),
      color_code_side_bar VARCHAR(200),
      license VARCHAR(200) NOT NULL,
      status VARCHAR(20) NOT NULL
);

CREATE INDEX idx_organization_tenant_id ON organization(tenant_id);

CREATE TABLE organization_info (
      id SERIAL PRIMARY KEY,
      organization_id INTEGER NOT NULL,
      phone_number VARCHAR(20),
      email VARCHAR(100),
      image BYTEA,
      CUI VARCHAR(20) NOT NULL UNIQUE,
      reg_com VARCHAR(20) NOT NULL,
      address VARCHAR(200) NOT NULL,
      city VARCHAR(100) NOT NULL,
      county VARCHAR(100) NOT NULL,
      country VARCHAR(100) NOT NULL,
      postal_code VARCHAR(20) NOT NULL,
      FOREIGN KEY (organization_id) REFERENCES organization(id)
);

CREATE TABLE organization_invoice_preferences (
      id SERIAL PRIMARY KEY,
      organization_id INTEGER NOT NULL,
      prefix VARCHAR(20) NOT NULL,
      starting_number INTEGER NOT NULL DEFAULT 1,
      number_of_decimals INTEGER NOT NULL DEFAULT 2,
      notes VARCHAR(200),
      sub_header VARCHAR(200),
      footer VARCHAR(200),
      FOREIGN KEY (organization_id) REFERENCES organization(id)
);