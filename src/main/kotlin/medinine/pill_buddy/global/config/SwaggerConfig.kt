package medinine.pill_buddy.global.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun customOpenApi(): OpenAPI {
        return OpenAPI().info(
            Info()
                .title("Pill Buddy")
                .version("1.0")
                .description("약 복용 알림 서비스 Pill buddy 입니다.")
                .contact(
                    Contact().name("데브코스 백엔드 2차 9팀 medinine").url("https://github.com/prgrms-be-devcourse/NBE2_2_Team9")
                )
        ).addSecurityItem(SecurityRequirement().addList("Bearer Authentication"))
            .components(Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()))
    }

    private fun createAPIKeyScheme(): SecurityScheme {
        return SecurityScheme().type(SecurityScheme.Type.HTTP)
            .bearerFormat("JWT")
            .scheme("bearer")
    }
}