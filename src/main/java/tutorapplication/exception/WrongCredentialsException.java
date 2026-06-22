package tutorapplication.exception;

public class WrongCredentialsException extends Exception {
    public WrongCredentialsException() {
        super("Error: Email, password or role entered is incorrect.");
    }
}

