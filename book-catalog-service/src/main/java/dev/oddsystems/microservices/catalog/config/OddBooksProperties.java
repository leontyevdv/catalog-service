package dev.oddsystems.microservices.catalog.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oddbooks")
public record OddBooksProperties(String greeting) {}
