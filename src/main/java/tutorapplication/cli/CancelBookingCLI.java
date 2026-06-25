package tutorapplication.cli;

import tutorapplication.bean.BookingBean;
import tutorapplication.controller.BookingController;
import tutorapplication.others.Print;
import tutorapplication.pattern.AbstractState;
import tutorapplication.pattern.StateMachineImpl;

import java.util.List;
import java.util.Scanner;

public class CancelBookingCLI extends AbstractState {

    private final BookingController bookingController;
    private final List<BookingBean> myBookings;

    public CancelBookingCLI(List<BookingBean> myBookings) {
        super();
        this.bookingController = new BookingController();
        this.myBookings = myBookings;
    }

    @Override
    public void action(StateMachineImpl context) {

        Print.print("Enter the ID of the booking you want to delete (or '0' to go back): ");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String rawInput = input.trim();

        if (rawInput.equals("0")) {
            Print.println("Going back to the booking page...");
            goBack(context);
            return;
        }

        int targetBookingId;
        try {
            targetBookingId = Integer.parseInt(rawInput);
        } catch (NumberFormatException _) {
            Print.errorPrint("[ERROR] Invalid ID format.");
            return;
        }

        BookingBean selectedBooking = null;
        for (int i = 0; i < myBookings.size(); i++) {
            BookingBean b = myBookings.get(i);
            if (b.getBookingId() == targetBookingId) {
                selectedBooking = b;
                break;
            }
        }

        if (selectedBooking == null) {
            Print.errorPrint("[ERROR] Reservation ID not found in your history.");
            return;
        }

        if (selectedBooking.getStatus().equalsIgnoreCase("rejected")) {
            Print.errorPrint("[ERROR] This booking has already been rejected by the tutor, there is no need to cancel it.");
            goBack(context);
            return;
        }

        Print.print("Are you sure you want to cancel your reservation #" + targetBookingId + "? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("yes") || confirm.equals("y")) {
            boolean success = bookingController.cancelStudentBooking(selectedBooking.getBookingId(), selectedBooking.getId());

            if (success) {
                Print.println("\n=================================================");
                Print.println("[SUCCESS] Booking canceled successfully!");
                Print.println("[INFO] The lesson is available again!");
                Print.println("=================================================");
            }
            else {
                Print.errorPrint("\n[ERROR] Technical error while updating the database.");
            }
        }
        else {
            Print.println("\n[NOTICE] Operation cancelled. The reservation remains unchanged.");
        }
        goBack(context);
    }
    @Override
    public void display() {
        printHeader("Cancel Booking");
    }
}
