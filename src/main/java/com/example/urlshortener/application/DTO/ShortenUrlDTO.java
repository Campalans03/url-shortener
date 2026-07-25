package com.example.urlshortener.application.DTO;

import com.example.urlshortener.domain.ShortUrl;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ShortenUrlDTO {
    private String id;
    private String longUrl;
    private String shortCode;

    public ShortenUrlDTO(ShortUrl shortUrl) {
        this.id = shortUrl.id();
        this.longUrl = shortUrl.longUrl();
        this.shortCode = shortUrl.shortCode();
    }
}
