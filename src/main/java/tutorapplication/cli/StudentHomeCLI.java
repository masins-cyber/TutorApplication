package tutorapplication.cli;

import tutorapplication.others.Print;
import tutorapplication.pattern.AbstractState;
import tutorapplication.pattern.InitialState;
import tutorapplication.pattern.StateMachine;

public class StudentHomeCLI extends AbstractState {
    private final String studentEmail;

    public StudentHomeCLI(StateMachine stateMachine, String email) {
        super(stateMachine);
        this.studentEmail = email;
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

    @Override
    public void handleInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            return;
        }
        switch (input) {
            case "1":
                Print.println("Navigating to the lesson search...");
                stateMachine.setState(new SearchLessonCLI(stateMachine, this.studentEmail));
                break;
            case "2":
                Print.println("Features under development...");
                stateMachine.setState(new ViewBookingCLI(stateMachine, this.studentEmail));
                break;
            case "3":
                Print.println("Logging out...");
                stateMachine.setState(new InitialState(stateMachine));
                break;
            default:
                Print.println("Invalid option.");
                break;
        }
    }
}

