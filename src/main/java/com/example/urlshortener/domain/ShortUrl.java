package com.example.urlshortener.domain;

import java.time.Instant;
import java.util.UUID;

public record ShortUrl(String id,
                       String longUrl,
                       String shortCode,
                       Instant createdAt,
                       long clickCount) {

    public static ShortUrl create(String longUrl, String shortCode) {
        return new ShortUrl(UUID.randomUUID().toString(), longUrl, shortCode, Instant.now(), 0L);
    }

    public ShortUrl withIncrementedClicks() {
        return new ShortUrl(id, longUrl, shortCode, createdAt, clickCount + 1);
    }
}
