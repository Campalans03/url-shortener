package com.example.urlshortener.api;

import com.example.urlshortener.application.UrlShortenerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UrlRestController {
    private UrlShortenerService urlShortenerService;

    public UrlRestController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping("/shorten")
    public String shortenUrl(String longUrl) {
        return urlShortenerService.shortenUrl(longUrl);
    }

}
