package cl.duoc.auth_server.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
@Import(OAuth2AuthorizationServerConfiguration.class)
public class AuthorizationServerConfig {

    @Bean
    RegisteredClientRepository registeredClientRepository(
            @Value("${AUTH_WEB_CLIENT_SECRET}") String webSecret,
            @Value("${AUTH_MOVIL_CLIENT_SECRET}") String movilSecret,
            @Value("${AUTH_CAJERO_CLIENT_SECRET}") String cajeroSecret,
            @Value("${AUTH_SERVICIOS_CLIENT_SECRET}") String serviciosSecret) {

        PasswordEncoder passwordEncoder =
                PasswordEncoderFactories.createDelegatingPasswordEncoder();

        RegisteredClient webClient = RegisteredClient
                .withId(UUID.randomUUID().toString())
                .clientId("bff-web-client")
                .clientSecret(passwordEncoder.encode(webSecret))
                .clientAuthenticationMethod(
                        ClientAuthenticationMethod.CLIENT_SECRET_BASIC
                )
                .authorizationGrantType(
                        AuthorizationGrantType.CLIENT_CREDENTIALS
                )
                .scope("web.read")
                .scope("clientes.read")
                .scope("cuentas.read")
                .scope("pagos.read")
                .build();

        RegisteredClient movilClient = RegisteredClient
                .withId(UUID.randomUUID().toString())
                .clientId("bff-movil-client")
                .clientSecret(passwordEncoder.encode(movilSecret))
                .clientAuthenticationMethod(
                        ClientAuthenticationMethod.CLIENT_SECRET_BASIC
                )
                .authorizationGrantType(
                        AuthorizationGrantType.CLIENT_CREDENTIALS
                )
                .scope("movil.read")
                .scope("clientes.read")
                .scope("cuentas.read")
                .build();

        RegisteredClient cajeroClient = RegisteredClient
                .withId(UUID.randomUUID().toString())
                .clientId("bff-cajero-client")
                .clientSecret(passwordEncoder.encode(cajeroSecret))
                .clientAuthenticationMethod(
                        ClientAuthenticationMethod.CLIENT_SECRET_BASIC
                )
                .authorizationGrantType(
                        AuthorizationGrantType.CLIENT_CREDENTIALS
                )
                .scope("cajero.read")
                .scope("cajero.write")
                .scope("cuentas.read")
                .scope("cuentas.write")
                .scope("pagos.write")
                .build();

        RegisteredClient serviciosClient = RegisteredClient
                .withId(UUID.randomUUID().toString())
                .clientId("servicios-client")
                .clientSecret(passwordEncoder.encode(serviciosSecret))
                .clientAuthenticationMethod(
                        ClientAuthenticationMethod.CLIENT_SECRET_BASIC
                )
                .authorizationGrantType(
                        AuthorizationGrantType.CLIENT_CREDENTIALS
                )
                .scope("clientes.read")
                .scope("clientes.write")
                .scope("cuentas.read")
                .scope("cuentas.write")
                .scope("pagos.read")
                .scope("pagos.write")
                .build();

        return new InMemoryRegisteredClientRepository(
                List.of(
                        webClient,
                        movilClient,
                        cajeroClient,
                        serviciosClient
                )
        );
    }

    @Bean
    JWKSource<SecurityContext> jwkSource() {

        KeyPair keyPair = generarClaveRsa();

        RSAPublicKey publicKey =
                (RSAPublicKey) keyPair.getPublic();

        RSAPrivateKey privateKey =
                (RSAPrivateKey) keyPair.getPrivate();

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();

        JWKSet jwkSet = new JWKSet(rsaKey);

        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    JwtDecoder jwtDecoder(
            JWKSource<SecurityContext> jwkSource) {

        return OAuth2AuthorizationServerConfiguration
                .jwtDecoder(jwkSource);
    }

    @Bean
    AuthorizationServerSettings authorizationServerSettings() {

        return AuthorizationServerSettings.builder()
                .issuer("http://localhost:9000")
                .build();
    }

    private static KeyPair generarClaveRsa() {

        try {

            KeyPairGenerator keyPairGenerator =
                    KeyPairGenerator.getInstance("RSA");

            keyPairGenerator.initialize(2048);

            return keyPairGenerator.generateKeyPair();

        } catch (Exception e) {

            throw new IllegalStateException(
                    "No fue posible generar las claves RSA",
                    e
            );
        }
    }
}