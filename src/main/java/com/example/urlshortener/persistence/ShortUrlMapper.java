package com.example.urlshortener.persistence;

import com.example.urlshortener.domain.ShortUrl;
import com.example.urlshortener.persistence.entity.ShortUrlEntity;

final class ShortUrlMapper {

    private ShortUrlMapper() {
    }

    static ShortUrlEntity toEntity(ShortUrl shortUrl) {
        return new ShortUrlEntity(
                shortUrl.id(),
                shortUrl.longUrl(),
                shortUrl.shortCode(),
                shortUrl.createdAt(),
                shortUrl.clickCount());
    }

    static ShortUrl toDomain(ShortUrlEntity entity) {
        return new ShortUrl(
                entity.getId(),
                entity.getLongUrl(),
                entity.getShortCode(),
                entity.getCreatedAt(),
                entity.getClickCount());
    }
}
