package com.ssafy.alttab.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI 설정 클래스
 * Swagger를 사용하여 API 명세서를 자동으로 생성하며, API 보안 설정을 정의
 */
@OpenAPIDefinition(
        info = @Info(
                title = "alttab api 명세서",
                description = "<h3>alttab API Reference for Developers</h3>",
                version = "v1",
                contact = @Contact(
                        name = "ji jong kwon",
                        email = "help3451@naver.com",
                        url = "https://i11a309.p.ssafy.io/"
                )
        ),
        security = @SecurityRequirement(name = "bearerAuth")
)

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "Authorization"
)

public class SwaggerConfiguration {

}