package tutorapplication.cli;

import tutorapplication.bean.UserBean;
import tutorapplication.controller.RegistrationController;
import tutorapplication.exception.EmailAlreadyInUseException;
import tutorapplication.exception.InvalidEmailException;
import tutorapplication.others.Print;
import tutorapplication.pattern.AbstractState;
import tutorapplication.pattern.StateMachineImpl;

import java.util.Scanner;

public class RegistrationCLI extends AbstractState {

    @Override
    public void action(StateMachineImpl context) {
        display();

        Scanner scanner = new Scanner(System.in);
        UserBean userBean = new UserBean();

        Print.print("Email: ");
        userBean.setEmail(scanner.nextLine());
        Print.print("Password: ");
        userBean.setPassword(scanner.nextLine());
        Print.print("Name: ");
        userBean.setName(scanner.nextLine());
        Print.print("Surname: ");
        userBean.setSurname(scanner.nextLine());
        Print.print("Role (STUDENT/TUTOR): ");
        String role = scanner.nextLine().toUpperCase();
        userBean.setRole(role);

        if ("STUDENT".equals(role)) {
            Print.print("Student ID: ");
            userBean.setStudentId(scanner.nextLine());
        }

        RegistrationController registrationController = new RegistrationController();

        try {
            boolean success = registrationController.register(userBean);

            if (success) {
                Print.println("\nRegistration successful!");
                Print.println("Now you can login.");
            }
            else {
                Print.println("\nRegistration failed due to a database error. Please try again.");
            }

            goBack(context);
        }
        catch (InvalidEmailException | EmailAlreadyInUseException e) {
            Print.println("\n" + e.getMessage());
            Print.println("Please try again.");
        }
    }

    @Override
    public void display() {
        printHeader("Registration");
    }
}