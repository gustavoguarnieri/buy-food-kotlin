package br.com.example.buyfood.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig {
  @Value("\${swagger.enabled}")
  val isSwaggerEnabled = false

  @Bean
  fun api(): Docket {
    return Docket(DocumentationType.SWAGGER_2)
      .enable(isSwaggerEnabled)
      .select()
      .apis(RequestHandlerSelectors.basePackage("br.com.example.buyfood.controller"))
      .paths(PathSelectors.any())
      .build()
      .useDefaultResponseMessages(false)
      .apiInfo(apiInfo())
  }

  private fun apiInfo(): ApiInfo {
    return ApiInfoBuilder()
      .title("Buy-Food Rest Api Documentation")
      .description("All necessary information is in this documentation")
      .version("1.0.0")
      .license("Apache License Version 2.0")
      .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
      .contact(
        Contact(
          "Gustavo",
          "https://www.linkedin.com/in/gustavo-guarnieri/",
          "gustavo.guarnieri@gmail.com"
        )
      )
      .build()
  }

  fun swaggerAuthWhiteList(): Array<String> {
    return if (isSwaggerEnabled) {
      arrayOf(
        "/v2/api-docs",
        "/swagger-resources",
        "/swagger-resources/**",
        "/configuration/ui",
        "/configuration/security",
        "/swagger-ui/**",
        "/webjars/**"
      )
    } else {
      arrayOf()
    }
  }
}
