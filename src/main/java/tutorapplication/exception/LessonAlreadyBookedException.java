package tutorapplication.exception;

public class LessonAlreadyBookedException extends Exception {
    public LessonAlreadyBookedException(int id) {
        super("Error: The lesson #" + id + " has already been booked by another student!");
    }
}
