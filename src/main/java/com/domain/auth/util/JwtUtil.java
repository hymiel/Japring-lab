package com.domain.auth.util;

import com.domain.auth.exception.ExpiredTokenException;
import com.domain.auth.exception.InvalidTokenException;
import com.domain.user.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    /**
     * 생성자 주입 방식으로 유틸 작성
     *  - @Value 어노테이션을 통해 생성자를 주입 할 경우 순환 참조 위험이 줄고, 테스트 시 Mocktito나 생성자 기반 주입이 편리함 또, 모든 의존성이 생성 시점에 주입되기 때문에 불변성 보장
     */
    private final Key secretKey;

    private final Long TokenExpirationTime;

    public JwtUtil(@Value("${security.jwt.secret-key}") String secretKey,
                   @Value("${security.jwt.expiration-time}") Long TokenExpirationTime)
    {
        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
        this.TokenExpirationTime = TokenExpirationTime;
    }


    public String generateToken(User user) {
        // JWT 토큰 안에 들어갈 클레임(Claims) 객체를 생성 = payload => Claims : JWT Payload 내용을 구성하는 Map-like 구조
        // Claims claims = Jwts.claims().setSubject((user.getEmail()));
        // ↑ 이렇게 쓰면 sub(subject) 필드를 설정하는 것으로 사용자 식별자(누구 토큰이냐?) 구분, * JWT payload 생성 예시 : {"sub": "hymiel@example.com"}

        Claims claims = Jwts.claims(); // 빈 Claims 객체 생성
        claims.put("userId", user.getId()); // 커스텀 정보 삽입 (role, id 등)
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole());
        ZonedDateTime now = ZonedDateTime.now(); // 시간이 필요하며 넣는걸로

        // JWT를 생성해서 반환하기 >> header, payload, signature를 순차적으로 구성하지만 header는 명시하지 않아도 자동 생성 되는 것 기억하지!
        return Jwts.builder()
//                .setHeaderParam("kid", "my-key-id") // header를 별도로 명시하고 싶으면...
//                .setHeaderParam("typ", "JWT")
                .setClaims(claims) // payload 내용
                .setExpiration(Date.from(now.plusSeconds(TokenExpirationTime).toInstant())) // 토큰 만료 설정 now = 현재시간, TokenExpirationTime = 몇 초 뒤까지 유효한지
                .signWith(secretKey, SignatureAlgorithm.HS256) // JWT 서명 : HMAC-SHA256 알고리즘을 사용해 위조 방지 서명, 얘가 빠지면 JWT 무결성 보장이 안됨
                .compact(); // JWT -> String으로 압축해서 반환 >> eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGVtYWlsLmNvbSIsInJvbGUiOiJVU0VSIiwiZXhwIjoxNzIwNzEwODAwfQ.Gjr1GgQfCq_XIgN4G7XZyCzS0NHTAGo0bGyMPbO3VpE 이런식으로 되겠지?

        // 여기까지가 Authorization: Bearer ... 로 쓰이는 토큰!
    }

    public Claims decodeToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey) // 반드시 같은 key로 서명한 token!
                    .build()
                    .parseClaimsJws(token)
                    .getBody(); //  Payload 영역 (Claims) 반환
        } catch (SecurityException | MalformedJwtException | IllegalArgumentException | DecodingException | NullPointerException e) {
            // 🔐 시그니처 검증 실패, 형식 이상, 디코딩 실패 등
            // - 시크릿 키 위조
            // - 토큰 구조 자체가 잘못됨 (ex. header.payload.signature 형식 아님)
            // - Base64 디코딩 실패, null 전달된 경우 등
            throw new InvalidTokenException("잘못된 인증 방식입니다. 올바른 인증 방식을 사용해주세요.");
        } catch (ExpiredJwtException e) {
            // ⏰ 토큰 만료 시
            // - exp 값이 현재 시간보다 과거인 경우
            throw new ExpiredTokenException("로그인이 만료되었습니다. 다시 로그인해주세요.");
        } catch (UnsupportedJwtException e) {
            // 🚫 JWT 형식은 맞지만 JJWT에서 지원하지 않는 구조/알고리즘인 경우
            throw new InvalidTokenException("지원하지 않는 인증 방식입니다. 올바른 인증을 사용해주세요.");
        } catch (Exception e) {
            // ❗ 예상하지 못한 그 외 모든 예외
            // - null 키 오류, 내부 라이브러리 문제 등
            log.error(e.getMessage());
            throw new InvalidTokenException("토큰 인증 에러가 발생했습니다.\n관리자에게 문의하세요.");
        }

    }
}
