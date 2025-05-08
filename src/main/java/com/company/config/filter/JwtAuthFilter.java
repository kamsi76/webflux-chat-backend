package com.company.config.filter;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.company.chat.repository.UserRepository;
import com.company.common.dto.ApiResponse;
import com.company.common.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * JWT 인증 필터 (WebFlux용)
 * - 모든 요청의 Authorization 헤더에서 JWT를 추출하고 검증
 * - 유효한 경우 User 객체를 request attribute로 저장
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter implements WebFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환 도구

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String path = exchange.getRequest().getPath().toString();
        String method = exchange.getRequest().getMethod().name();

        if ("OPTIONS".equalsIgnoreCase(method)) {
            return chain.filter(exchange);
        }

        //로그인 회원가입과 라우터 정보를 가져오는 경로는 필터에서 제외처리
        if( path.startsWith("/api/v1/auth") || path.startsWith("/api/v1/routes")) {
            return chain.filter(exchange);
        }

        String token = exchange.getRequest().getHeaders().getFirst("Authorization");

        if( token == null || !token.startsWith("Bearer ")) {
            return writeUnauthorizedResponse(exchange, "Authorization 헤더가 없습니다.");
        }

        String username;

        try {
            username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        } catch (Exception e) {
            return writeUnauthorizedResponse(exchange, "JWT 파싱 실패");
        }

        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new RuntimeException("사용자 없음")))
                .flatMap(user -> {
                    // 인증된 사용자 정보를 attribute에 저장
                    exchange.getAttributes().put("user", user);
                    return chain.filter(exchange); // 다음 필터 or Controller로 진행
                })
                .onErrorResume(e -> writeUnauthorizedResponse(exchange, e.getMessage()));

    }

    /**
     * 인증에 실패 한경우 응답하기 위해 등답메시지를 JSON 형식으로 변환한다.
     * @param exchange
     * @param message
     * @return
     */
    private Mono<Void> writeUnauthorizedResponse(ServerWebExchange exchange, String message) {

        // 응답 객체 구성
        ApiResponse<Object> response = new ApiResponse<>(false, message, null);

        try {

            // JSON 변환
            byte[] json = objectMapper.writeValueAsBytes(response);

            //응답 설정
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

            // JSON을 response body에 쓰기
            DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
            DataBuffer buffer = bufferFactory.wrap(json);

            return exchange.getResponse().writeWith(Flux.just(buffer));


        } catch (Exception e) {
            return exchange.getResponse().setComplete();
        }

    }

}
