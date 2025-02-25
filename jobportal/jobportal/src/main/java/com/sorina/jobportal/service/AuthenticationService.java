package com.sorina.jobportal.service;


import com.sorina.jobportal.exception.ExpiredTokenException;
import com.sorina.jobportal.exception.InvalidTokenException;
import com.sorina.jobportal.exception.UsernameAlreadyExistsException;
import com.sorina.jobportal.model.*;
import com.sorina.jobportal.repository.JobSeekerProfileRepository;
import com.sorina.jobportal.repository.RecruiterProfileRepository;
import com.sorina.jobportal.repository.TokenRepository;
import com.sorina.jobportal.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final TokenRepository tokenRepository;

    private final JobSeekerProfileRepository jobSeekerProfileRepository;

    private final RecruiterProfileRepository recruiterProfileRepository;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, TokenRepository tokenRepository, JobSeekerProfileRepository jobSeekerProfileRepository, RecruiterProfileRepository recruiterProfileRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
    }

    public AuthenticationResponse register(User request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("A user with the username '" + request.getUsername() + "' already exists.");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(request.getRole());

        user = userRepository.save(user);

        // Create Profile based on role
        createUserProfile(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // save the generated token
        saveUserToken(accessToken, refreshToken, user);

        return new AuthenticationResponse(accessToken, refreshToken);

    }

    public AuthenticationResponse authenticate(User request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + request.getUsername()));

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Bad credentials " + request.getUsername());
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllTokenByUser(user);
        saveUserToken(accessToken, refreshToken, user);

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    public ResponseEntity<Object> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidTokenException("Missing or invalid authorization header.");
        }

        String token = authHeader.substring(7);

        try {
            String username = jwtService.extractUsername(token);

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

            if (!jwtService.isValidRefreshToken(token, user)) {
                throw new InvalidTokenException("Refresh token is invalid or expired.");
            }

            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            revokeAllTokenByUser(user);
            saveUserToken(accessToken, refreshToken, user);

            return ResponseEntity.ok(new AuthenticationResponse(accessToken, refreshToken));
        } catch (ExpiredTokenException e) {
            throw new ExpiredTokenException("Your session has expired. Please log in again.");
        } catch (Exception e) {
            throw new InvalidTokenException("An error occurred during token processing: " + e.getMessage());
        }
    }

    public void createUserProfile(User user) {
        switch (user.getRole()) {
            case JOB_SEEKER -> {
                JobSeekerProfile jobSeekerProfile = new JobSeekerProfile();
                jobSeekerProfile.setUser(user); // Link profile to user
                jobSeekerProfile.setFirstName(user.getFirstName());
                jobSeekerProfile.setLastName(user.getLastName());
                jobSeekerProfileRepository.save(jobSeekerProfile); // Save profile
            }
            case RECRUITER -> {
                RecruiterProfile recruiterProfile = new RecruiterProfile();
                recruiterProfile.setUser(user);
                recruiterProfile.setFirstName(user.getFirstName());
                recruiterProfile.setLastName(user.getLastName());
                recruiterProfileRepository.save(recruiterProfile);
            }
            default -> throw new IllegalArgumentException("Unsupported role: " + user.getRole());
        }
    }

    private void revokeAllTokenByUser(User user) {
        List<Token> validTokenListByUser = tokenRepository.findAllAccessTokenByUser(user.getId());

        if (!validTokenListByUser.isEmpty()) {
            validTokenListByUser.forEach(t -> {
                t.setLoggedOut(true);
            });
        }

        tokenRepository.saveAll(validTokenListByUser);
    }

    private void saveUserToken(String accessToken, String refreshToken, User user) {
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }


}

