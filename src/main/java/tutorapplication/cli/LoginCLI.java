package tutorapplication.cli;

import tutorapplication.bean.LoginBean;
import tutorapplication.controller.LoginController;
import tutorapplication.exception.UserNotPresentException;
import tutorapplication.exception.WrongCredentialsException;
import tutorapplication.model.User;
import tutorapplication.others.Print;
import tutorapplication.pattern.AbstractState;
import tutorapplication.pattern.StateMachine;
import tutorapplication.pattern.InitialState;

import java.util.Scanner;

public class LoginCLI extends AbstractState {
    private final Scanner scanner = new Scanner(System.in);

    public LoginCLI(StateMachine stateMachine) {
        super(stateMachine);
    }

    @Override
    public void display() {
        printHeader("Login Screen");

        Print.print("Email: ");
        String email = scanner.nextLine();
        Print.print("Password: ");
        String password = scanner.nextLine();
        Print.print("Are you a Tutor? (yes/no): ");
        String choice = scanner.nextLine();

        boolean isTutor = choice.equalsIgnoreCase("yes");
        processLogin(email, password, isTutor);
    }

    private void processLogin(String email, String password, Boolean isTutor) {
        LoginBean loginBean = new LoginBean();
        loginBean.setEmail(email);
        loginBean.setPassword(password);
        loginBean.setTutor(isTutor);

        LoginController loginController = new LoginController();
        try {
            User user = loginController.login(loginBean);
            Print.println("\nLogin Successful! Welcome back.");

            if(loginBean.isTutor()) {
                stateMachine.setState(new TutorHomeCLI(stateMachine, user.getEmail()));
            }
            else {
                stateMachine.setState(new StudentHomeCLI(stateMachine, user.getEmail()));
            }
        }
        catch (UserNotPresentException e) {
            Print.println("\n[LOGIN FAILED] " + e.getMessage());
            Print.println("If you are not yet registered, select the Registration option from the main menu.");
            stateMachine.setState(new InitialState(stateMachine));
        }
        catch (WrongCredentialsException e) {
            Print.println("\n" + e.getMessage());
            stateMachine.setState(new InitialState(stateMachine));
        }
    }
}

