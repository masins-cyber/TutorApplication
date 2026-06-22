package tutorapplication.CLI;

import tutorapplication.controller.BookingController;
import tutorapplication.model.Booking;
import tutorapplication.model.Lesson;
import tutorapplication.pattern.AbstractState;
import tutorapplication.pattern.StateMachine;

import java.util.List;

public class ViewBookingCLI extends AbstractState {
    private final String studentEmail;
    private final BookingController bookingController;
    private final List<Booking> myBookings;

    public ViewBookingCLI(StateMachine stateMachine, String studentEmail) {
        super(stateMachine);
        this.studentEmail = studentEmail;
        this.bookingController = new BookingController();
        this.myBookings = bookingController.getAllStudentBookings(studentEmail);
    }

    @Override
    public void display() {
        printHeader("My bookings");

        if(myBookings.isEmpty()) {
            System.out.println("\nYou haven't made any reservations yet.");
            System.out.println("Press ENTER to return to Student Home.");
            return;
        }
        System.out.println("\nHere is the complete list of your requests:");
        for (int i = 0; i < myBookings.size(); i++) {
            Booking b = myBookings.get(i);
            Lesson l = bookingController.getLessonDetails(b.getId());

            System.out.println("----------------------------------------");

            if (b.getStatus().equalsIgnoreCase("accepted")) {
                System.out.println("CONFIRMED BOOKINGS: #" + b.getBookingId());
            }
            else if (b.getStatus().equalsIgnoreCase("rejected")) {
                System.out.println("REJECTED BOOKINGS: #" + b.getBookingId());
            }
            else {
                System.out.println("RESERVATION MADE (Booked that is waiting for tutor): #" + b.getBookingId());
            }
            if (l != null) {
                System.out.println("   Subject: " + l.getSubject());
                System.out.println("   Tutor: " + l.getTutorEmail());
                System.out.println("   Day: " + l.getDate() + " | Time: " + l.getTime());
            }
            System.out.println("   Actual state: [" + b.getStatus().toUpperCase() + "]");
        }

        System.out.println("----------------------------------------");
        System.out.println("\nOPTIONS:");
        System.out.println("1) Delete/Cancel a reservation.");
        System.out.println("2) Return to Student Home.");
        System.out.print("Select an option: ");
    }

    @Override
    public void handleInput(String input) {
        String choose = input.trim();

        if (myBookings.isEmpty()) {
            stateMachine.setState(new StudentHomeCLI(stateMachine, this.studentEmail));
            return;
        }
        switch (choose) {
            case "1":
                System.out.println("Going to delete the booking...");
                stateMachine.setState(new CancelBookingCLI(stateMachine, this.studentEmail, this.myBookings));
                break;
            case "2":
                System.out.println("Going to the student page...");
                stateMachine.setState(new StudentHomeCLI(stateMachine, this.studentEmail));
                break;
            default:
                System.out.println("[ADVISE] Invalid option. Retry.");
                stateMachine.setState(new ViewBookingCLI(stateMachine, this.studentEmail));
                break;
        }
    }
}
