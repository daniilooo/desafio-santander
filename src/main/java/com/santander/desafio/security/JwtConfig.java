package com.santander.desafio.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.*;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class JwtConfig {

    @Bean
    public JwtEncoder jwtEncoder() {
        RSAPublicKey publicKey = KeyUtils.loadPublicKey("/keypair/public.pem");
        RSAPrivateKey privateKey = KeyUtils.loadPrivateKey("/keypair/private.pem");
        JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        JWKSource<SecurityContext> jwkSource = (jwkSelector, securityContext) -> jwkSelector.select(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        RSAPublicKey publicKey = KeyUtils.loadPublicKey("/keypair/public.pem");
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }
}
