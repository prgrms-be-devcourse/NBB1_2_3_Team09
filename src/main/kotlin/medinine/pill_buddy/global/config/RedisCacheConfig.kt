package medinine.pill_buddy.global.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@Configuration
@EnableCaching
class RedisCacheConfig {

    // 코틀린에서 LocalDateTime 직렬화 문제 발생, 캐시 매니저 빈에 해결 로직 추가
    @Bean
    fun redisCacheManager(connectionFactory: RedisConnectionFactory): CacheManager {
        val typeValidator = BasicPolymorphicTypeValidator.builder()
            .allowIfSubType(Object::class.java)
            .build()

        val objectMapper = ObjectMapper().apply {
            registerModule(JavaTimeModule())
            activateDefaultTyping(typeValidator, ObjectMapper.DefaultTyping.NON_FINAL)
        }

        val redisSerializer = GenericJackson2JsonRedisSerializer(objectMapper)

        val redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
            .entryTtl(Duration.ofMinutes(5)) // TTL 설정

        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(redisCacheConfiguration)
            .build()
    }
}