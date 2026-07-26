# GameFlix Backend CI

This project uses GitHub Actions to automatically build and test the Spring Boot backend.

## When the workflow runs

The workflow is defined in `.github/workflows/ci.yml`. It starts when:

- Code is pushed to the `main` branch.
- A pull request targets the `main` branch.

## Workflow steps

GitHub creates an Ubuntu runner and performs these steps in order:

1. **Checkout repository** – downloads the project source code.
2. **Set up Java** – installs Amazon Corretto JDK 21 and enables Maven dependency caching.
3. **Build with Maven** – runs `./mvnw clean package` to compile the application, run its tests, and create the JAR file.
4. **Build Docker image** – uses the `Dockerfile` to build the `gameflix-backend` image.

If a step fails, the workflow stops and reports an error. A successful run confirms that the application can be tested, packaged, and built as a Docker image.
