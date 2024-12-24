package com.asejnr.tradingplatform.controller;

import com.asejnr.tradingplatform.config.JwtProvider;
import com.asejnr.tradingplatform.modal.User;
import com.asejnr.tradingplatform.repository.UserRepository;
import com.asejnr.tradingplatform.response.AuthResponse;
import com.asejnr.tradingplatform.service.CustomerUserDetailsService;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) throws Exception {
        log.info("User Data: {}", user);

        User emailExists = userRepository.findByEmail(user.getEmail());
        if (emailExists != null) {
            try {
                log.error("Email Already Exists");
                throw new Exception("Email already in use with another account.");
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }
        User newUser = new User();

        newUser.setFullName(user.getFullName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setPhone(user.getPhone());

        User savedUser = userRepository.save(newUser);

        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwtToken = JwtProvider.generateToken(auth);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwtToken(jwtToken);
        authResponse.setStatus(true);
        authResponse.setMessage("Registered successfully");

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) throws Exception {
        log.info("User Data: {}", user);

        String username = user.getEmail();
        String password = user.getPassword();

        Authentication auth = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwtToken = JwtProvider.generateToken(auth);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwtToken(jwtToken);
        authResponse.setStatus(true);
        authResponse.setMessage("Logged in successfully");

        return ResponseEntity.ok(authResponse);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customerUserDetailsService.loadUserByUsername(username);

        if (userDetails==null) {
            log.error("User not found");
            throw new BadCredentialsException("Invalid username or password.");
        }
        if (!password.equals(userDetails.getPassword())) {
            log.error("Passwords do not match");
            throw new BadCredentialsException("Passwords do not match.");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }
}
