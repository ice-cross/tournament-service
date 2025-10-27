package pl.ick.tournament_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.net.URL;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class JwkFetcher {

    @Value("${auth.jwks-url}")
    private String jwksUrl;

    private volatile RSAPublicKey cachedKey;
    private volatile Instant lastUpdated;

    public JwkFetcher() {
        // default constructor
    }

    public RSAPublicKey getPublicKey() {
        if (cachedKey == null) {
            log.info("JWKS key not cached yet — fetching initial key...");
            refreshKey();
        }
        return cachedKey;
    }

    @Scheduled(fixedDelayString = "${auth.jwks-refresh-interval:21600000}") // default: 6h in ms
    public void scheduledRefresh() {
        log.info("Refreshing JWKS public key...");
        refreshKey();
    }

    private synchronized void refreshKey() {
        try {
            Map<String, Object> jwks = new ObjectMapper().readValue(new URL(jwksUrl), Map.class);
            List<Map<String, String>> keys = (List<Map<String, String>>) jwks.get("keys");
            Map<String, String> key = keys.get(0);

            byte[] nBytes = Base64.getUrlDecoder().decode(key.get("n"));
            byte[] eBytes = Base64.getUrlDecoder().decode(key.get("e"));

            BigInteger modulus = new BigInteger(1, nBytes);
            BigInteger exponent = new BigInteger(1, eBytes);

            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            RSAPublicKey newKey = (RSAPublicKey) kf.generatePublic(keySpec);

            cachedKey = newKey;
            lastUpdated = Instant.now();
            log.info("Successfully refreshed JWKS key from {}", jwksUrl);
        } catch (Exception e) {
            log.warn("Failed to refresh JWKS key, using cached key (if available). Error: {}", e.getMessage());
        }
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }
}
