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
        String email = scanner.nextLine().trim();
        Print.print("Password: ");
        String password = scanner.nextLine().trim();
        Print.print("Name: ");
        String name = scanner.nextLine().trim();
        Print.print("Surname: ");
        String surname = scanner.nextLine().trim();
        Print.print("Role (STUDENT/TUTOR): ");
        String role = scanner.nextLine().trim().toUpperCase();

        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || surname.isEmpty() || role.isEmpty()) {
            Print.println("\n[ERROR] All fields are mandatory! Returning to main menu.");
            goBack(context);
            return;
        }

        userBean.setEmail(email);
        userBean.setPassword(password);
        userBean.setName(name);
        userBean.setSurname(surname);
        userBean.setRole(role);

        if ("STUDENT".equals(role)) {
            Print.print("Student ID: ");
            String studentId = scanner.nextLine().trim();

            if (studentId.isEmpty()) {
                Print.println("\n[ERROR] Student ID is mandatory for STUDENT role! Returning to main menu.");
                goBack(context);
                return;
            }
            userBean.setStudentId(studentId);
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