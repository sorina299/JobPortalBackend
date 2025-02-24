package com.sorina.jobportal.service;


import com.sorina.jobportal.exception.UsernameAlreadyExistsException;
import com.sorina.jobportal.model.*;
import com.sorina.jobportal.repository.JobSeekerProfileRepository;
import com.sorina.jobportal.repository.RecruiterProfileRepository;
import com.sorina.jobportal.repository.TokenRepository;
import com.sorina.jobportal.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

    public AuthenticationResponse register(User request){

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("An user with the exact username already exists.");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(request.getRole());

        user = userRepository.save(user);

        // Call the profile creation method
        createUserProfile(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // save the generated token
        saveUserToken(accessToken, refreshToken, user);

        return new AuthenticationResponse(accessToken, refreshToken);

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

    public AuthenticationResponse authenticate(User request){
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("There is no user registered with the username: " + request.getUsername()));

        try {
            // Now that the user exists, authenticate the password
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException ex) {
            // Since the user exists, a BadCredentialsException at this point means the password is incorrect
            throw new BadCredentialsException("Incorrect password for username: " + request.getUsername());
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllTokenByUser(user);

        saveUserToken(accessToken, refreshToken, user);

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    private void revokeAllTokenByUser(User user) {
        List<Token> validTokenListByUser = tokenRepository.findAllAccessTokenByUser(user.getId());

        if(!validTokenListByUser.isEmpty()){
            validTokenListByUser.forEach(t->{
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

    public ResponseEntity<Object> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        // Extract the token from the authorization header
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Missing or invalid authorization header.");
        }

        String token = authHeader.substring(7);

        try {
            // Extract username from token
            String username = jwtService.extractUsername(token);

            // Check if the user exists in the database
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("No available user was found."));

            // Validate the refresh token
            if (jwtService.isValidRefreshToken(token, user)) {
                // Generate a new access token
                String accessToken = jwtService.generateAccessToken(user);
                String refreshToken = jwtService.generateRefreshToken(user);

                revokeAllTokenByUser(user);

                saveUserToken(accessToken, refreshToken, user);

                return ResponseEntity.ok(new AuthenticationResponse(accessToken, refreshToken));
            } else {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Refresh token is invalid or expired.");
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("An error occurred during token processing: " + e.getMessage());
        }
    }


}

