package cl.duoc.ms_vetcare_catalog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.DelegatingJwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    @Value("${vetcare.security.audience}")
    private String audience;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // =========================
                        // ACTUATOR PÚBLICO
                        // =========================
                        .requestMatchers(
                                "/actuator/health",
                                "/actuator/info"
                        ).permitAll()

                        // =========================
                        // CATÁLOGO - LEER
                        // Scope: Catalogo.Leer
                        // Rol: Admin u Operador
                        // =========================
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/catalog/services",
                                "/api/catalog/services/**"
                        )
                        .access((authentication, context) -> {

                            var authorities =
                                    authentication.get().getAuthorities();

                            boolean hasScope =
                                    authorities.stream()
                                            .anyMatch(authority ->
                                                    authority.getAuthority()
                                                            .equals("SCOPE_Catalogo.Leer")
                                            );

                            boolean hasAllowedRole =
                                    authorities.stream()
                                            .anyMatch(authority ->
                                                    authority.getAuthority()
                                                            .equals("ROLE_Admin")
                                                    ||
                                                    authority.getAuthority()
                                                            .equals("ROLE_Operador")
                                            );

                            return new AuthorizationDecision(
                                    hasScope && hasAllowedRole
                            );
                        })

                        // =========================
                        // CATÁLOGO - CREAR
                        // Scope: Catalogo.Escribir
                        // Rol: SOLO Admin
                        // =========================
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/catalog/services",
                                "/api/catalog/services/**"
                        )
                        .access((authentication, context) -> {

                            var authorities =
                                    authentication.get().getAuthorities();

                            boolean hasScope =
                                    authorities.stream()
                                            .anyMatch(authority ->
                                                    authority.getAuthority()
                                                            .equals("SCOPE_Catalogo.Escribir")
                                            );

                            boolean isAdmin =
                                    authorities.stream()
                                            .anyMatch(authority ->
                                                    authority.getAuthority()
                                                            .equals("ROLE_Admin")
                                            );

                            return new AuthorizationDecision(
                                    hasScope && isAdmin
                            );
                        })

                        // =========================
                        // CATÁLOGO - MODIFICAR
                        // Scope: Catalogo.Escribir
                        // Rol: SOLO Admin
                        // =========================
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/catalog/services",
                                "/api/catalog/services/**"
                        )
                        .access((authentication, context) -> {

                            var authorities =
                                    authentication.get().getAuthorities();

                            boolean hasScope =
                                    authorities.stream()
                                            .anyMatch(authority ->
                                                    authority.getAuthority()
                                                            .equals("SCOPE_Catalogo.Escribir")
                                            );

                            boolean isAdmin =
                                    authorities.stream()
                                            .anyMatch(authority ->
                                                    authority.getAuthority()
                                                            .equals("ROLE_Admin")
                                            );

                            return new AuthorizationDecision(
                                    hasScope && isAdmin
                            );
                        })

                        // =========================
                        // CATÁLOGO - ELIMINAR
                        // Scope: Catalogo.Escribir
                        // Rol: SOLO Admin
                        // =========================
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/catalog/services",
                                "/api/catalog/services/**"
                        )
                        .access((authentication, context) -> {

                            var authorities =
                                    authentication.get().getAuthorities();

                            boolean hasScope =
                                    authorities.stream()
                                            .anyMatch(authority ->
                                                    authority.getAuthority()
                                                            .equals("SCOPE_Catalogo.Escribir")
                                            );

                            boolean isAdmin =
                                    authorities.stream()
                                            .anyMatch(authority ->
                                                    authority.getAuthority()
                                                            .equals("ROLE_Admin")
                                            );

                            return new AuthorizationDecision(
                                    hasScope && isAdmin
                            );
                        })

                        // Todo lo demás exige autenticación
                        .anyRequest().authenticated()
                )

                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(
                                        jwtAuthenticationConverter()
                                )
                        )
                );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        // =========================
        // SCOPES
        // =========================
        JwtGrantedAuthoritiesConverter scopesConverter =
                new JwtGrantedAuthoritiesConverter();

        scopesConverter.setAuthoritiesClaimName("scp");
        scopesConverter.setAuthorityPrefix("SCOPE_");

        // =========================
        // ROLES
        // =========================
        JwtGrantedAuthoritiesConverter rolesConverter =
                new JwtGrantedAuthoritiesConverter();

        rolesConverter.setAuthoritiesClaimName("roles");
        rolesConverter.setAuthorityPrefix("ROLE_");

        // Combina scopes + roles
        DelegatingJwtGrantedAuthoritiesConverter authoritiesConverter =
                new DelegatingJwtGrantedAuthoritiesConverter(
                        scopesConverter,
                        rolesConverter
                );

        JwtAuthenticationConverter authenticationConverter =
                new JwtAuthenticationConverter();

        authenticationConverter.setJwtGrantedAuthoritiesConverter(
                authoritiesConverter
        );

        return authenticationConverter;
    }

    @Bean
    public JwtDecoder jwtDecoder() {

        NimbusJwtDecoder jwtDecoder =
                (NimbusJwtDecoder)
                        JwtDecoders.fromIssuerLocation(issuer);

        OAuth2TokenValidator<Jwt> issuerValidator =
                JwtValidators.createDefaultWithIssuer(issuer);

        OAuth2TokenValidator<Jwt> audienceValidator =
                token -> {

                    if (token.getAudience() != null &&
                            token.getAudience().contains(audience)) {

                        return OAuth2TokenValidatorResult.success();
                    }

                    OAuth2Error error =
                            new OAuth2Error(
                                    "invalid_token",
                                    "El token no contiene la audiencia requerida",
                                    null
                            );

                    return OAuth2TokenValidatorResult.failure(error);
                };

        jwtDecoder.setJwtValidator(
                new DelegatingOAuth2TokenValidator<>(
                        issuerValidator,
                        audienceValidator
                )
        );

        return jwtDecoder;
    }
}