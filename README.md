# Legal Prose Repository

The project encapsulates the Legal Prose Repository module of the 5GZORRO platform. 
It is a SpringBoot application with associated documentation built from Swagger annotations.

## Getting Started
To run the application using Maven simply execute the following from the command-line:

`./mvnw spring-boot:run`

## Documentation

You can access swagger-ui by running the API and navigating to `http://localhost:8080/swagger-ui`

An open-api schema is also available under `docs/swagger` from the root of the project.

## Updating OpenAPI swagger-ui file
This should be completed as a github action but to do manually run ./mvnw springdoc-openapi:generate. This should output an updated openapi.json file at: /docs/swagger based on the spring apis rest controllers
