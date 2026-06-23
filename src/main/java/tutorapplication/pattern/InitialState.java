package tutorapplication.pattern;

import tutorapplication.cli.LoginCLI;
import tutorapplication.cli.RegistrationCLI;
import tutorapplication.others.Print;

public class InitialState extends AbstractState {

    public InitialState(StateMachine stateMachine) {
        super(stateMachine);
    }

    @Override
    public void display() {
        Print.println("\n*******************************");
        Print.println("*      TUTOR APPLICATION      *");
        Print.println("*******************************");
        Print.println("1) Enter");
        Print.println("2) Register");
        Print.println("Write 'exit' to exit.");
        Print.print("Select an option: ");
    }

    @Override
    public void handleInput(String input) {
        switch (input) {
            case "1":
                Print.println("Moving to the login phase...");
                stateMachine.setState(new LoginCLI(stateMachine));
                break;
            case "2":
                Print.println("Opening registration form...");
                stateMachine.setState(new RegistrationCLI(stateMachine));
                break;
            default:
                Print.println("Command not recognized.");
                break;
        }
    }
}

