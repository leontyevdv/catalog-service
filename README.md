# Catalog Service

A sandbox for playing with different microservices concepts.

## What's interesting in this project
- Java 21
- Gatling simulations and load tests. See the [simulations](simulations) directory. Currenctly runs against the gatling test environment.
- Spec-first approach with the OpenAPI specification. See the [apis/spec](apis/spec) directory.
- Spec-first server with OpenAPI Generator. See the [apis/server](apis/server) directory.
- External configuration with Spring Cloud Config Server. See the [config-repo](https://github.com/leontyevdv/book-service-config).
- ADR (Architecture Decision Records). See the [doc/architecture/decisions](doc/architecture/decisions) directory and  [the ADR Tools' repo](https://github.com/npryce/adr-tools)

## TODO
- Modify Gatling simulations to simulate load for this service.
- Configure spec-first client generation.
- Vulnerability checks with grype.

## Useful Commands

### Build and run the application

| Gradle Command	         | Description                                   |
|:---------------------------|:----------------------------------------------|
| `./gradlew bootRun`        | Run the application.                          |
| `./gradlew build`          | Build the application.                        |
| `./gradlew test`           | Run tests.                                    |
| `./gradlew bootJar`        | Package the application as a JAR.             |
| `./gradlew bootBuildImage` | Package the application as a container image. |

After building the application, you can also run it from the Java CLI:

```bash
java -jar build/libs/catalog-service-0.0.1-SNAPSHOT.jar
```

### Run vulnerability checks

Prerequisites:
- Install grype. See [Installation documentation](https://github.com/anchore/grype#installation)

```bash
grype .
```

### Add new ADR

Prerequisites:
- Install adr-tools. See [Installation documentation](https://github.com/npryce/adr-tools/blob/master/INSTALL.md)

Create Architecture Decision Records:
```bash
adr new Implement as Unix shell scripts
```

Create a new ADR that supercedes a previous one:
```bash
adr new -s 9 Use Rust for performance-critical functionality
```

Help
```bash
adr help
```

## Container tasks

Run Catalog Service as a container

```bash
docker run --rm --name catalog-service -p 8080:8080 catalog-service:0.0.1-SNAPSHOT
```

### Container Commands

| Docker Command	              | Description       |
|:-------------------------------:|:-----------------:|
| `docker stop catalog-service`   | Stop container.   |
| `docker start catalog-service`  | Start container.  |
| `docker remove catalog-service` | Remove container. |

## Kubernetes tasks

Prerequisites:
- Install minikube. See [Installation documentation](https://minikube.sigs.k8s.io/docs/start/)

### Create Deployment for application container

```bash
kubectl create deployment catalog-service --image=catalog-service:0.0.1-SNAPSHOT
```

### Create Service for application Deployment

```bash
kubectl expose deployment catalog-service --name=catalog-service --port=8080
```

### Port forwarding from localhost to Kubernetes cluster

```bash
kubectl port-forward service/catalog-service 8000:8080
```

### Delete Deployment for application container

```bash
kubectl delete deployment catalog-service
```

### Delete Service for application container

```bash
kubectl delete service catalog-service
```

## REST API

| Endpoint	      | Method   | Req. body  | Status | Resp. body     | Description    		   	     |
|:---------------:|:--------:|:----------:|:------:|:--------------:|:-------------------------------|
| `/books`        | `GET`    |            | 200    | Book[]         | Get all the books in the catalog. |
| `/books`        | `POST`   | Book       | 201    | Book           | Add a new book to the catalog. |
|                 |          |            | 422    |                | A book with the same ISBN already exists. |
| `/books/{isbn}` | `GET`    |            | 200    | Book           | Get the book with the given ISBN. |
|                 |          |            | 404    |                | No book with the given ISBN exists. |
| `/books/{isbn}` | `PUT`    | Book       | 200    | Book           | Update the book with the given ISBN. |
|                 |          |            | 200    | Book           | Create a book with the given ISBN. |
| `/books/{isbn}` | `DELETE` |            | 204    |                | Delete the book with the given ISBN. |

## Resources

- [Cloud Native Spring in Action](https://www.manning.com/books/cloud-native-spring-in-action) book
  by [Thomas Vitale](https://www.thomasvitale.com).