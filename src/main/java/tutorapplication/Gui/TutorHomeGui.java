package tutorapplication.Gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TutorHomeGui {

    private static final Logger logger = Logger.getLogger(TutorHomeGui.class.getName());
    private String tutorEmail;
    @FXML
    private Button insertLessonButton;
    @FXML
    private Button viewRequestsButton;
    @FXML
    private Button logoutButton;

    public void setTutorEmail(String email) {
        this.tutorEmail = email;
    }

    @FXML
    void insertLesson() {
        logger.log(Level.INFO, "Navigating to Insert Lesson page...");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/insertlesson.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("Cannot find FXML file: /fxml/insertlesson.fxml");
            }
            Parent root = loader.load();

            InsertLessonGui insertLessonGui = loader.getController();
            insertLessonGui.setTutorEmail(this.tutorEmail);

            Stage stage = (Stage) insertLessonButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading page: insertlesson.fxml", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("System Error");
            alert.setHeaderText(null);
            alert.setContentText("Unable to load page: insertlesson.fxml");
            alert.showAndWait();
        }
    }

    @FXML
    void viewBookingRequests() {
        logger.log(Level.INFO, "Navigating to View Booking Requests page...");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/confirmbookingtutor.fxml"));
            Parent root = loader.load();

            ConfirmBookingTutorGui approvalGui = loader.getController();
            approvalGui.setTutorEmail(this.tutorEmail);

            Stage stage = (Stage) viewRequestsButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading confirmbookingtutor.fxml", e);
        }
    }

    @FXML
    void logout() {
        logger.log(Level.INFO, "Tutor logging out, returning to login screen.");
        changeScreen();
    }

    private void changeScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("Cannot find login.fxml");
            }
            Parent root = loader.load();
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
            logger.log(Level.INFO, "Screen successfully changed to login.fxml");
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading login.fxml page", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("System Error");
            alert.setHeaderText(null);
            alert.setContentText("Unable to load login.fxml page");
            alert.showAndWait();
        }
    }
}

