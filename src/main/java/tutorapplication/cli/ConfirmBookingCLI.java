package tutorapplication.cli;

import tutorapplication.bean.BookingBean;
import tutorapplication.controller.BookingController;
import tutorapplication.exception.LessonAlreadyBookedException;
import tutorapplication.model.Lesson;
import tutorapplication.others.Print;
import tutorapplication.pattern.AbstractState;
import tutorapplication.pattern.StateMachine;

import java.util.Scanner;

public class ConfirmBookingCLI extends AbstractState {
    private final int id;
    private final Scanner scanner = new Scanner(System.in);
    private final String studentEmail;
    private final BookingController controller;
    private final Lesson lesson;

    public ConfirmBookingCLI(StateMachine stateMachine, int id,  String studentEmail) {
        super(stateMachine);
        this.id = id;
        this.studentEmail = studentEmail;
        this.controller = new BookingController();
        this.lesson = controller.getLessonDetails(id);
    }

    @Override
    public void display() {
        printHeader("Confirm Booking");

        if(this.lesson == null) {
            Print.println("[ERROR] The requested lesson does not exist or has been removed.");
            stateMachine.setState(new StudentHomeCLI(stateMachine, this.studentEmail));
            return;
        }

        Print.println("Here is the summary of the lesson you selected:");
        Print.println("-------------------------------------------------");
        Print.println("Lesson Id : #" + lesson.getId());
        Print.println("Subject   : " + lesson.getSubject().toUpperCase());
        Print.println("Day     : " + lesson.getDate().toUpperCase());
        Print.println("Time     : " + lesson.getTime());
        Print.println("Price    : " + lesson.getPrice() + "€");
        Print.println("Tutor's email    : " + lesson.getTutorEmail());
        Print.println("-------------------------------------------------");

        Print.print("Do you want to definitively confirm your booking? (yes/no): ");

        String choice = scanner.nextLine().trim().toLowerCase();
        if (choice.equals("yes") || choice.equals("y")) {

            BookingBean bookingBean = new BookingBean();
            bookingBean.setId(this.id);
            bookingBean.setStudentEmail(this.studentEmail);

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
        stateMachine.setState(new StudentHomeCLI(stateMachine, this.studentEmail));
    }
}

