package tutorapplication.pattern;

import tutorapplication.CLI.LoginCLI;
import tutorapplication.CLI.RegistrationCLI;

public class InitialState extends AbstractState {

    public InitialState(StateMachine stateMachine) {
        super(stateMachine);
    }

    @Override
    public void display() {
        System.out.println("\n*******************************");
        System.out.println("*      TUTOR APPLICATION      *");
        System.out.println("*******************************");
        System.out.println("1) Enter");
        System.out.println("2) Register");
        System.out.println("Write 'exit' to exit.");
        System.out.print("Select an option: ");
    }

    @Override
    public void handleInput(String input) {
        switch (input) {
            case "1":
                System.out.println("Moving to the login phase...");
                stateMachine.setState(new LoginCLI(stateMachine));
                break;
            case "2":
                System.out.println("Opening registration form...");
                stateMachine.setState(new RegistrationCLI(stateMachine));
                break;
            default:
                System.out.println("Command not recognized.");
                break;
        }
    }
}

