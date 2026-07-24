package ru.rom8.rescue.gateway.service;

import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmailsCacheServiceIT {

    private static final String EMAILS_HASH_KEY = "volunteer-gateway:emails";
    private static final String EMAIL = "volunteer@example.test";

    @Container
    private static final RedisContainer REDIS = new RedisContainer("redis:8.8.0");

    private LettuceConnectionFactory connectionFactory;
    private StringRedisTemplate redisTemplate;
    private EmailsCacheService emailsCacheService;

    @BeforeAll
    void setUp() {
        connectionFactory = new LettuceConnectionFactory(
                new RedisStandaloneConfiguration(REDIS.getHost(), REDIS.getRedisPort())
        );
        connectionFactory.afterPropertiesSet();

        redisTemplate = new StringRedisTemplate(connectionFactory);
        redisTemplate.afterPropertiesSet();
        emailsCacheService = new EmailsCacheService(redisTemplate);
    }

    @AfterEach
    void clearRedis() {
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushDb();
    }

    @AfterAll
    void tearDown() {
        connectionFactory.destroy();
    }

    @Test
    @DisplayName("Создаёт UUID для нового email и сохраняет его в Redis Hash")
    void createsUuidForNewEmailAndStoresItInRedisHash() {
        UUID uuid = emailsCacheService.getUUIDOrCreateByEmail(EMAIL);

        assertThat(uuid).isNotNull();
        assertThat(redisTemplate.opsForHash().get(EMAILS_HASH_KEY, EMAIL)).isEqualTo(uuid.toString());
        assertThat(emailsCacheService.hasRegistered(EMAIL)).isTrue();
    }

    @Test
    @DisplayName("Возвращает сохранённый UUID при повторном запросе email")
    void returnsSavedUuidForRepeatedEmail() {
        assertThat(redisTemplate.opsForHash().size(EMAILS_HASH_KEY)).isEqualTo(0);

        UUID createdUuid = emailsCacheService.getUUIDOrCreateByEmail(EMAIL);
        assertThat(redisTemplate.opsForHash().size(EMAILS_HASH_KEY)).isEqualTo(1);

        UUID returnedUuid = emailsCacheService.getUUIDOrCreateByEmail(EMAIL);
        assertThat(redisTemplate.opsForHash().size(EMAILS_HASH_KEY)).isEqualTo(1);

        assertThat(returnedUuid).isEqualTo(createdUuid);
        assertThat(redisTemplate.opsForHash().get(EMAILS_HASH_KEY, EMAIL)).isEqualTo(createdUuid.toString());
    }
}
