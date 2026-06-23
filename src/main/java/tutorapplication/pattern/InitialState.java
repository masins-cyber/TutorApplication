package tutorapplication.pattern;

import tutorapplication.cli.LoginCLI;
import tutorapplication.cli.RegistrationCLI;
import tutorapplication.others.Print;
import java.util.Scanner;

public class InitialState extends AbstractState {

    @Override
    public void action(StateMachineImpl context) {
        display();
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                Print.println("Moving to the login phase...");
                goNext(context, new LoginCLI());
                break;
            case "2":
                Print.println("Opening registration form...");
                goNext(context, new RegistrationCLI());
                break;
            case "exit":
                Print.println("Closing application...");
                context.terminate();
                break;
            default:
                Print.println("Command not recognized.");
                break;
        }
    }

    @Override
    public void display() {
        Print.println("\n*******************************");
        Print.println("* TUTOR APPLICATION      *");
        Print.println("*******************************");
        Print.println("1) Enter");
        Print.println("2) Register");
        Print.println("Write 'exit' to exit.");
        Print.print("Select an option: ");
    }
}