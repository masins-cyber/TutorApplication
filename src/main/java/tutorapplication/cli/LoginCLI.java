package tutorapplication.cli;

import tutorapplication.bean.LoginBean;
import tutorapplication.controller.LoginController;
import tutorapplication.exception.UserNotPresentException;
import tutorapplication.exception.WrongCredentialsException;
import tutorapplication.others.Print;
import tutorapplication.pattern.AbstractState;
import tutorapplication.pattern.StateMachineImpl;

import java.util.Scanner;

public class LoginCLI extends AbstractState {

    @Override
    public void action(StateMachineImpl context) {
        display();

        Scanner scanner = new Scanner(System.in);
        Print.print("Email: ");
        String email = scanner.nextLine();
        Print.print("Password: ");
        String password = scanner.nextLine();
        Print.print("Are you a Tutor? (yes/no): ");
        String choice = scanner.nextLine();

        boolean isTutor = choice.equalsIgnoreCase("yes");

        LoginBean loginBean = new LoginBean();
        loginBean.setEmail(email);
        loginBean.setPassword(password);
        loginBean.setTutor(isTutor);

        LoginController loginController = new LoginController();
        try {
            loginController.login(loginBean);
            Print.println("\nLogin Successful! Welcome back.");

            context.setSessionUser(loginBean);

            if (loginBean.isTutor()) {
                goNext(context, new TutorHomeCLI());
            } else {
                goNext(context, new StudentHomeCLI());
            }
        }
        catch (UserNotPresentException e) {
            Print.println("\n[LOGIN FAILED] " + e.getMessage());
            Print.println("If you are not yet registered, select the Registration option from the main menu.");
            goBack(context);
        }
        catch (WrongCredentialsException e) {
            Print.println("\n" + e.getMessage());
            goBack(context);
        }
    }

    @Override
    public void display() {
        printHeader("Login Screen");
    }
}