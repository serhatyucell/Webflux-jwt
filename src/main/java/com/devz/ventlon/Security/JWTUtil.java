package com.devz.ventlon.Security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import reactor.core.publisher.Mono;
import java.util.Date;

public class JWTUtil {

    private static final String SECRET_KEY = "JSOeFwdiebXNtLkuhv4w99ONRs8bLegFhnz0386hB2EDl8dgqCvYl2DmH29e0z3llUPLvLfP8jCQgrmhFILQFqUrNmwqt0j";
    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
    private static final JWTVerifier verifier = JWT.require(algorithm).build();
    private static final Long expirationTime = 3600000L; // 3600 saniye (1 saat)

    public static Mono<String> createJwt(String subject) {
        return Mono.fromCallable(() -> {

            return JWT.create()
                    .withIssuer("Serhat")
                    .withSubject(subject)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                    .sign(Algorithm.HMAC256(SECRET_KEY));

        });
    }
    public static Mono<String> validateJwt(String jwt) {
        return Mono.fromCallable(() -> {
            try {
                DecodedJWT decodedJWT = verifier.verify(jwt);
                return decodedJWT.getSubject();
            }catch (Exception e) {
                //Süresi geçmiş olabilir, doğru bir jwt olmayabilir.
                throw new RuntimeException("JWT doğrulanamadı.");
            }
        });
    }
}
