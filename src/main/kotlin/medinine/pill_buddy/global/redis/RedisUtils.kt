package medinine.pill_buddy.global.redis

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit



@Component
class RedisUtils(
    @Qualifier(value = "BlackList") private val redisTemplate: RedisTemplate<String, String>
) {

    fun setBlackList(token: String, loginId: String, milliSeconds: Long) {
        redisTemplate.opsForValue().set(token, loginId, milliSeconds, TimeUnit.MILLISECONDS)
    }

    fun hasTokenBlackList(token: String): Boolean {
        return redisTemplate.hasKey(token)
    }

    fun clearAll() {
        val keys = redisTemplate.keys("*")
        keys.let {
            redisTemplate.delete(it)
        }
    }
}