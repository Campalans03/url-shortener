package com.example.urlshortener.application.DTO;

import jakarta.validation.constraints.NotBlank;

public record ShortenUrlRequest(@NotBlank(message = "longUrl must not be empty") String longUrl) {
}
