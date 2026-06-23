package tutorapplication.cli;

import tutorapplication.controller.BookingController;
import tutorapplication.exception.UserNotPresentException;
import tutorapplication.model.Booking;
import tutorapplication.model.Lesson;
import tutorapplication.pattern.AbstractState;
import tutorapplication.pattern.InitialState;
import tutorapplication.pattern.StateMachine;
import tutorapplication.others.Print;

import java.util.List;
import java.util.Scanner;

public class ConfirmBookingTutorCLI extends AbstractState {
    private final String tutorEmail;
    private final BookingController bookingController;
    private final List<Booking> pendingBookings;

    public ConfirmBookingTutorCLI(StateMachine stateMachine, String tutorEmail) {
        super(stateMachine);
        this.tutorEmail = tutorEmail;
        this.bookingController = new BookingController();
        this.pendingBookings = bookingController.getPendingBookings(tutorEmail);
    }

    @Override
    public void display() {
        printHeader("Approvals");
        if (pendingBookings.isEmpty()) {
            Print.println("\nYou have no reservations waiting for approval.");
            Print.println("Press ENTER to return to Home.");
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            stateMachine.setState(new TutorHomeCLI(stateMachine, this.tutorEmail));
            return;
        }

        Print.println("\nHere are the booking requests for your lessons:");
        for (int i = 0; i < pendingBookings.size(); i++) {
            Booking b = pendingBookings.get(i);
            Lesson l = bookingController.getLessonDetails(b.getId());
            Print.println("----------------------------------------");
            Print.println(" RESERVATION ID: #" + b.getBookingId());
            Print.println("   Student: " + b.getStudentEmail());
            if (l != null) {
                Print.println("   Subject: " + l.getSubject() + " | Day: " + l.getDate() + " | Time: " + l.getTime());
            }
            Print.println("   Actual state: [" + b.getStatus() + "]");
        }

        Print.println("\nOPTIONS:");
        Print.println("1) Manage a booking (Accept/Reject)");
        Print.println("2) Return to Home Tutor");
        Print.print("Select an option: ");
    }

    @Override
    public void handleInput(String input) {
        String choice = input.trim();

        if (pendingBookings.isEmpty() || choice.equals("2")) {
            stateMachine.setState(new TutorHomeCLI(stateMachine, this.tutorEmail));
            return;
        }

        if (choice.equals("1")) {
            processSelection();
        } else {
            stateMachine.setState(new ConfirmBookingTutorCLI(stateMachine, this.tutorEmail));
        }
    }

    private void processSelection() {
        Scanner scanner = new Scanner(System.in);
        Print.print("\nEnter the booking ID you want to manage: ");

        int targetBookingId;
        try {
            targetBookingId = Integer.parseInt(scanner.nextLine().trim());
        }
        catch (NumberFormatException _) {
            Print.println("\n[ERROR] Invalid input! You must insert a valid numeric ID.");
            stateMachine.setState(new ConfirmBookingTutorCLI(stateMachine, this.tutorEmail));
            return;
        }

        Booking selectedBooking = findBookingById(targetBookingId);
        if (selectedBooking == null) {
            Print.println("[ERROR] Booking ID invalid or not your responsibility.");
            stateMachine.setState(new ConfirmBookingTutorCLI(stateMachine, this.tutorEmail));
            return;
        }

        Print.print("Do you want to ACCEPT or REJECT the booking? (accept/reject): ");
        String decision = scanner.nextLine().trim().toLowerCase();
        recordDecision(selectedBooking, decision);
    }

    private Booking findBookingById(int targetBookingId) {
        for (int i = 0; i < pendingBookings.size(); i++) {
            Booking b = pendingBookings.get(i);
            if (b.getBookingId() == targetBookingId) {
                return b;
            }
        }
        return null;
    }

    private void recordDecision(Booking selectedBooking, String decision) {
        if (!decision.equals("accept") && !decision.equals("reject")) {
            Print.println("[NOTICE] Operation cancelled. Invalid choice.");
            stateMachine.setState(new ConfirmBookingTutorCLI(stateMachine, this.tutorEmail));
            return;
        }

        try {
            boolean success = bookingController.processTutorDecision(selectedBooking.getBookingId(), selectedBooking.getId(), decision);
            if (success) {
                Print.println("\n=================================================");
                String finalState = decision.equals("accept") ? "accepted" : "rejected";
                Print.println("[SUCCESS] Decision recorded! Status changed to: '" + finalState + "'");
                if (decision.equals("reject")) {
                    Print.println("[INFO] The lesson is available again for other students.");
                }
                Print.println("=================================================");
            }
            else {
                Print.println("\n[ERROR] Error updating database.");
            }
            stateMachine.setState(new ConfirmBookingTutorCLI(stateMachine, this.tutorEmail));
        } catch (UserNotPresentException e) {
            handleSessionError(e);
        }
    }

    private void handleSessionError(UserNotPresentException e) {
        Print.println("\n=================================================");
        Print.println(e.getMessage());

        if (e.getMessage().contains(this.tutorEmail)) {
            Print.println("[CRITICAL ALERT] Session invalid. Your Tutor account is no longer present in the database.");
            Print.println("[INFO] You will be logged out for security purposes.");
            Print.println("=================================================");
            stateMachine.setState(new InitialState(stateMachine));
        } else {
            stateMachine.setState(new ConfirmBookingTutorCLI(stateMachine, this.tutorEmail));
        }
    }
}

