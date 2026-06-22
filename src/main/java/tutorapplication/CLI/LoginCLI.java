package tutorapplication.CLI;

import tutorapplication.bean.LoginBean;
import tutorapplication.controller.LoginController;
import tutorapplication.exception.UserNotPresentException;
import tutorapplication.exception.WrongCredentialsException;
import tutorapplication.model.User;
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

        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Are you a Tutor? (yes/no): ");
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
            System.out.println("\nLogin Successful! Welcome back.");

            if(loginBean.isTutor()) {
                stateMachine.setState(new TutorHomeCLI(stateMachine, user.getEmail()));
            }
            else {
                stateMachine.setState(new StudentHomeCLI(stateMachine, user.getEmail()));
            }
        }
        catch (UserNotPresentException e) {
            System.out.println("\n[LOGIN FAILED] " + e.getMessage());
            System.out.println("If you are not yet registered, select the Registration option from the main menu.");
            stateMachine.setState(new InitialState(stateMachine));
        }
        catch (WrongCredentialsException e) {
            System.out.println("\n" + e.getMessage());
            stateMachine.setState(new InitialState(stateMachine));
        }
    }
}

