package tutorapplication.cli;

import tutorapplication.bean.UserBean;
import tutorapplication.controller.RegistrationController;
import tutorapplication.exception.EmailAlreadyInUseException;
import tutorapplication.exception.InvalidEmailException;
import tutorapplication.others.Print;
import tutorapplication.pattern.AbstractState;
import tutorapplication.pattern.InitialState;
import tutorapplication.pattern.StateMachine;

import java.util.Scanner;

public class RegistrationCLI extends AbstractState {
    private final Scanner scanner = new Scanner(System.in);

    public RegistrationCLI(StateMachine stateMachine) {
        super(stateMachine);
    }

    @Override
    public void display() {
        printHeader("Registration");

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

        if("STUDENT".equals(role)) {
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

            stateMachine.setState(new InitialState(stateMachine));
        }
        catch (InvalidEmailException e) {
            Print.println("\n" + e.getMessage());
            Print.println("Please enter a valid email address.");
            stateMachine.setState(new RegistrationCLI(stateMachine));
        }
        catch (EmailAlreadyInUseException e) {
            Print.println("\n" + e.getMessage());
            Print.println("Please try again with a different email address.");
            stateMachine.setState(new RegistrationCLI(stateMachine));
        }
    }
}

