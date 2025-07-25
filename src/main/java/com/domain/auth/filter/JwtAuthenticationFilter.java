package com.domain.auth.filter;

import com.common.dto.ResponseDto;
import com.domain.auth.util.JwtUtil;
import com.common.exception.CustomRuntimeException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.rmi.server.ServerCloneException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    /**
     * 요청이 들어 올 때마다 JWT를 추출하고 인증 정보를 설정
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            // ① 요청 헤더에서 JWT 추출
            String token = resolveToken(request);

            // ② 토큰이 존재하고, 유효한 경우
            if (token != null){
                Claims claims = jwtUtil.decodeToken(token); // 유효성 검증 및 클레임 추출

                // JWT Build 할 때 subject를 넣었으면 이렇게! ↓
                // Claims claims = Jwts.claims().setSubject(user.getEmail());

                // JWT Build 할 때 subject를 안넣었으니까 getSubject() 대신!
                String email = claims.get("email", String.class);

                // ③ role 클레임 값
                /* claims.get("role") -> Object 반환
                    - 타입 불일치 시 예외가 발생하고, null은 null값을 반환함
                    - 타입 안전성은 강한 편
                 */
                String role = claims.get("role", String.class);

                /* String.valueOf(...) -> 문자열 변환
                   - 타입 상관없이 변환 > Integer, Boolean 등 다른 타입도 강제로 String 변환
                   - null 이면 "null"이라는 문자열 반환 (주의)
                   - Prefix 등 다양한 사유로 Object 반환이 안될 때...(?)
                 */
                //  String role = String.valueOf(claims.get("role"));

                // ④ 인증 객체 생성 및 SecurityContext에 저장
                // 사용자의 권한(Authority) 목록을 생성
                List<GrantedAuthority> authorities =
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)); // Spring Security는 "ROLE_" prefix가 붙은 권한을 기본으로 인식

                // 인증된 사용자 정보를 담은 Authentication 객체 생성
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(email, null, authorities);

                // 현재 요청의 SecurityContext에 Authentication 설정
                // → 이후 컨트롤러에서 @AuthenticationPrincipal 등으로 인증 정보 접근 가능
                SecurityContextHolder.getContext().setAuthentication(auth);

            }
            // 다음 필터 진행
            filterChain.doFilter(request, response);
        } catch (JwtException ex) {
            handleAuthenticationError(response, "지원하지 않는 인증 방식입니다. 올바른 인증을 사용해주세요.", "Invalid Token Type Exception");
        } catch (CustomRuntimeException e) {
            handleAuthenticationError(response, e.getMessage(), e.getReason());
        } catch (Exception e) {
            log.error("토큰 인증 에러 발생");
            handleAuthenticationError(response, "인증 에러가 발생했습니다.\n관리자에게 문의하세요.", null);
        }
    }


    /**
     * 요청의 Authorization 헤더에서 Bearer 토큰을 안전하게 추출하는 메서드.
     * - "Bearer {token}" 형식의 헤더 값에서 실제 토큰만 분리하여 반환
     * - 유효하지 않은 경우 null 반환
     *
     * @param request HttpServletRequest
     * @return 토큰 문자열 또는 null
     */
    private String resolveToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Authorization")) // ① Authorization 헤더가 존재할 경우 Optional로 감쌈 (null-safe)
                .filter(bearer -> StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) // ② 값이 비어있지 않고 "Bearer "로 시작하는 경우만 필터링
                .map(bearer -> bearer.substring(7)) // ③ "Bearer " 이후 실제 토큰 부분만 추출
                .orElse(null); // ④ 조건을 만족하지 않으면 null 반환
    }


    /**
     * JWT 인증 실패 시 JSON 형태의 에러 응답 리턴
     */
    private void handleAuthenticationError(HttpServletResponse response, String message, String reason) throws IOException {
        ResponseDto<Object> errorResponse = new ResponseDto<>(
                false,
                HttpServletResponse.SC_UNAUTHORIZED,
                message,
                reason,
                null
        );

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        String responseBody = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(responseBody);
    }

}
