package cl.duoc.ms_clientes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/clientes/**"
                        )
                        .hasAuthority("SCOPE_clientes.read")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/clientes/**"
                        )
                        .hasAuthority("SCOPE_clientes.write")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/clientes/**"
                        )
                        .hasAuthority("SCOPE_clientes.write")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/clientes/**"
                        )
                        .hasAuthority("SCOPE_clientes.write")

                        .anyRequest()
                        .authenticated()
                )

                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(Customizer.withDefaults())
                );

        return http.build();
    }
}