package tutorapplication.cli;

import tutorapplication.pattern.AbstractState;
import tutorapplication.pattern.StateMachine;
import tutorapplication.pattern.InitialState;

public class TutorHomeCLI extends AbstractState {
    private final String tutorEmail;

    public TutorHomeCLI(StateMachine stateMachine, String email) {
        super(stateMachine);
        this.tutorEmail = email;
    }

    @Override
    public void display() {
        printHeader("Tutor Homepage");
        System.out.println("Welcome, Tutor!");
        System.out.println("1) Manage availability (Set day, time and price)");
        System.out.println("2) View booking requests");
        System.out.println("3) Logout");
        System.out.print("Select an option: ");
    }

    @Override
    public void handleInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            return;
        }
        switch (input) {
            case "1":
                System.out.println("Navigating to tutor profile management...");
                stateMachine.setState(new InsertLessonCLI(stateMachine, this.tutorEmail));
                break;
            case "2":
                System.out.println("Loading pending requests...");
                stateMachine.setState(new ConfirmBookingTutorCLI(stateMachine, this.tutorEmail));
                break;
            case "3":
                System.out.println("Logging out...");
                stateMachine.setState(new InitialState(stateMachine));
                break;
            default:
                System.out.println("Invalid option.");
                break;
        }
    }
}

