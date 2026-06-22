package tutorapplication.CLI;

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
        System.out.println("Welcome to Student Home");
        System.out.println("1) Search and book a lesson");
        System.out.println("2) Manage bookings");
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
                System.out.println("Navigating to the lesson search...");
                stateMachine.setState(new SearchLessonCLI(stateMachine, this.studentEmail));
                break;
            case "2":
                System.out.println("Features under development...");
                stateMachine.setState(new ViewBookingCLI(stateMachine, this.studentEmail));
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

