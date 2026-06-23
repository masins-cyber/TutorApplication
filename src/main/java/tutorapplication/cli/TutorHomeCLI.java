package tutorapplication.cli;

import tutorapplication.others.Print;
import tutorapplication.pattern.AbstractState;
import tutorapplication.pattern.InitialState;
import tutorapplication.pattern.StateMachineImpl;

public class TutorHomeCLI extends AbstractState {

    @Override
    public void action(StateMachineImpl context) {
        String input = showMenuAndGetInput();
        processTutorChoice(context, input);
    }

    private void processTutorChoice(StateMachineImpl context, String input) {
        switch (input) {
            case "1":
                manageLessons(context);
                break;
            case "2":
                viewRequests(context);
                break;
            case "3":
                executeLogout(context);
                break;
            default:
                Print.println("Invalid option.");
                break;
        }
    }

    private void manageLessons(StateMachineImpl context) {
        Print.println("Navigating to tutor profile management...");
        goNext(context, new InsertLessonCLI());
    }

    private void viewRequests(StateMachineImpl context) {
        Print.println("Loading pending requests...");
        goNext(context, new ConfirmBookingTutorCLI());
    }

    private void executeLogout(StateMachineImpl context) {
        Print.println("Logging out...");
        context.setSessionUser(null);
        goNext(context, new InitialState());
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