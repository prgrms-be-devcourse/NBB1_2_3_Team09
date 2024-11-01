package medinine.pill_buddy.global.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import medinine.pill_buddy.domain.user.service.MyUserDetailService
import medinine.pill_buddy.global.exception.ErrorCode
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import medinine.pill_buddy.global.redis.RedisUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtTokenProvider(

    @Value("\${jwt.token.client-secret}")
    secretKey: String,

    @Value("\${jwt.token.access-expiration-time}")
    private val accessExpirationTime: Long,

    @Value("\${jwt.token.refresh-expiration-time}")
    private val refreshExpirationTime: Long,

    private val myUserDetailsService: MyUserDetailService,

    private val redisUtils: RedisUtils
) {
    companion object {
        private const val GRANT_TYPE = "Bearer"
        private const val ACCESS_TOKEN_TYPE = "ACCESS"
        private const val REFRESH_TOKEN_TYPE = "REFRESH"
    }

    private val key: Key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))

    fun generateToken(loginId: String): JwtToken {
        val now = Date()
        val accessToken = getAccessToken(loginId, now)
        val refreshToken = getRefreshToken(loginId, now)

        return JwtToken(GRANT_TYPE, accessToken, refreshToken)
    }

    fun reissueAccessToken(refreshToken: String): JwtToken {
        val loginId = getLoginId(refreshToken)
        val claims = parseClaims(refreshToken)
        val remainingTime = claims.expiration.time - Date().time

        redisUtils.setBlackList(refreshToken, loginId, remainingTime)
        val userDetails = myUserDetailsService.loadUserByUsername(loginId)

        return generateToken(userDetails.username)
    }

    private fun getAccessToken(loginId: String, now: Date): String {
        val accessTokenExpireDate = Date(now.time + accessExpirationTime)

        val claims = Jwts.claims().apply {
            subject = loginId
        }

        return Jwts.builder()
            .setHeader(setHeader(ACCESS_TOKEN_TYPE))
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(accessTokenExpireDate)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    private fun getRefreshToken(loginId: String, now: Date): String {
        val refreshTokenExpireDate = Date(now.time + refreshExpirationTime)

        val claims = Jwts.claims().apply {
            subject = loginId
        }

        return Jwts.builder()
            .setHeader(setHeader(REFRESH_TOKEN_TYPE))
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(refreshTokenExpireDate)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    private fun setHeader(type: String): Map<String, Any> = mapOf(
        "type" to "JWT",
        "tokenType" to type,
        "alg" to "HS256"
    )

    fun getAuthenticationByToken(token: String): Authentication {
        val loginId = getLoginId(token)
        val userDetails = myUserDetailsService.loadUserByUsername(loginId)
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    private fun getLoginId(token: String): String = parseClaims(token).subject

    private fun parseClaims(token: String): Claims {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .body
        } catch (e: ExpiredJwtException) {
            throw PillBuddyCustomException(ErrorCode.JWT_TOKEN_EXPIRED)
        }
    }

    fun validateToken(token: String): Boolean {
        return try {
            if (redisUtils.hasTokenBlackList(token)) {
                return false
            }
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
            true
        } catch (e: SecurityException) {
            throw PillBuddyCustomException(ErrorCode.JWT_TOKEN_INVALID)
        } catch (e: MalformedJwtException) {
            throw PillBuddyCustomException(ErrorCode.JWT_TOKEN_INVALID)
        } catch (e: ExpiredJwtException) {
            throw PillBuddyCustomException(ErrorCode.JWT_TOKEN_EXPIRED)
        } catch (e: UnsupportedJwtException) {
            throw PillBuddyCustomException(ErrorCode.JWT_TOKEN_UNSUPPORTED)
        }
    }

    fun isAccessToken(token: String): Boolean {
        return ACCESS_TOKEN_TYPE == getHeader(token)["tokenType"]
    }

    fun isRefreshToken(token: String): Boolean {
        return REFRESH_TOKEN_TYPE == getHeader(token)["tokenType"]
    }

    private fun getHeader(token: String): JwsHeader<*> {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .header
    }

    fun resolveToken(bearerToken: String?): String? {
        return bearerToken
            ?.takeIf { it.startsWith(GRANT_TYPE) && it.length > GRANT_TYPE.length + 1 }
            ?.substring(GRANT_TYPE.length + 1)
    }
}