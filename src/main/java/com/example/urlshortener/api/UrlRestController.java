package com.example.urlshortener.api;

import com.example.urlshortener.application.UrlShortenerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class UrlRestController {
    private final UrlShortenerService urlShortenerService;

    public UrlRestController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping("/shorten")
    public String shortenUrl(String longUrl) {
        return urlShortenerService.shortenUrl(longUrl);
    }

    /**
     * The path is restricted to the shape of a generated code so this mapping does not
     * swallow other routes such as {@code /shorten} or {@code /h2-console}.
     */
    @GetMapping("/{shortCode:[A-Za-z0-9]{7}}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        return urlShortenerService.resolve(shortCode)
                .map(longUrl -> ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create(longUrl))
                        .<Void>build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
