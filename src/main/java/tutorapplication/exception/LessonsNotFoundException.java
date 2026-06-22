package tutorapplication.exception;

public class LessonsNotFoundException extends Exception {
    public LessonsNotFoundException() {
        super("No available lessons match the search criteria entered.");
    }
}
