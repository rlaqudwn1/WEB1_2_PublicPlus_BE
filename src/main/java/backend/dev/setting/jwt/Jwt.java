package backend.dev.setting.jwt;

import backend.dev.setting.exception.ErrorCode;
import backend.dev.setting.exception.PublicPlusCustomException;
import backend.dev.setting.redis.Redis;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Jwt {
    @Value("${JwtToken.secret_key}")
    private String secretKey;
    @Value("${JwtToken.accessExpTime}") //1일
    private Long accessExpTime;
    @Value("${JwtToken.refreshExpTime}")//30일
    private Long refreshExpTime;
    private final Redis redis;

    public String sign(String headerType,String userId) {
        Date now = new Date();
        Claims claims = Jwts.claims();
        claims.setIssuer("PublicPlus");
        claims.setIssuedAt(now);
        claims.setSubject(userId);
        Long expireTime = (headerType.equals("access_token")  ? accessExpTime : refreshExpTime);
        claims.setExpiration(new Date(now.getTime() + expireTime));
        Map<String, Object> header = new HashMap<>();
        header.put("token", headerType);
        header.put("Alg", "HS256");
        header.put("typ","JWT");

        return Jwts.builder().setHeader(header).setClaims(claims).signWith(getKey()).compact();
    }


    public boolean verify(String token) {
        try {
            if (redis.isHasValues(token)) {
                return false;
            }
            Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new PublicPlusCustomException(ErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new PublicPlusCustomException(ErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new PublicPlusCustomException(ErrorCode.UNSUPPORTED_TOKEN);
        }
    }

    String getLoginId(String token) {
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new PublicPlusCustomException(ErrorCode.EXPIRED_TOKEN);
        }
    }
    public boolean isAccessToken(String token) {
        JwsHeader<?> header = getHeader(token);
        return "access_token".equals(header.get("token"));
    }
    public boolean isRefreshToken(String token) {
        JwsHeader<?> header = getHeader(token);
        return "refresh_token".equals(header.get("token"));
    }

    private JwsHeader<?> getHeader(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getHeader();
    }

    private Key getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }


}
