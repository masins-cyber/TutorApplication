package tutorapplication.cli;

import tutorapplication.others.Print;
import tutorapplication.pattern.AbstractState;
import tutorapplication.pattern.InitialState;
import tutorapplication.pattern.StateMachineImpl;

public class StudentHomeCLI extends AbstractState {

    @Override
    public void action(StateMachineImpl context) {
        String input = showMenuAndGetInput();
        processStudentChoice(context, input);
    }

    private void processStudentChoice(StateMachineImpl context, String input) {
        switch (input) {
            case "1":
                goToSearch(context);
                break;
            case "2":
                goToBookings(context);
                break;
            case "3":
                executeLogout(context);
                break;
            default:
                Print.println("Invalid option.");
                break;
        }
    }

    private void goToSearch(StateMachineImpl context) {
        Print.println("Navigating to the lesson search...");
        goNext(context, new SearchLessonCLI());
    }

    private void goToBookings(StateMachineImpl context) {
        Print.println("Loading your bookings...");
        goNext(context, new ViewBookingCLI());
    }

    private void executeLogout(StateMachineImpl context) {
        Print.println("Logging out...");
        context.setSessionUser(null);
        goNext(context, new InitialState());
    }

    @Override
    public void display() {
        printHeader("Student Homepage");
        Print.println("Welcome to Student Home");
        Print.println("1) Search and book a lesson");
        Print.println("2) Manage bookings");
        Print.println("3) Logout");
        Print.print("Select an option: ");
    }
}