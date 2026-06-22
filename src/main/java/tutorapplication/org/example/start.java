package tutorapplication.org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tutorapplication.others.Config;
import tutorapplication.pattern.StateMachineImpl;

import java.util.Scanner;

public class start {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean validPersistenceChoice = false;

        while(!validPersistenceChoice) {
            System.out.println("=========================================");
            System.out.println("     WELCOME TO TUTOR APPLICATION        ");
            System.out.println("=========================================");
            System.out.println("Select the persistence layer to use:");
            System.out.println("1) MySQL Database");
            System.out.println("2) JSON Files");
            System.out.println("3) In-Memory");
            System.out.print("Select: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    Config.setPersistenceType("mysql");
                    System.out.println("\n[CONFIG] System configured to use MySQL.");
                    validPersistenceChoice = true;
                    break;
                case "2":
                    Config.setPersistenceType("json");
                    System.out.println("\n[CONFIG] System configured to use JSON.");
                    validPersistenceChoice = true;
                    break;
                case "3":
                    Config.setPersistenceType("memory");
                    System.out.println("\n[CONFIG] System configured to use In-Memory RAM.");
                    validPersistenceChoice = true;
                    break;
                default:
                    System.out.println("\n[ERROR] Invalid choice! Please select 1, 2, or 3.\n");
                    break;
            }
        }
        System.out.println("=========================================\n");

        boolean validInterfaceChoice = false;
        String interfaceChoice = "";

        while (!validInterfaceChoice) {
            System.out.println("=========================================");
            System.out.println("       SELECT USER INTERFACE MODE        ");
            System.out.println("=========================================");
            System.out.println("1) Command Line Interface (CLI)");
            System.out.println("2) Graphical User Interface (GUI)");
            System.out.print("Select: ");

            interfaceChoice = scanner.nextLine().trim();

            if (interfaceChoice.equals("1") || interfaceChoice.equals("2")) {
                validInterfaceChoice = true;
            } else {
                System.out.println("\n[ERROR] Invalid choice! Please select 1 or 2.\n");
            }
        }
        System.out.println("=========================================\n");

        if (interfaceChoice.equals("1")) {
            System.out.println("[SYSTEM] Launching Command Line Interface...\n");
            StateMachineImpl app = new StateMachineImpl();
            app.run();
        } else {
            System.out.println("[SYSTEM] Launching Graphical User Interface...\n");
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
