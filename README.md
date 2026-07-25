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

Creates a short code for a URL. The long URL is passed as a request parameter.

```bash
curl -X POST "http://localhost:8080/shorten" -d "longUrl=https://example.com/some/very/long/path"
```

Response is the generated short code as plain text:

```
aB3xY9z
```

## Roadmap

- `GET /{shortCode}` redirect endpoint (the `findByShortCode` port and the `clickCount` field are already in place)
- Return a JSON payload via `ShortenUrlDTO` instead of a bare string
- Bean validation on the incoming URL
- Persistent database instead of in-memory H2
