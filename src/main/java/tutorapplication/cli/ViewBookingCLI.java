package tutorapplication.cli;

import tutorapplication.bean.BookingBean;
import tutorapplication.bean.LessonBean;
import tutorapplication.controller.BookingController;
import tutorapplication.others.Print;
import tutorapplication.pattern.AbstractState;
import tutorapplication.pattern.StateMachineImpl;

import java.util.List;
import java.util.Scanner;

public class ViewBookingCLI extends AbstractState {

    @Override
    public void action(StateMachineImpl context) {
        String studentEmail = context.getSessionUser().getEmail();
        BookingController bookingController = new BookingController();
        List<BookingBean> myBookings = bookingController.getAllStudentBookings(studentEmail);

        printHeader("My bookings");

        if (myBookings.isEmpty()) {
            Print.println("\nYou haven't made any reservations yet.");
            Print.println("Press ENTER to return to Student Home.");
            new Scanner(System.in).nextLine();
            goBack(context);
            return;
        }

        Print.println("\nHere is the complete list of your requests:");
        for (int i = 0; i < myBookings.size(); i++) {
            BookingBean b = myBookings.get(i);
            LessonBean l = bookingController.getLessonDetails(b.getId());

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
                Print.println(" Subject: " + l.getSubject());
                Print.println(" Tutor: " + l.getTutorEmail());
                Print.println(" Day: " + l.getDay() + " | Time: " + l.getTimeSlot());
            }
            Print.println(" Actual state: [" + b.getStatus().toUpperCase() + "]");
        }

        Print.println("----------------------------------------");
        Print.println("\nOPTIONS:");
        Print.println("1) Delete/Cancel a reservation.");
        Print.println("2) Return to Student Home.");
        Print.print("Select an option: ");

        Scanner scanner = new Scanner(System.in);
        String choose = scanner.nextLine().trim();

        switch (choose) {
            case "1":
                Print.println("Going to delete the booking...");
                goNext(context, new CancelBookingCLI(myBookings));
                break;
            case "2":
                Print.println("Going back to the student page...");
                goBack(context);
                break;
            default:
                Print.println("[ADVISE] Invalid option. Retry.");
                break;
        }
    }
}