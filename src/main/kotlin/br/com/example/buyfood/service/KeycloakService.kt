package br.com.example.buyfood.service

import org.keycloak.KeycloakPrincipal
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.keycloak.representations.idm.CredentialRepresentation
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import java.security.Principal

class KeycloakService {
  fun getKeycloakClaims(): Map<String, Any>? {
    if (SecurityContextHolder.getContext().authentication is AnonymousAuthenticationToken) {
      return null
    }

    val authentication = SecurityContextHolder.getContext().authentication as KeycloakAuthenticationToken
    val principal = authentication.principal as Principal
    val keycloakPrincipal = principal as KeycloakPrincipal<*>
    val token = keycloakPrincipal.keycloakSecurityContext.token
    return token.otherClaims
  }

  fun getCredentialRepresentation(password: String?): CredentialRepresentation {
    val passwordCred = CredentialRepresentation()
    return passwordCred.apply { isTemporary = false; type = CredentialRepresentation.PASSWORD; value = password }
  }
}
