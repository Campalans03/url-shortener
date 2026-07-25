package com.example.urlshortener.persistence;

import com.example.urlshortener.domain.ShortUrl;
import com.example.urlshortener.domain.ShortUrlRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ShortUrlRepositoryAdapter implements ShortUrlRepository {

    private final ShortUrlJpaRepository jpaRepository;

    public ShortUrlRepositoryAdapter(ShortUrlJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ShortUrl save(ShortUrl shortUrl) {
        return ShortUrlMapper.toDomain(jpaRepository.save(ShortUrlMapper.toEntity(shortUrl)));
    }

    @Override
    public Optional<ShortUrl> findByShortCode(String shortCode) {
        return jpaRepository.findByShortCode(shortCode).map(ShortUrlMapper::toDomain);
    }

    @Override
    public boolean existsByShortCode(String shortCode) {
        return jpaRepository.existsByShortCode(shortCode);
    }
}
