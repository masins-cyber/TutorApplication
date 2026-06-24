package tutorapplication.cli;

import tutorapplication.bean.BookingBean;
import tutorapplication.bean.LessonBean;
import tutorapplication.controller.BookingController;
import tutorapplication.exception.UserNotPresentException;
import tutorapplication.pattern.AbstractState;
import tutorapplication.pattern.InitialState;
import tutorapplication.pattern.StateMachineImpl;
import tutorapplication.others.Print;

import java.util.List;
import java.util.Scanner;

public class ConfirmBookingTutorCLI extends AbstractState {
    private final BookingController bookingController;

    public ConfirmBookingTutorCLI() {
        super();
        this.bookingController = new BookingController();
    }

    @Override
    public void action(StateMachineImpl context) {
        String tutorEmail = context.getSessionUser().getEmail();
        List<BookingBean> pendingBookings = bookingController.getPendingBookings(tutorEmail);

        if (pendingBookings.isEmpty()) {
            handleNoBookings(context);
            return;
        }

        printHeader("Approvals");
        Print.println("\nHere are the booking requests for your lessons:");
        printPendingBookingsSummary(pendingBookings);

        Scanner scanner = new Scanner(System.in);
        Print.print("Enter the Reference ID of the booking you want to process (or '0' to go back): ");
        String input = scanner.nextLine().trim();

        if (input.equals("0")) {
            goBack(context);
            return;
        }

        processTutorInput(context, input, pendingBookings, tutorEmail, scanner);
    }

    private void handleNoBookings(StateMachineImpl context) {
        Print.println("\nYou have no reservations waiting for approval.");
        Print.println("Press ENTER to return to Home.");
        new Scanner(System.in).nextLine();
        goBack(context);
    }

    private void printPendingBookingsSummary(List<BookingBean> pendingBookings) {
        for (BookingBean b : pendingBookings) {
            LessonBean l = bookingController.getLessonDetails(b.getId());
            Print.println("-------------------------------------------------");
            Print.println("Booking Reference ID: #" + b.getBookingId());
            Print.println("Student Email: " + b.getStudentEmail());
            if (l != null) {
                Print.println("Subject: " + l.getSubject() + " | Day: " + l.getDay() + " | Time: " + l.getTimeSlot());
            }
            Print.println("-------------------------------------------------");
        }
    }

    private void processTutorInput(StateMachineImpl context, String input, List<BookingBean> pendingBookings, String tutorEmail, Scanner scanner) {
        int targetBookingId;
        try {
            targetBookingId = Integer.parseInt(input);
        } catch (NumberFormatException _) {
            Print.println("[ERROR] Invalid ID format.");
            return;
        }

        BookingBean selectedBooking = findBookingById(pendingBookings, targetBookingId);
        if (selectedBooking == null) {
            Print.println("[ERROR] Reference ID not found.");
            return;
        }

        Print.print("Type 'accept' to approve or 'reject' to deny this booking: ");
        String decision = scanner.nextLine().trim().toLowerCase();

        executeDecision(context, selectedBooking, decision, tutorEmail);
    }

    private BookingBean findBookingById(List<BookingBean> pendingBookings, int targetBookingId) {
        for (int i = 0; i < pendingBookings.size(); i++) {
            BookingBean b = pendingBookings.get(i);
            if (b.getBookingId() == targetBookingId) {
                return b;
            }
        }
        return null;
    }

    private void executeDecision(StateMachineImpl context, BookingBean selectedBooking, String decision, String tutorEmail) {
        if (!decision.equals("accept") && !decision.equals("reject")) {
            Print.println("[NOTICE] Operation cancelled. Invalid choice.");
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
            } else {
                Print.println("\n[ERROR] Error updating database.");
            }
        } catch (UserNotPresentException e) {
            handleSessionError(context, e, tutorEmail);
        }
    }

    private void handleSessionError(StateMachineImpl context, UserNotPresentException e, String tutorEmail) {
        Print.println("\n=================================================");
        Print.println(e.getMessage());

        if (e.getMessage().contains(tutorEmail)) {
            Print.println("[CRITICAL ALERT] Session invalid. Your Tutor account is no longer present in the database.");
            Print.println("[INFO] You will be logged out for security purposes.");
            Print.println("=================================================");
            context.setSessionUser(null);
            goNext(context, new InitialState());
        }
    }
}