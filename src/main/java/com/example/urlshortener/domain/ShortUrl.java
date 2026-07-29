package com.example.urlshortener.domain;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

public record ShortUrl(String id,
                       String longUrl,
                       String shortCode,
                       Instant createdAt,
                       long clickCount) {

    /** Matches the column length of {@code short_url.long_url}. */
    private static final int MAX_URL_LENGTH = 2048;

    public static ShortUrl create(String longUrl, String shortCode) {
        return new ShortUrl(UUID.randomUUID().toString(), normalize(longUrl), shortCode, Instant.now(), 0L);
    }

    public ShortUrl withIncrementedClicks() {
        return new ShortUrl(id, longUrl, shortCode, createdAt, clickCount + 1);
    }

    private static String normalize(String longUrl) {
        if (longUrl == null || longUrl.isBlank()) {
            throw new InvalidUrlException("The URL must not be empty");
        }
        String stripped = longUrl.strip();
        if (stripped.length() > MAX_URL_LENGTH) {
            throw new InvalidUrlException("The URL must not exceed " + MAX_URL_LENGTH + " characters");
        }

        URI uri;
        try {
            uri = new URI(stripped);
        } catch (URISyntaxException e) {
            throw new InvalidUrlException("The URL is not a valid URI: " + e.getReason());
        }
        if (!uri.isAbsolute() || uri.getHost() == null) {
            throw new InvalidUrlException(
                    "The URL must be absolute and contain a host, for example https://example.com");
        }
        // Anything else (javascript:, data:, file:) would be echoed back in a Location header.
        String scheme = uri.getScheme().toLowerCase(Locale.ROOT);
        if (!scheme.equals("http") && !scheme.equals("https")) {
            throw new InvalidUrlException("Only http and https URLs are supported, but got: " + scheme);
        }
        return stripped;
    }
}
