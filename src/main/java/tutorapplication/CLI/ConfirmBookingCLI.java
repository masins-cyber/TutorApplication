package tutorapplication.CLI;

import tutorapplication.bean.BookingBean;
import tutorapplication.controller.BookingController;
import tutorapplication.exception.LessonAlreadyBookedException;
import tutorapplication.model.Lesson;
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
            System.out.println("[ERROR] The requested lesson does not exist or has been removed.");
            stateMachine.setState(new StudentHomeCLI(stateMachine, this.studentEmail));
            return;
        }

        System.out.println("Here is the summary of the lesson you selected:");
        System.out.println("-------------------------------------------------");
        System.out.println("Lesson Id : #" + lesson.getId());
        System.out.println("Subject   : " + lesson.getSubject().toUpperCase());
        System.out.println("Day     : " + lesson.getDate().toUpperCase());
        System.out.println("Time     : " + lesson.getTime());
        System.out.println("Price    : " + lesson.getPrice() + "€");
        System.out.println("Tutor's email    : " + lesson.getTutorEmail());
        System.out.println("-------------------------------------------------");

        System.out.print("Do you want to definitively confirm your booking? (yes/no): ");

        String choice = scanner.nextLine().trim().toLowerCase();
        if (choice.equals("yes") || choice.equals("y")) {

            BookingBean bookingBean = new BookingBean();
            bookingBean.setId(this.id);
            bookingBean.setStudentEmail(this.studentEmail);

            try {
                int bookingId = controller.bookLesson(bookingBean);

                if (bookingId != -1) {
                    System.out.println("\n================================");
                    System.out.println("[RECEIPT] BOOKING CONFIRMED WITH ID: #" + bookingId);
                    System.out.println("Current booking status: 'booked'");
                    System.out.println("================================");
                }
                else {
                    System.out.println("\n[CRITICAL ERROR] Error saving the reservation in the database.");
                }
            }
            catch (LessonAlreadyBookedException e) {
                System.out.println("\n=================================================");
                System.out.println("[BOOKING FAILED] " + e.getMessage());
                System.out.println("=================================================");
            }
        }
        else {
            System.out.println("\n[ADVICE] Reservation canceled by student.");
        }
        stateMachine.setState(new StudentHomeCLI(stateMachine, this.studentEmail));
    }
}

