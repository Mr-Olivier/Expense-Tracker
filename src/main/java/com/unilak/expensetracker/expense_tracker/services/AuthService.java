////package com.unilak.expensetracker.expense_tracker.services;
////
////import com.unilak.expensetracker.expense_tracker.entities.User;
////import com.unilak.expensetracker.expense_tracker.payloads.request.LoginRequest;
////import com.unilak.expensetracker.expense_tracker.payloads.request.SignupRequest;
////import com.unilak.expensetracker.expense_tracker.payloads.response.JwtResponse;
////import com.unilak.expensetracker.expense_tracker.repositories.UserRepository;
////import com.unilak.expensetracker.expense_tracker.utils.JwtUtils;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.security.authentication.BadCredentialsException;
////import org.springframework.security.crypto.password.PasswordEncoder;
////import org.springframework.stereotype.Service;
////
////import java.time.LocalDateTime;
////
/////**
//// * Service for authentication operations
//// * @author YourName
//// * @reg YourRegistrationNumber
//// */
////@Service
////public class AuthService {
////
////    @Autowired
////    private UserRepository userRepository;
////
////    @Autowired
////    private PasswordEncoder passwordEncoder;
////
////    @Autowired
////    private JwtUtils jwtUtils;
////
////    /**
////     * Register a new user
////     * @param signupRequest User signup data
////     * @return JWT response with token
////     */
////    public JwtResponse signup(SignupRequest signupRequest) {
////        // Check if email already exists
////        if (userRepository.existsByEmail(signupRequest.getEmail())) {
////            throw new RuntimeException("Email is already in use");
////        }
////
////        // Create new user
////        User user = new User();
////        user.setEmail(signupRequest.getEmail());
////        user.setFirstName(signupRequest.getFirstName());
////        user.setLastName(signupRequest.getLastName());
////        // Encrypt the password
////        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
////        user.setPhoneNumber(signupRequest.getPhoneNumber());
////        user.setCreatedOn(LocalDateTime.now());
////        user.setStatus("active");
////
////        // Save user to database
////        User savedUser = userRepository.save(user);
////
////        // Generate JWT token
////        String token = jwtUtils.generateToken(user.getEmail());
////
////        // Return response
////        return new JwtResponse(
////                savedUser.getId(),
////                savedUser.getFirstName(),
////                savedUser.getLastName(),
////                savedUser.getEmail(),
////                token
////        );
////    }
////
////    /**
////     * Authenticate a user
////     * @param loginRequest User login data
////     * @return JWT response with token
////     */
////    public JwtResponse login(LoginRequest loginRequest) {
////        // Find user by email
////        User user = userRepository.findByEmail(loginRequest.getEmail())
////                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
////
////        // Check if user is banned
////        if ("banned".equals(user.getStatus())) {
////            throw new RuntimeException("Your account has been banned. Please contact admin.");
////        }
////
////        // Verify password
////        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
////            throw new BadCredentialsException("Invalid email or password");
////        }
////
////        // Generate JWT token
////        String token = jwtUtils.generateToken(user.getEmail());
////
////        // Return response
////        return new JwtResponse(
////                user.getId(),
////                user.getFirstName(),
////                user.getLastName(),
////                user.getEmail(),
////                token
////        );
////    }
////}
//
//
//
//package com.unilak.expensetracker.expense_tracker.services;
//
//import com.unilak.expensetracker.expense_tracker.entities.User;
//import com.unilak.expensetracker.expense_tracker.exceptions.AuthenticationException;
//import com.unilak.expensetracker.expense_tracker.payloads.request.LoginRequest;
//import com.unilak.expensetracker.expense_tracker.payloads.request.SignupRequest;
//import com.unilak.expensetracker.expense_tracker.payloads.response.JwtResponse;
//import com.unilak.expensetracker.expense_tracker.repositories.UserRepository;
//import com.unilak.expensetracker.expense_tracker.utils.JwtUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//
///**
// * Service for authentication operations
// * @author YourName
// * @reg YourRegistrationNumber
// */
//@Service
//public class AuthService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private JwtUtils jwtUtils;
//
//    /**
//     * Register a new user
//     * @param signupRequest User signup data
//     * @return JWT response with token
//     */
//    public JwtResponse signup(SignupRequest signupRequest) {
//        // Check if email already exists
//        if (userRepository.existsByEmail(signupRequest.getEmail())) {
//            throw new AuthenticationException("Email is already in use");
//        }
//
//        // Create new user
//        User user = new User();
//        user.setEmail(signupRequest.getEmail());
//        user.setFirstName(signupRequest.getFirstName());
//        user.setLastName(signupRequest.getLastName());
//        // Encrypt the password
//        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
//        user.setPhoneNumber(signupRequest.getPhoneNumber());
//        user.setCreatedOn(LocalDateTime.now());
//        user.setStatus("active");
//
//        // Save user to database
//        User savedUser = userRepository.save(user);
//
//        // Generate JWT token
//        String token = jwtUtils.generateToken(user.getEmail(), user.getRole());
//
//        // Return response
//        return new JwtResponse(
//                savedUser.getId(),
//                savedUser.getFirstName(),
//                savedUser.getLastName(),
//                savedUser.getEmail(),
//                token
//        );
//    }
//
//    /**
//     * Authenticate a user
//     * @param loginRequest User login data
//     * @return JWT response with token
//     */
//    public JwtResponse login(LoginRequest loginRequest) {
//        // Input validation
//        if (loginRequest.getEmail() == null || loginRequest.getEmail().isEmpty()) {
//            throw new AuthenticationException("Email cannot be empty");
//        }
//
//        if (loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
//            throw new AuthenticationException("Password cannot be empty");
//        }
//
//        // Find user by email
//        User user = userRepository.findByEmail(loginRequest.getEmail())
//                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
//
//        // Check if user is banned
//        if ("banned".equals(user.getStatus())) {
//            throw new AuthenticationException("Your account has been banned. Please contact admin.");
//        }
//
//        // Verify password
//        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
//            throw new BadCredentialsException("Invalid email or password");
//        }
//
//        // Generate JWT token
//        String token = jwtUtils.generateToken(user.getEmail(), user.getRole());
//
//        // Return response
//        return new JwtResponse(
//                user.getId(),
//                user.getFirstName(),
//                user.getLastName(),
//                user.getEmail(),
//                token
//        );
//    }
//}



