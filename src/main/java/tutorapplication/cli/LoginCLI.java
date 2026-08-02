package tutorapplication.cli;

import tutorapplication.bean.LoginBean;
import tutorapplication.bean.UserBean;
import tutorapplication.controller.LoginController;
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
        String email = scanner.nextLine().trim();
        Print.print("Password: ");
        String password = scanner.nextLine().trim();
        Print.print("Are you a Tutor? (yes/no): ");
        String choice = scanner.nextLine().trim();

        if (email.isEmpty() || password.isEmpty() || choice.isEmpty()) {
            Print.println("\n[ERROR] All fields are mandatory to login! Returning to main menu.");
            goBack(context);
            return;
        }

        boolean isTutor = choice.equalsIgnoreCase("yes");

        LoginBean loginBean = new LoginBean();
        loginBean.setEmail(email);
        loginBean.setPassword(password);
        loginBean.setTutor(isTutor);

        LoginController loginController = new LoginController();
        try {
            UserBean loggedUser = loginController.login(loginBean);
            Print.println("\nLogin Successful! Welcome back.");

            context.setSessionUser(loggedUser);

            if (loggedUser.getRole().equalsIgnoreCase("TUTOR")) {
                goNext(context, new TutorHomeCLI());
            } else {
                goNext(context, new StudentHomeCLI());
            }
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