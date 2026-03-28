package com.vinogradov.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "mail")
public record MailProperties(
        String sender,
        String from,
        String subject,
        String content,
        String baseUrl
) {
}