package com.unilak.expensetracker.expense_tracker.services;

import com.unilak.expensetracker.expense_tracker.entities.User;
import com.unilak.expensetracker.expense_tracker.exceptions.ResourceNotFoundException;
import com.unilak.expensetracker.expense_tracker.payloads.request.LoginRequest;
import com.unilak.expensetracker.expense_tracker.payloads.request.SignupRequest;
import com.unilak.expensetracker.expense_tracker.payloads.response.JwtResponse;
import com.unilak.expensetracker.expense_tracker.repositories.UserRepository;
import com.unilak.expensetracker.expense_tracker.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * Service for authentication operations
 * @author YourName
 * @reg YourRegistrationNumber
 */
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    // Email validation pattern
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /**
     * Register a new user
     * @param signupRequest User signup data
     * @return JWT response with token
     */
    public JwtResponse signup(SignupRequest signupRequest) {
        // Validate email format
        if (!EMAIL_PATTERN.matcher(signupRequest.getEmail()).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }

        // Validate password length
        if (signupRequest.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }

        // Validate required fields
        if (signupRequest.getFirstName() == null || signupRequest.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }

        if (signupRequest.getLastName() == null || signupRequest.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }

        // Create new user
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        // Encrypt the password
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setPhoneNumber(signupRequest.getPhoneNumber());
        user.setCreatedOn(LocalDateTime.now());
        user.setStatus("active");
        user.setRole("USER");  // Default role is USER

        // Save user to database
        User savedUser = userRepository.save(user);

        // Generate JWT token
        String token = jwtUtils.generateToken(user.getEmail(), user.getRole());

        // Return response
        return new JwtResponse(
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                token
        );
    }

    /**
     * Authenticate a user
     * @param loginRequest User login data
     * @return JWT response with token
     */
    public JwtResponse login(LoginRequest loginRequest) {
        // Validate email format
        if (!EMAIL_PATTERN.matcher(loginRequest.getEmail()).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }

        // Find user by email
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        // Check if user is banned
        if ("banned".equals(user.getStatus())) {
            throw new IllegalArgumentException("Your account has been banned. Please contact admin.");
        }

        // Verify password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        // Generate JWT token
        String token = jwtUtils.generateToken(user.getEmail(), user.getRole());

        // Return response
        return new JwtResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                token
        );
    }
}