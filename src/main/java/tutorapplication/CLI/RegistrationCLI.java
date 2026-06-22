package tutorapplication.CLI;

import tutorapplication.bean.UserBean;
import tutorapplication.controller.RegistrationController;
import tutorapplication.exception.EmailAlreadyInUseException;
import tutorapplication.exception.InvalidEmailException;
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

        System.out.print("Email: ");
        userBean.setEmail(scanner.nextLine());
        System.out.print("Password: ");
        userBean.setPassword(scanner.nextLine());
        System.out.print("Name: ");
        userBean.setName(scanner.nextLine());
        System.out.print("Surname: ");
        userBean.setSurname(scanner.nextLine());
        System.out.print("Role (STUDENT/TUTOR): ");
        String role = scanner.nextLine().toUpperCase();
        userBean.setRole(role);

        if("STUDENT".equals(role)) {
            System.out.print("Student ID: ");
            userBean.setStudentId(scanner.nextLine());
        }

        RegistrationController registrationController = new RegistrationController();

        try {
            boolean success = registrationController.register(userBean);

            if (success) {
                System.out.println("\nRegistration successful!");
                System.out.println("Now you can login.");
            }
            else {
                System.out.println("\nRegistration failed due to a database error. Please try again.");
            }

            stateMachine.setState(new InitialState(stateMachine));
        }
        catch (InvalidEmailException e) {
            System.out.println("\n" + e.getMessage());
            System.out.println("Please enter a valid email address.");
            stateMachine.setState(new RegistrationCLI(stateMachine));
        }
        catch (EmailAlreadyInUseException e) {
            System.out.println("\n" + e.getMessage());
            System.out.println("Please try again with a different email address.");
            stateMachine.setState(new RegistrationCLI(stateMachine));
        }
    }
}

