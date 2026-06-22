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

public class StudentHomeGui {

    private static final Logger logger = Logger.getLogger(StudentHomeGui.class.getName());

    private String studentEmail;

    @FXML
    private Button searchLessonButton;
    @FXML
    private Button manageBookingsButton;
    @FXML
    private Button logoutButton;

    public void setStudentEmail(String email) {
        this.studentEmail = email;
    }

    @FXML
    void searchLesson() {
        logger.log(Level.INFO, "Navigating to Book a Lesson page...");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/searchlesson.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("Cannot find FXML file: /fxml/searchlesson.fxml");
            }
            Parent root = loader.load();

            SearchLessonGui searchGui = loader.getController();
            searchGui.setStudentEmail(this.studentEmail);

            Stage stage = (Stage) searchLessonButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading searchlesson.fxml", e);
            showAlert("Unable to load search window.");
        }
    }

    @FXML
    void manageBookings() {
        logger.log(Level.INFO, "Navigating to Manage Bookings page...");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/viewbooking.fxml"));
            Parent root = loader.load();

            ViewBookingGui viewBookingGui = loader.getController();
            viewBookingGui.setStudentEmail(this.studentEmail);

            Stage stage = (Stage) manageBookingsButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading viewbooking.fxml page", e);
            showAlert("Unable to load bookings window.");
        }
    }

    @FXML
    void logout() {
        logger.log(Level.INFO, "User logging out, returning to login screen.");
        changeScreen();
    }

    private void changeScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("Cannot find login.fxml file");
            }
            Parent root = loader.load();
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
            logger.log(Level.INFO, "Screen successfully changed to login.fxml ");
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading login.fxml page ", e);
            showAlert("Unable to go to login.fxml");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("System Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}