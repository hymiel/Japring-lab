package com.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Spring 설정 클래스 :  빈(Bean) 등록
@Configuration
// OpenAPI 문서의 전역 설정을 정의
@OpenAPIDefinition(
        // 📌 import 된 경우
        // import io.swagger.v3.oas.annotations.info.Info;
        // info = @Info()

        // 📌 import 없이 패키지 전체 명시
        info = @io.swagger.v3.oas.annotations.info.Info(
                // 문서 제목
                title = "API SWAGGER BY HYMIEL",
                // 문서 설명
                description = "API SWAGGER BY HYMIEL",
                // 문서 버전
                version = "v1",
                // 서비스 약관 링크
                termsOfService = "https://example.com/terms",
                // 개발자/관리자 연락처 정보
                contact = @Contact(
                        name = "이현경",
                        email = "your.email@example.com",
                        url = "https://github.com/hymiel"
                ),
                // API 문서의 라이선스 정보
                license = @License(
                        name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0"
                )
        )
)


public class SwaggerConfig {

    // Swagger 보안 스키마 이름 상수 정의 (Bearer 인증을 위한 구분자)
    private static final String SECURITY_SCHEME_NAME = "Bearer Auth";

    // 현재 활성화된 Spring profile(local/dev/prod 등)를 주입 받음
    @Value("${spring.profiles.active:local}")
    private String activeProfile;

    /**
     * Swagger에서 v1 API 그룹으로 구분해서 문서를 보여줄 수 있도록 설정
     * /api/v1/** 경로의 요청만 이 그룹에 포함됨
     */
    @Bean
    public GroupedOpenApi apiV1Group() {
        return GroupedOpenApi.builder()
                .group("v1") // Swagger UI에 표시될 그룹 이름
                .pathsToMatch("/api/v1/**") // 포함시킬 API 경로 패턴
                .build();
    }

    /**
     * Swagger 문서 정보 및 보안 설정 구성
     */
    @Bean
    public OpenAPI customOpenAPI() {

        // OpenAPI 문서 생성
        OpenAPI openAPI = new OpenAPI()
                // 문서 기본 정보 (제목, 설명, 버전)
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("API") // Swagger 문서 제목
                        .version("v1") // API 버전
                        .description("API with Bearer Auth")) // 설명

                // 전체 문서에 Bearer 인증이 필요하다고 명시
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))

                // 보안 스키마 정의 - JWT 토큰 기반 Bearer 인증
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME) // 스키마 이름
                                        .type(SecurityScheme.Type.HTTP) // HTTP 인증
                                        .scheme("Bearer") // 인증 스킴
                                        .bearerFormat("JWT"))); // 형식은 JWT

        // local 환경이 아닐 경우 Swagger 문서에 기본 서버 URL 명시
        if (!"dev".equalsIgnoreCase(activeProfile)) {
            Server server = new Server();
            server.setUrl("http://test.com"); // 운영/스테이지 서버 주소
            openAPI.addServersItem(server);
        }

        return openAPI;
    }
}