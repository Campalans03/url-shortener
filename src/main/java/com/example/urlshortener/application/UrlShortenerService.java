package com.example.urlshortener.application;

import com.example.urlshortener.domain.ShortCodeGenerator;
import com.example.urlshortener.domain.ShortUrl;
import com.example.urlshortener.domain.ShortUrlRepository;
import org.springframework.stereotype.Service;

@Service
public class UrlShortenerService {

    private static final int MAX_ATTEMPTS = 5;

    private final ShortUrlRepository shortUrlRepository;
    private final ShortCodeGenerator shortCodeGenerator;

    public UrlShortenerService(ShortUrlRepository shortUrlRepository, ShortCodeGenerator shortCodeGenerator) {
        this.shortUrlRepository = shortUrlRepository;
        this.shortCodeGenerator = shortCodeGenerator;
    }

    public String shortenUrl(String longUrl) {
        ShortUrl saved = shortUrlRepository.save(ShortUrl.create(longUrl, generateUniqueCode()));
        return saved.shortCode();
    }

    private String generateUniqueCode() {
        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            String candidate = shortCodeGenerator.generate();
            if (!shortUrlRepository.existsByShortCode(candidate)) {
                return candidate;
            }
        }
        throw new IllegalStateException(
                "Could not generate a unique short code after " + MAX_ATTEMPTS + " attempts");
    }
}
