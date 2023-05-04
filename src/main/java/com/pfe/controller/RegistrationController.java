package com.pfe.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pfe.entity.Role;
import com.pfe.entity.User;
import com.pfe.models.RegistrationRequest;
import com.pfe.models.RestaurantRegistrationRequest;
import com.pfe.service.UserService;
import com.pfe.service.impl.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/")
@AllArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;
    private final UserService userService;


    @PostMapping("restaurant/signup")
    public ResponseEntity<String> RestaurantRegister(@RequestBody RestaurantRegistrationRequest request) {
        String role = "ROLE_Manager";
        return ResponseEntity.ok(registrationService.restaurantRegister(request, role));
    }


    @PostMapping("signup")
    public ResponseEntity<String> register(@RequestBody RegistrationRequest request) {
        String role = "ROLE_USER";
        return ResponseEntity.ok(registrationService.register(request, role));
    }

    @GetMapping("confirm")
    public String confirm(@RequestParam("registrationToken") String registrationToken) {
        return registrationService.confirmToken(registrationToken);
    }
    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("to9JnV7gUIfEK3DWjIE0YfyX1w0fPKe9YdG9XDgFaj9KaAwh8Y8V3uGVQuYt42qQ4WQRoUko3hY4FibE".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String email = decodedJWT.getSubject();
                User user = userService.getUser(email);


//                create an other token and send it
                String access_token = JWT.create()
                        .withSubject(user.getEmail())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURI().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);


//        response.setHeader("access_token", access_token);
//        response.setHeader("refresh_token", refresh_token);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
//                end

            } catch (Exception e) {

                response.setHeader("error", e.getMessage());
//                    response.sendError(FORBIDDEN.value());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }

    }
}
