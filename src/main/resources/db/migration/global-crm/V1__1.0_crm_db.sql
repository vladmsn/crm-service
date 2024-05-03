
CREATE TABLE organization (
      id SERIAL PRIMARY KEY,
      name VARCHAR(200) NOT NULL,
      tenant_id VARCHAR(200) NOT NULL,
      db_schema_name VARCHAR(200) NOT NULL,
      license UUID NOT NULL DEFAULT uuid_generate_v4(),
      image BYTEA
);

CREATE INDEX idx_organization_tenant_id ON organization(tenant_id);

CREATE TABLE organization_info (
      id SERIAL PRIMARY KEY,
      organization_id INTEGER NOT NULL,
      telephone VARCHAR(20) NOT NULL,
      CUI VARCHAR(20) NOT NULL,
      address VARCHAR(200) NOT NULL,
      city VARCHAR(100) NOT NULL,
      county VARCHAR(100) NOT NULL,
      country VARCHAR(100) NOT NULL,
      postal_code VARCHAR(20) NOT NULL,
      FOREIGN KEY (organization_id) REFERENCES organization(id)
);