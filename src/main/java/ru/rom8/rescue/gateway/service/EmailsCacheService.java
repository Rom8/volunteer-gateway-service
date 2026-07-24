package ru.rom8.rescue.gateway.service;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class EmailsCacheService {

    private final Map<String, UUID> emailsCache = new LinkedHashMap<>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, UUID> eldest) {
            return size() > 10_000;
        }
    };  //todo make cache (redis?)

    public UUID getUUIDOrCreateByEmail(String email) {
        final String cleanedEmail = getCleanedEmail(email);
        UUID uuid = emailsCache.get(cleanedEmail);
        if (uuid == null) {
            uuid = UUID.randomUUID();       //todo make uuid in redis
            emailsCache.put(cleanedEmail, uuid);
        }
        return uuid;
    }

    public boolean hasRegistered(String email) {
        final String cleanedEmail = getCleanedEmail(email);
        return emailsCache.containsKey(cleanedEmail);
    }

    private static @NonNull String getCleanedEmail(String email) {
        if (!StringUtils.hasText(email)) {
            throw new IllegalArgumentException("Wrong email: " + email);
        }
        return email.trim().toLowerCase();
    }
}
