package tutorapplication.cli;

import tutorapplication.others.Print;
import tutorapplication.pattern.AbstractState;
import tutorapplication.pattern.InitialState;
import tutorapplication.pattern.StateMachineImpl;

public class StudentHomeCLI extends AbstractState {

    @Override
    public void action(StateMachineImpl context) {

        String input = showMenuAndGetInput();

        switch (input) {
            case "1":
                Print.println("Navigating to the lesson search...");
                goNext(context, new SearchLessonCLI());
                break;
            case "2":
                Print.println("Loading your bookings...");
                goNext(context, new ViewBookingCLI());
                break;
            case "3":
                Print.println("Logging out...");
                context.setSessionUser(null);
                goNext(context, new InitialState());
                break;
            default:
                Print.println("Invalid option.");
                break;
        }
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

