package br.com.example.buyfood.config

import org.keycloak.adapters.KeycloakConfigResolver
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver
import org.keycloak.adapters.springsecurity.KeycloakConfiguration
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticatedActionsFilter
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter
import org.keycloak.adapters.springsecurity.filter.KeycloakSecurityContextRequestFilter
import org.keycloak.adapters.springsecurity.management.HttpSessionManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy

@KeycloakConfiguration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig(val swaggerConfig: SwaggerConfig) : KeycloakWebSecurityConfigurerAdapter() {

  @Throws(Exception::class)
  override fun configure(http: HttpSecurity) {
    super.configure(http)
    http.cors()
      .and()
      .csrf()
      .disable()
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      .authorizeRequests()
      .antMatchers(*swaggerConfig.swaggerAuthWhiteList())
      .permitAll()
      .antMatchers("/index.html")
      .permitAll()
      .antMatchers("/actuator/health")
      .permitAll()
      .antMatchers("/api/v1/users/create")
      .permitAll()
      .antMatchers("/api/v1/users/signin")
      .permitAll()
      .antMatchers(
        "/api/v1/establishments/{establishmentId}/products/{productId}/images/download-file/**"
      )
      .permitAll()
      .antMatchers("/api/v1/establishments/{establishmentId}/images/download-file/**")
      .permitAll()
      .antMatchers("/api/v1/admin/**")
      .hasRole(PROFILE_ADMIN)
      .antMatchers("/api/v1/users/**")
      .hasAnyRole(PROFILE_USER, PROFILE_ESTABLISHMENT, PROFILE_ADMIN)
      .antMatchers("/api/v1/establishments/**")
      .hasAnyRole(PROFILE_USER, PROFILE_ESTABLISHMENT, PROFILE_ADMIN)
      .anyRequest()
      .authenticated()
  }

  override fun sessionAuthenticationStrategy(): SessionAuthenticationStrategy {
    /** Returning NullAuthenticatedSessionStrategy means app will not remember session  */
    return NullAuthenticatedSessionStrategy()
  }

  @Autowired
  fun configureGlobal(auth: AuthenticationManagerBuilder) {
    val grantedAuthorityMapper = SimpleAuthorityMapper()
    grantedAuthorityMapper.setPrefix(AUTHORITY_MAPPER_PREFIX)
    grantedAuthorityMapper.setConvertToUpperCase(true)
    val keycloakAuthenticationProvider = keycloakAuthenticationProvider()
    keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(grantedAuthorityMapper)
    auth.authenticationProvider(keycloakAuthenticationProvider)
  }

  @Bean
  fun keycloakAuthenticationProcessingFilterRegistrationBean(
    filter: KeycloakAuthenticationProcessingFilter
  ): FilterRegistrationBean<KeycloakAuthenticationProcessingFilter> {
    val registrationBean = FilterRegistrationBean(filter)
    registrationBean.isEnabled = false
    return registrationBean
  }

  @Bean
  fun keycloakPreAuthActionsFilterRegistrationBean(
    filter: KeycloakPreAuthActionsFilter
  ): FilterRegistrationBean<KeycloakPreAuthActionsFilter> {
    val registrationBean = FilterRegistrationBean(filter)
    registrationBean.isEnabled = false
    return registrationBean
  }

  @Bean
  fun keycloakAuthenticatedActionsFilterBean(
    filter: KeycloakAuthenticatedActionsFilter
  ): FilterRegistrationBean<KeycloakAuthenticatedActionsFilter> {
    val registrationBean = FilterRegistrationBean(filter)
    registrationBean.isEnabled = false
    return registrationBean
  }

  @Bean
  fun keycloakSecurityContextRequestFilterBean(
    filter: KeycloakSecurityContextRequestFilter
  ): FilterRegistrationBean<KeycloakSecurityContextRequestFilter> {
    val registrationBean = FilterRegistrationBean(filter)
    registrationBean.isEnabled = false
    return registrationBean
  }

  @Bean
  fun KeycloakConfigResolver(): KeycloakConfigResolver {
    return KeycloakSpringBootConfigResolver()
  }

  @Bean
  @ConditionalOnMissingBean(HttpSessionManager::class)
  override fun httpSessionManager(): HttpSessionManager {
    return HttpSessionManager()
  }

  companion object {
    private const val AUTHORITY_MAPPER_PREFIX = "ROLE_"
    private const val PROFILE_ADMIN = "ADMIN"
    private const val PROFILE_ESTABLISHMENT = "ESTABLISHMENT"
    private const val PROFILE_USER = "USER"
  }
}
