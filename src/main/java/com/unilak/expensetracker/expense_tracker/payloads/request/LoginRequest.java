//package com.unilak.expensetracker.expense_tracker.payloads.request;
//
//import jakarta.validation.constraints.NotBlank;
//import lombok.Data;
//
///**
// * DTO for user login request
// * @author YourName
// * @reg YourRegistrationNumber
// */
//@Data
//public class LoginRequest {
//
//    @NotBlank(message = "Email is required")
//    private String email;
//
//    @NotBlank(message = "Password is required")
//    private String password;
//}


package com.unilak.expensetracker.expense_tracker.payloads.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for user login request
 * @author YourName
 * @reg YourRegistrationNumber
 */
@Data
public class LoginRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}