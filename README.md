# Muvix Backend

Spring Boot MVC API for the Muvix frontend.

## Run locally

Set these environment variables before starting the application:

- `DB_PASSWORD`: Supabase PostgreSQL password.
- `JWT_SECRET`: Base64-encoded random key containing at least 32 bytes.

The configured datasource URL and user can be overridden with `DATABASE_URL` and `DATABASE_USERNAME`. The API listens on port 8080 by default.

Swagger UI is available at `/swagger-ui/index.html` and the OpenAPI document at `/v3/api-docs`.
