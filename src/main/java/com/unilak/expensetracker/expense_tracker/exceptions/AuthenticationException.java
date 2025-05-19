package com.unilak.expensetracker.expense_tracker.exceptions;

/**git commit -m "Add AuthenticationException for handling authentication errors"
 * Exception for authentication errors
 * @author YourName
 * @reg YourRegistrationNumber
 */
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
}