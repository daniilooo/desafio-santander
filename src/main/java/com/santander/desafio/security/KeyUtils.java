package com.santander.desafio.security;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.stream.Collectors;

public class KeyUtils {
    private static String readPemResource(String path) {
        try (InputStream is = KeyUtils.class.getResourceAsStream(path);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            return br.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao ler PEM: " + path, e);
        }
    }

    public static RSAPrivateKey loadPrivateKey(String classpathPem) {
        try {
            String pem = readPemResource(classpathPem)
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\n", "");
            byte[] decoded = Base64.getDecoder().decode(pem);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey pk = kf.generatePrivate(spec);
            return (RSAPrivateKey) pk;
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao carregar chave privada", e);
        }
    }

    public static RSAPublicKey loadPublicKey(String classpathPem) {
        try {
            String pem = readPemResource(classpathPem)
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\n", "");
            byte[] decoded = Base64.getDecoder().decode(pem);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey pk = kf.generatePublic(spec);
            return (RSAPublicKey) pk;
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao carregar chave pública", e);
        }
    }
}
