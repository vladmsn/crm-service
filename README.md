# CRM Service

This project is a CRM system designed to manage users, partners, transactions, and invoices. It supports multi-tenancy with dedicated schemas for each tenant.

## Getting Started

### Prerequisites

- Java 17
- Maven
- PostgreSQL
- Docker (optional for running the database in a container)

### Installing

1. Clone the repository
```bash
git clone https://github.com/vladmsn/crm-service.git
```

2. Navigate into the project directory
```bash
cd crm-service
```

3. Build the project using Maven
```bash
mvn clean install
```

## Running the application

You can run the application using the following command:

```bash
mvn spring-boot:run
```

The application will start running at `http://localhost:8080`.

### Running the database in a container
```bash
docker-compose up -d
```

## Configuration

The application's configuration is located in `src/main/resources/application.yml`. Here you can configure the database connection and other application settings.

For testing, a separate configuration is provided in `src/test/resources/application-test.yml`.


## Authors

- **vladmsn** - *Initial work* - [vladmsn](https://github.com/vladmsn)
