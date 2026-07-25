package com.example.urlshortener.persistence;

import com.example.urlshortener.persistence.entity.ShortUrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface ShortUrlJpaRepository extends JpaRepository<ShortUrlEntity, String> {

    Optional<ShortUrlEntity> findByShortCode(String shortCode);

    boolean existsByShortCode(String shortCode);
}
