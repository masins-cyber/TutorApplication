package tutorapplication.cli;

import tutorapplication.controller.BookingController;
import tutorapplication.model.Booking;
import tutorapplication.pattern.AbstractState;
import tutorapplication.pattern.StateMachine;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CancelBookingCLI extends AbstractState {
    private static final Logger logger = Logger.getLogger(CancelBookingCLI.class.getName());

    private final String studentEmail;
    private final BookingController bookingController;
    private final List<Booking> myBookings;

    public CancelBookingCLI(StateMachine stateMachine, String studentEmail, List<Booking> myBookings) {
        super(stateMachine);
        this.studentEmail = studentEmail;
        this.bookingController = new BookingController();
        this.myBookings = myBookings;
    }

    @Override
    public void display() {
        printHeader("Cancel Booking");
        logger.log(Level.INFO, "Enter the ID of the booking you want to delete (or '0' to go back): ");
    }

    @Override
    public void handleInput(String input) {
        String rawInput = input.trim();

        if (rawInput.equals("0")) {
            logger.log(Level.INFO, "Going back to the booking page...");
            stateMachine.setState(new ViewBookingCLI(stateMachine, this.studentEmail));
            return;
        }

        int targetBookingId = Integer.parseInt(rawInput);
        Booking selectedBooking = null;

        for (int i = 0; i < myBookings.size(); i++) {
            Booking b = myBookings.get(i);
            if (b.getBookingId() == targetBookingId) {
                selectedBooking = b;
                break;
            }
        }

        if (selectedBooking == null) {
            logger.log(Level.WARNING, "[ERROR] Reservation ID not found in your history.");
            stateMachine.setState(new CancelBookingCLI(stateMachine, this.studentEmail, this.myBookings));
            return;
        }

        if (selectedBooking.getStatus().equalsIgnoreCase("rejected")) {
            logger.log(Level.WARNING, "[ERROR] This booking has already been rejected by the tutor, there is no need to cancel it.");
            stateMachine.setState(new ViewBookingCLI(stateMachine, this.studentEmail));
            return;
        }

        Scanner scanner = new Scanner(System.in);
        String promptMsg = String.format("Are you sure you want to cancel your reservation #%d? (yes/no): ", targetBookingId);
        logger.log(Level.INFO, promptMsg);
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("yes") || confirm.equals("y")) {
            boolean success = bookingController.cancelStudentBooking(selectedBooking.getBookingId(), selectedBooking.getId());

            if (success) {
                logger.log(Level.INFO, "\n=================================================");
                logger.log(Level.INFO, "[SUCCESS] Booking canceled successfully!");
                logger.log(Level.INFO, "[INFO] The lesson is available again!");
                logger.log(Level.INFO, "=================================================");
            } else {
                logger.log(Level.SEVERE, "\n[ERROR] Technical error while updating the database.");
            }
        } else {
            logger.log(Level.INFO, "\n[NOTICE] Operation cancelled. The reservation remains unchanged.");
        }
        stateMachine.setState(new ViewBookingCLI(stateMachine, this.studentEmail));
    }
}

