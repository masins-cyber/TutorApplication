package tutorapplication.org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tutorapplication.others.Config;
import tutorapplication.pattern.StateMachineImpl;
import tutorapplication.others.Print;

import java.util.Scanner;

public class Start {

    private static final String DIVIDER = "=========================================";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean validPersistenceChoice = false;

        while(!validPersistenceChoice) {
            Print.println(DIVIDER);
            Print.println("     WELCOME TO TUTOR APPLICATION        ");
            Print.println(DIVIDER);
            Print.println("Select the persistence layer to use:");
            Print.println("1) MySQL Database");
            Print.println("2) JSON Files");
            Print.println("3) In-Memory");
            Print.print("Select: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    Config.setPersistenceType("mysql");
                    Print.println("\n[CONFIG] System configured to use MySQL.");
                    validPersistenceChoice = true;
                    break;
                case "2":
                    Config.setPersistenceType("json");
                    Print.println("\n[CONFIG] System configured to use JSON.");
                    validPersistenceChoice = true;
                    break;
                case "3":
                    Config.setPersistenceType("memory");
                    Print.println("\n[CONFIG] System configured to use In-Memory RAM.");
                    validPersistenceChoice = true;
                    break;
                default:
                    Print.println("\n[ERROR] Invalid choice! Please select 1, 2, or 3.\n");
                    break;
            }
        }
        Print.println(DIVIDER + "\n");

        boolean validInterfaceChoice = false;
        String interfaceChoice = "";

        while (!validInterfaceChoice) {
            Print.println(DIVIDER);
            Print.println("       SELECT USER INTERFACE MODE        ");
            Print.println(DIVIDER);
            Print.println("1) Command Line Interface (CLI)");
            Print.println("2) Graphical User Interface (GUI)");
            Print.print("Select: ");

            interfaceChoice = scanner.nextLine().trim();

            if (interfaceChoice.equals("1") || interfaceChoice.equals("2")) {
                validInterfaceChoice = true;
            } else {
                Print.println("\n[ERROR] Invalid choice! Please select 1 or 2.\n");
            }
        }
        Print.println(DIVIDER + "\n");

        if (interfaceChoice.equals("1")) {
            Print.println("[SYSTEM] Launching Command Line Interface...\n");
            StateMachineImpl app = new StateMachineImpl();
            app.run();
        }
        else {
            Print.println("[SYSTEM] Launching Graphical User Interface...\n");
            scanner.close();
            Application.launch(JavaFXLauncher.class);
        }
    }

    public static class JavaFXLauncher extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            primaryStage.setTitle("Tutor Application");
            primaryStage.setScene(new Scene(root, 800, 500));
            primaryStage.setResizable(false);
            primaryStage.centerOnScreen();
            primaryStage.show();
            primaryStage.setAlwaysOnTop(true);
            primaryStage.setAlwaysOnTop(false);
            primaryStage.requestFocus();
        }
    }
}
