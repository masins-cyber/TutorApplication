package tutorapplication.cli;

import tutorapplication.bean.BookingBean;
import tutorapplication.bean.LessonBean;
import tutorapplication.controller.BookingController;
import tutorapplication.exception.LessonAlreadyBookedException;
import tutorapplication.others.Print;
import tutorapplication.pattern.AbstractState;
import tutorapplication.pattern.StateMachineImpl;

import java.util.Scanner;

public class ConfirmBookingCLI extends AbstractState {
    private final int lessonId;
    private final BookingController controller;
    private final LessonBean lesson;

    public ConfirmBookingCLI(int lessonId) {
        super();
        this.lessonId = lessonId;
        this.controller = new BookingController();
        this.lesson = controller.getLessonDetails(lessonId);
    }

    @Override
    public void action(StateMachineImpl context) {
        if (this.lesson == null) {
            Print.println("[ERROR] The requested lesson does not exist or has been removed.");
            goBack(context);
            return;
        }

        display();

        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine().trim().toLowerCase();

        if (choice.equals("yes") || choice.equals("y")) {
            BookingBean bookingBean = new BookingBean();
            bookingBean.setId(this.lessonId);

            String studentEmail = context.getSessionUser().getEmail();
            bookingBean.setStudentEmail(studentEmail);

            try {
                int bookingId = controller.bookLesson(bookingBean);

                if (bookingId != -1) {
                    Print.println("\n================================");
                    Print.println("[RECEIPT] BOOKING CONFIRMED WITH ID: #" + bookingId);
                    Print.println("Current booking status: 'booked'");
                    Print.println("================================");
                }
                else {
                    Print.println("\n[CRITICAL ERROR] Error saving the reservation in the database.");
                }
            }
            catch (LessonAlreadyBookedException e) {
                Print.println("\n=================================================");
                Print.println("[BOOKING FAILED] " + e.getMessage());
                Print.println("=================================================");
            }
        }
        else {
            Print.println("\n[ADVICE] Reservation canceled by student.");
        }
        goBack(context);
    }

    @Override
    public void display() {
        printHeader("Confirm Booking");
        Print.println("Here is the summary of the lesson you selected:");
        Print.println("-------------------------------------------------");
        Print.println("Lesson Id : #" + lesson.getId());
        Print.println("Subject   : " + lesson.getSubject());
        Print.println("Tutor     : " + lesson.getTutorEmail());
        Print.println("Day       : " + lesson.getDay() + " | Time: " + lesson.getTimeSlot());
        Print.println("Price     : " + lesson.getMaxPrice() + "€");
        Print.println("-------------------------------------------------");
        Print.print("Do you want to definitively confirm your booking? (yes/no): ");
    }
}

