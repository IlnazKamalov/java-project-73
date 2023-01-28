package hexlet.code.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static io.jsonwebtoken.impl.TextCodec.BASE64;

import io.jsonwebtoken.Clock;

import java.util.Date;
import java.util.Map;

@Component
public class JWTHelper {

    private final String secretKey;
    private final String issuer;
    private final long expirationSec;
    private final long clockSkewSec;
    private final Clock clock;

    public JWTHelper(@Value("${jwt.issuer:task-manager}") final String issuer,
                     @Value("${jwt.expiration-sec:86400}") final long expirationSec,
                     @Value("${jwt.clock-skew-sec:300}") final long clockSkewSec,
                     @Value("${jwt.secret:secret}") final String secretKey) {

        this.issuer = issuer;
        this.expirationSec = expirationSec;
        this.clockSkewSec = clockSkewSec;
        this.secretKey = BASE64.encode(secretKey);
        this.clock = DefaultClock.INSTANCE;
    }

    public final String expiring(final Map<String, Object> attributes) {

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setClaims(getClaims(attributes, expirationSec))
                .compact();
    }

    public final Map<String, Object> verify(final String token) {

        return Jwts.parser()
                .requireIssuer(issuer)
                .setClock(clock)
                .setAllowedClockSkewSeconds(clockSkewSec)
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    private Claims getClaims(final Map<String, Object> attributes,
                             final long expirationSec) {

        final Claims claims = Jwts.claims();
        claims.setIssuer(issuer);
        claims.setIssuedAt(clock.now());
        claims.putAll(attributes);

        if (expirationSec > 0) {
            claims.setExpiration(new Date(System.currentTimeMillis() + expirationSec * 1000));
        }

        return claims;
    }
}
