package com.example.transpro.rest;

import com.example.transpro.model.User;
import com.example.transpro.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Value("${jwt.secret:SekretnyKluczJWT1234567890123456}")
    private String jwtSecret;

    @Value("${jwt.expiration:3600000}") // np. 1h
    private long jwtExpiration;

    public AuthRestController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Niepoprawne dane logowania");
        }

        User user = userService.findByUsername(loginRequest.getUsername());
        if (user == null) {
            return ResponseEntity.status(404).body("Użytkownik nie znaleziony");
        }

        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();

        // Ustawiamy ciasteczko - HttpOnly uniemożliwia dostęp JS do tokenu, co jest bezpieczniejsze.
        ResponseCookie jwtCookie = ResponseCookie.from("JWT_TOKEN", token)
                .httpOnly(true)        // Chroni przed odczytem w JS
                .secure(true)         // Ustaw true jeśli używasz HTTPS
                .path("/")
                .maxAge(jwtExpiration/1000)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", jwtCookie.toString())
                .body(new LoginResponse(token, user.getUsername()));
    }


    // Dto do przyjęcia danych logowania
    static class LoginRequest {
        private String username;
        private String password;
        // get/set
        public String getUsername() {return username;}
        public void setUsername(String username) {this.username = username;}
        public String getPassword() {return password;}
        public void setPassword(String password) {this.password = password;}
    }

    // Dto do odpowiedzi z tokenem
    static class LoginResponse {
        private String token;
        private String username;
        public LoginResponse(String token, String username) {
            this.token = token;
            this.username = username;
        }
        public String getToken() {return token;}
        public String getUsername() {return username;}
    }
}
