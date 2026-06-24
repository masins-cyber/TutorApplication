package tutorapplication.exception;

public class LessonAlreadyInsertedException extends Exception {
    public LessonAlreadyInsertedException(String tutorEmail) {
        super("The tutor " + tutorEmail + " has already inserted an available lesson for this specific day and time slot.");
    }
}