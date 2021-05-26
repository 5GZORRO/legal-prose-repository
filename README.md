# Legal Prose Repository

The project encapsulates the Legal Prose Repository module of the 5GZORRO platform. 
It is a SpringBoot application with associated documentation built from Swagger annotations.

## Getting Started
To run the application using Maven simply execute the following from the command-line:

`./mvnw spring-boot:run`

### Start Database
To start a dev instance of potgres, simply execute the following docker-compose command in a terminal in the root of the project:

`docker-compose up`

This will start an instance of postgres on the usual localhost:5432 and the username/password to access the database can be found in `./database.env`

### Stop the database
To stop the database:
`docker-compose down` (add the flag `-v` to remove the persisted volumn/state as well)

### Staging Database
IMPORTANT: It is intentional that all admin domains are using the same governance db in the staging environment.  

Simulating shared access to a common DB for the purposes of administering the platform.  For our purposes, Operator A is hosting this DB.

You can see this configuration in `/.github/workflows/publish-package.yml`

## Documentation

You can access swagger-ui by running the API and navigating to `http://localhost:8080/swagger-ui`

An open-api schema is also available under `docs/swagger` from the root of the project.

## Updating OpenAPI swagger-ui file
This should be completed as a github action but to do manually run ./mvnw springdoc-openapi:generate. This should output an updated openapi.json file at: /docs/swagger based on the spring apis rest controllers
