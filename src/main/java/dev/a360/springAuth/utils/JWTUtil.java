package dev.a360.springAuth.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil implements Serializable, EnvironmentAware {
    public static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60;
    private String secret;
    private final String ISSUER = "dev.a360.spring-auth";

    public String getUserFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    private Claims getAllClaimsFromToken(String token) {
        System.out.println("JWT Secret Claim: "+secret);
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getAllClaimsFromToken(token).getExpiration();
        return expiration.before(new Date());
    }


    public String generateToken(String subject) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Issuer", ISSUER);
        System.out.println("JWT Secret: "+secret);

        return Jwts.builder().setClaims(claims).setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public Boolean validateToken(String token) {
        if(!isTokenExpired(token)){
            return getAllClaimsFromToken(token).get("Issuer").equals(ISSUER);
        }

        return false;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.secret = environment.getProperty("JWT_SECRET");
    }
}
