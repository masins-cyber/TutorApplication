package tutorapplication.cli;

import tutorapplication.controller.BookingController;
import tutorapplication.model.Booking;
import tutorapplication.model.Lesson;
import tutorapplication.others.Print;
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
            Print.println("\nYou haven't made any reservations yet.");
            Print.println("Press ENTER to return to Student Home.");
            return;
        }
        Print.println("\nHere is the complete list of your requests:");
        for (int i = 0; i < myBookings.size(); i++) {
            Booking b = myBookings.get(i);
            Lesson l = bookingController.getLessonDetails(b.getId());

            Print.println("----------------------------------------");

            if (b.getStatus().equalsIgnoreCase("accepted")) {
                Print.println("CONFIRMED BOOKINGS: #" + b.getBookingId());
            }
            else if (b.getStatus().equalsIgnoreCase("rejected")) {
                Print.println("REJECTED BOOKINGS: #" + b.getBookingId());
            }
            else {
                Print.println("RESERVATION MADE (Booked that is waiting for tutor): #" + b.getBookingId());
            }
            if (l != null) {
                Print.println("   Subject: " + l.getSubject());
                Print.println("   Tutor: " + l.getTutorEmail());
                Print.println("   Day: " + l.getDate() + " | Time: " + l.getTime());
            }
            Print.println("   Actual state: [" + b.getStatus().toUpperCase() + "]");
        }

        Print.println("----------------------------------------");
        Print.println("\nOPTIONS:");
        Print.println("1) Delete/Cancel a reservation.");
        Print.println("2) Return to Student Home.");
        Print.print("Select an option: ");
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
                Print.println("Going to delete the booking...");
                stateMachine.setState(new CancelBookingCLI(stateMachine, this.studentEmail, this.myBookings));
                break;
            case "2":
                Print.println("Going to the student page...");
                stateMachine.setState(new StudentHomeCLI(stateMachine, this.studentEmail));
                break;
            default:
                Print.println("[ADVISE] Invalid option. Retry.");
                stateMachine.setState(new ViewBookingCLI(stateMachine, this.studentEmail));
                break;
        }
    }
}

