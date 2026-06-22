package tutorapplication.exception;

public class InvalidEmailException extends Exception {
    public InvalidEmailException(String email) {
        super("Error: The email format '" + email + "' is invalid! Make sure it contains an '@' and a domain (e.g. .com, .it).");
    }
}
