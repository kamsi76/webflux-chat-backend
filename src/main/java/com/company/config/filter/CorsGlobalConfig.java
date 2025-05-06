package com.company.config.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsGlobalConfig {

	@Bean
	CorsWebFilter corsWebFilter() {
		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.addAllowedOrigin("http://localhost:5173"); // React 개발서버 Origin 허용
        corsConfig.addAllowedMethod("*"); // 모든 HTTP 메소드 허용 (GET, POST, PUT, DELETE 등)
        corsConfig.addAllowedHeader("*"); // 모든 헤더 허용
        corsConfig.setAllowCredentials(true); // 인증정보 포함 허용 (ex: 쿠키)
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig); // 모든 경로에 대해 CORS 설정 적용
        
        return new CorsWebFilter(source);
	}
	
}
