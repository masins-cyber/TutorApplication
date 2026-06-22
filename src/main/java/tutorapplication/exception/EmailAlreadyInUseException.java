package tutorapplication.exception;

public class EmailAlreadyInUseException extends Exception {
    public EmailAlreadyInUseException (String email) {
        super("Error: The email address '" + email + "' is already associated with an account.");
    }
}

