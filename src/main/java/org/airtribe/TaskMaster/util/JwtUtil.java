package org.airtribe.TaskMaster.util;
import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {

    public static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String generateToken(String userName, Long userId) {
        return Jwts.builder().setSubject(userName)
        .setExpiration(new Date(System.currentTimeMillis() + 8 * 60 * 1000)) 
        .setIssuedAt(new Date())
        .claim("userId", userId)
        .signWith(SECRET_KEY)
        .compact();
    }   

    public static boolean validateJwtToken(String authenticationHeader) {
        @SuppressWarnings("deprecation")
        Claims claims = Jwts.parser()
        .setSigningKey(SECRET_KEY)
        .parseClaimsJws(authenticationHeader)
        .getBody();
        return claims.getExpiration().after(new Date());
    }

    public static Long getUserIdFromToken(String token) {
    @SuppressWarnings("deprecation")
    Claims claims = Jwts.parser()
        .setSigningKey(SECRET_KEY)
        .parseClaimsJws(token)
        .getBody();
    return Long.parseLong(claims.get("userId").toString());
}

public static Date getExpiryFromToken(String token) {
    @SuppressWarnings("deprecation")
    Claims claims = Jwts.parser()
        .setSigningKey(SECRET_KEY)
        .parseClaimsJws(token)
        .getBody();
    return claims.getExpiration();
}
}


