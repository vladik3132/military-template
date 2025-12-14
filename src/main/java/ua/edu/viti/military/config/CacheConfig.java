package ua.edu.viti.military.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class CacheConfig {
    
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        
        // Базова конфігурація (за замовчуванням 1 година)
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration
            .defaultCacheConfig()
            .entryTtl(Duration.ofHours(1))
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new StringRedisSerializer()
                )
            )
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new GenericJackson2JsonRedisSerializer()
                )
            );
        
        // Специфічні налаштування для різних кешів
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        
        // Категорії - кешуємо на 24 години (рідко змінюються)
        cacheConfigurations.put("vehicleCategories", defaultConfig.entryTtl(Duration.ofHours(24)));
        
        // Транспорт - кешуємо на 30 хвилин (часто змінюються)
        cacheConfigurations.put("vehicles", defaultConfig.entryTtl(Duration.ofMinutes(30)));
        
        // Водії - кешуємо на 2 години
        cacheConfigurations.put("drivers", defaultConfig.entryTtl(Duration.ofHours(2)));
        
        // Статистика - кешуємо на 5 хвилин
        cacheConfigurations.put("statistics", defaultConfig.entryTtl(Duration.ofMinutes(5)));
        
        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(defaultConfig)
            .withInitialCacheConfigurations(cacheConfigurations)
            .build();
    }
}
