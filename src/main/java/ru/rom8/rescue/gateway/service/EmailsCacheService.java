package ru.rom8.rescue.gateway.service;

import org.jspecify.annotations.NonNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
public class EmailsCacheService {

    private static final String EMAILS_HASH_KEY = "volunteer-gateway:emails";

    private final StringRedisTemplate redisTemplate;

    public EmailsCacheService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public UUID getUUIDOrCreateByEmail(String email) {
        String cleanedEmail = getCleanedEmail(email);
        UUID uuid = UUID.randomUUID();
        Boolean created = redisTemplate.opsForHash().putIfAbsent(EMAILS_HASH_KEY, cleanedEmail, uuid.toString());

        if (Boolean.TRUE.equals(created)) {
            return uuid;
        }

        String registeredUuid = (String) redisTemplate.opsForHash().get(EMAILS_HASH_KEY, cleanedEmail);
        if (registeredUuid == null) {
            throw new IllegalStateException("Email UUID was not saved to Redis: " + cleanedEmail);
        }
        return UUID.fromString(registeredUuid);
    }

    public boolean hasRegistered(String email) {
        String cleanedEmail = getCleanedEmail(email);
        return Boolean.TRUE.equals(redisTemplate.opsForHash().hasKey(EMAILS_HASH_KEY, cleanedEmail));
    }

    private static @NonNull String getCleanedEmail(String email) {
        if (!StringUtils.hasText(email)) {
            throw new IllegalArgumentException("Wrong email: " + email);
        }
        return email.trim().toLowerCase();
    }
}
