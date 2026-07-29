package com.example.urlshortener.api;

import com.example.urlshortener.application.DTO.ShortenUrlDTO;
import com.example.urlshortener.application.DTO.ShortenUrlRequest;
import com.example.urlshortener.application.UrlShortenerService;
import com.example.urlshortener.domain.ShortUrl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
public class UrlRestController {
    private final UrlShortenerService urlShortenerService;

    public UrlRestController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<ShortenUrlDTO> shortenUrl(@Valid @RequestBody ShortenUrlRequest request) {
        ShortUrl shortUrl = urlShortenerService.shortenUrl(request.longUrl());
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/{shortCode}")
                .buildAndExpand(shortUrl.shortCode())
                .toUri();
        return ResponseEntity.created(location).body(new ShortenUrlDTO(shortUrl));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        return urlShortenerService.resolve(shortCode)
                .map(longUrl -> ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create(longUrl))
                        .<Void>build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
