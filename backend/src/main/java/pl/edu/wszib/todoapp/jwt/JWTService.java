package pl.edu.wszib.todoapp.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HexFormat;
import java.util.Map;

@Service
public class JWTService {
    @Value("${jwt.secret}")
    private String secretKey;

    public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
        String username = getLogin(jwtToken);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken);
    }

    public boolean isTokenExpired(String jwtToken) {
        Date expiration = getAllClaims(jwtToken).getExpiration();
        return expiration.before(new Date());
    }

    public String getLogin(String jwtToken) {
       return getAllClaims(jwtToken).getSubject();
    }

    public String generateToken(Map<String, Object> additionalClaims, UserDetails userDetails) {
        final Date currentDate = new Date();

        return Jwts
                .builder()
                .claims(additionalClaims)
                .subject(userDetails.getUsername())
                .issuedAt(currentDate)
                .expiration(new Date(currentDate.getTime() + 86400000 * 7))
                .signWith(getSecureKey())
                .compact();
    }

    private Claims getAllClaims(String jwtToken) {
        return Jwts
                .parser()
                .verifyWith(getSecureKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    private SecretKey getSecureKey() {
        return Keys.hmacShaKeyFor(
                HexFormat.of().parseHex(secretKey)
        );
    }
}
