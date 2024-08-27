package com.example.BookHealthServiceOnline.service;


import com.example.BookHealthServiceOnline.config.AppTenantContext;
import com.example.BookHealthServiceOnline.dao.AdminUserDao;
import com.example.BookHealthServiceOnline.dao.UserCategoryDao;
import com.example.BookHealthServiceOnline.dao.UserDao;
import com.example.BookHealthServiceOnline.dao.impl.UserCategoryDaoImpl;
import com.example.BookHealthServiceOnline.domain.AdminUser;
import com.example.BookHealthServiceOnline.domain.User;
import com.example.BookHealthServiceOnline.domain.UserCategory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.xml.bind.annotation.XmlType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.example.BookHealthServiceOnline.config.AppTenantContext.DEFAULT_TENANT;

@Service
public class JwtService {
    private final UserCategoryDao userCategoryDao;
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    private final AdminUserDao adminUserDao;

    private final UserDao userDao;

    public JwtService(UserCategoryDaoImpl userCategoryDao, AdminUserDao adminUserDao, UserDao userDao) {
        this.userCategoryDao = userCategoryDao;
        this.adminUserDao = adminUserDao;
        this.userDao = userDao;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractClaims(String token){
        Claims claims = extractAllClaims(token);
        return claims;
    }
    public String generateToken(User user) {

        Map<String, Object> claims = new HashMap<>();
        UserCategory userCategory=userCategoryDao.findById(user.getUserCategoryId()).orElse(null);
        if(userCategory!=null){
            claims.put("ROLE",userCategory.getCategoryName() );
        }
        return generateToken(claims, user);
    }
    public String generateToken(AdminUser adminUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("ROLE",adminUser.getUserRole());
        return generateToken(claims, adminUser);
    }

    public String generateToken(Map<String, Object> extraClaims, User user) {
        return buildToken(extraClaims, user, jwtExpiration);
    }
    public String generateToken(Map<String, Object> extraClaims, AdminUser adminUser) {
        return buildToken(extraClaims, adminUser, jwtExpiration);
    }

    public long getExpirationTime() {
        return jwtExpiration;
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            User user,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            AdminUser adminUser,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(adminUser.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public boolean isTokenValid(String token, User user) {
        final String username = extractUsername(token);
        return (username.equals(user.getUsername())) && !isTokenExpired(token);
    }
    public boolean isTokenValid(String token, AdminUser adminUser) {
        final String username = extractUsername(token);
        return (username.equals(adminUser.getUsername())) && !isTokenExpired(token);
    }
    public boolean isTokenValid(String token ){
        final String username = extractUsername(token);
        String tenant=AppTenantContext.getCurrentTenant();
        if(tenant==null || tenant.equals(DEFAULT_TENANT)){
                AdminUser adminUser = this.adminUserDao.findByUsername(username).orElse(null);
                return (username.equals(adminUser.getUsername())) && !isTokenExpired(token);
            }
        else{
            User user = this.userDao.findByUsername(username).orElse(null);
            return (username.equals(user.getUsername())) && !isTokenExpired(token);
        }
    }
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}