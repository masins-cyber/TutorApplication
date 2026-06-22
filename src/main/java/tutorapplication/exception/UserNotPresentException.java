package tutorapplication.exception;

public class UserNotPresentException extends Exception {
    public UserNotPresentException(String email) {
        super("Error: The user with email '" + email + "' is not present in the system.");
    }
}

