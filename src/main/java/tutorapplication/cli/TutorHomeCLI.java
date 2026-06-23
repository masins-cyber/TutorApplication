package tutorapplication.cli;

import tutorapplication.others.Print;
import tutorapplication.pattern.AbstractState;
import tutorapplication.pattern.InitialState;
import tutorapplication.pattern.StateMachineImpl;

public class TutorHomeCLI extends AbstractState {

    @Override
    public void action(StateMachineImpl context) {
        String input = showMenuAndGetInput();

        switch (input) {
            case "1":
                Print.println("Navigating to tutor profile management...");
                goNext(context, new InsertLessonCLI());
                break;
            case "2":
                Print.println("Loading pending requests...");
                goNext(context, new ConfirmBookingTutorCLI());
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
        printHeader("Tutor Homepage");
        Print.println("Welcome, Tutor!");
        Print.println("1) Manage availability (Set day, time and price)");
        Print.println("2) View booking requests");
        Print.println("3) Logout");
        Print.print("Select an option: ");
    }
}

