# url-shortener

A URL shortening service built with Spring Boot, structured around a hexagonal (ports & adapters) architecture.

## Stack

- Java 21
- Spring Boot 4.1.0 (Web MVC, Data JPA, Validation)
- H2 (in-memory) + H2 console
- Lombok
- Maven (wrapper included)

## Architecture

The code is split so that the domain has no knowledge of Spring or JPA:

```
com.example.urlshortener
├── domain          # ShortUrl record, ShortCodeGenerator, ShortUrlRepository (port)
├── application     # UrlShortenerService use cases, DTOs
├── persistence     # JPA entity, Spring Data repository, adapter + mapper
├── api             # REST controller
└── config          # Bean wiring
```

`ShortUrlRepository` is the port defined in the domain; `ShortUrlRepositoryAdapter` implements it on top of Spring Data JPA, mapping between the `ShortUrl` domain record and the `ShortUrlEntity`.

Short codes are 7 characters drawn from a 62-character alphabet using `SecureRandom`. `UrlShortenerService` retries up to 5 times if a generated code already exists, and fails fast otherwise.

## Running

```bash
./mvnw spring-boot:run
```

The app starts on `http://localhost:8080`. The H2 console is available at `/h2-console`.

## Tests

```bash
./mvnw test
```

## API

### `POST /shorten`

Creates a short code for a URL.

```bash
curl -X POST "http://localhost:8080/shorten" \
  -H "Content-Type: application/json" \
  -d '{"longUrl": "https://example.com/some/very/long/path"}'
```

Responds `201 Created` with a `Location` header pointing at the short URL and a JSON body:

```json
{
  "id": "0f0a4b1e-...",
  "longUrl": "https://example.com/some/very/long/path",
  "shortCode": "aB3xY9z"
}
```

The URL is stripped of surrounding whitespace and must be an absolute `http`/`https` URL with a
host, at most 2048 characters. Anything else is rejected with `400 Bad Request` and a
`ProblemDetail` body, so a stored URL is always safe to use as a redirect target.

### `GET /{shortCode}`

Responds `302 Found` with the original URL in the `Location` header, or `404 Not Found` if the
code is unknown. Each resolution increments `clickCount`.

```bash
curl -i "http://localhost:8080/aB3xY9z"
```

## Roadmap

- Persistent database instead of in-memory H2
