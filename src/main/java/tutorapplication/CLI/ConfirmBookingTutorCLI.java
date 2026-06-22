package tutorapplication.CLI;

import tutorapplication.controller.BookingController;
import tutorapplication.exception.UserNotPresentException;
import tutorapplication.model.Booking;
import tutorapplication.model.Lesson;
import tutorapplication.pattern.AbstractState;
import tutorapplication.pattern.InitialState;
import tutorapplication.pattern.StateMachine;

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
            System.out.println("\nYou have no reservations waiting for approval.");
            System.out.println("Press ENTER to return to Home.");
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            stateMachine.setState(new TutorHomeCLI(stateMachine, this.tutorEmail));
            return;
        }

        System.out.println("\nHere are the booking requests for your lessons:");
        for (int i = 0; i < pendingBookings.size(); i++) {
            Booking b = pendingBookings.get(i);
            Lesson l = bookingController.getLessonDetails(b.getId());
            System.out.println("----------------------------------------");
            System.out.println(" RESERVATION ID: #" + b.getBookingId());
            System.out.println("   Student: " + b.getStudentEmail());
            if (l != null) {
                System.out.println("   Subject: " + l.getSubject() + " | Day: " + l.getDate() + " | Time: " + l.getTime());
            }
            System.out.println("   Actual state: [" + b.getStatus() + "]");
        }

        System.out.println("\nOPTIONS:");
        System.out.println("1) Manage a booking (Accept/Reject)");
        System.out.println("2) Return to Home Tutor");
        System.out.print("Select an option: ");
    }

    @Override
    public void handleInput(String input) {
        String choice = input.trim();

        if (pendingBookings.isEmpty() || choice.equals("2")) {
            stateMachine.setState(new TutorHomeCLI(stateMachine, this.tutorEmail));
            return;
        }

        if (choice.equals("1")) {
            Scanner scanner = new Scanner(System.in);

            System.out.print("\nEnter the booking ID you want to manage: ");
            int targetBookingId;

            try {
                targetBookingId = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("\n[ERROR] Invalid input! You must insert a valid numeric ID.");
                stateMachine.setState(new ConfirmBookingTutorCLI(stateMachine, this.tutorEmail));
                return;
            }

            Booking selectedBooking = null;
            for (int i = 0; i < pendingBookings.size(); i++) {
                Booking b = pendingBookings.get(i);
                if (b.getBookingId() == targetBookingId) {
                    selectedBooking = b;
                    break;
                }
            }

            if (selectedBooking == null) {
                System.out.println("[ERROR] Booking ID invalid or not your responsibility.");
                stateMachine.setState(new ConfirmBookingTutorCLI(stateMachine, this.tutorEmail));
                return;
            }

            System.out.print("Do you want to ACCEPT or REJECT the booking? (accept/reject): ");
            String decision = scanner.nextLine().trim().toLowerCase();

            if (decision.equals("accept") || decision.equals("reject")) {
                try {
                    boolean success = bookingController.processTutorDecision(selectedBooking.getBookingId(), selectedBooking.getId(), decision);

                    if (success) {
                        System.out.println("\n=================================================");
                        String finalState;
                        if (decision.equals("accept")) {
                            finalState = "accepted";
                        } else {
                            finalState = "rejected";
                        }
                        System.out.println("[SUCCESS] Decision recorded! Status changed to: '" + finalState + "'");
                        if (decision.equals("reject")) {
                            System.out.println("[INFO] The lesson is available again for other students.");
                        }
                        System.out.println("=================================================");
                    } else {
                        System.out.println("\n[ERROR] Error updating database.");
                    }
                } catch (UserNotPresentException e) {
                    System.out.println("\n=================================================");
                    System.out.println(e.getMessage());

                    if (e.getMessage().contains(this.tutorEmail)) {
                        System.out.println("[CRITICAL ALERT] Session invalid. Your Tutor account is no longer present in the database.");
                        System.out.println("[INFO] You will be logged out for security purposes.");
                        System.out.println("=================================================");
                        stateMachine.setState(new InitialState(stateMachine));
                        return;
                    }
                }
            } else {
                System.out.println("[NOTICE] Operation cancelled. Invalid choice.");
            }
        }
        stateMachine.setState(new ConfirmBookingTutorCLI(stateMachine, this.tutorEmail));
    }
}
