package tutorapplication.Gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tutorapplication.bean.SearchLessonBean;
import tutorapplication.controller.BookingController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InsertLessonGui {
    private static final Logger logger = Logger.getLogger(InsertLessonGui.class.getName());
    private String tutorEmail;

    @FXML
    private TextField subjectField;
    @FXML
    private ComboBox<String> dayComboBox;
    @FXML
    private TextField timeField;
    @FXML
    private TextField priceField;
    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        dayComboBox.setItems(FXCollections.observableArrayList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"));
    }

    public void setTutorEmail(String email) {
        this.tutorEmail = email;
    }

    @FXML
    void insertLesson() {
        if (subjectField.getText().isBlank() || dayComboBox.getValue() == null || timeField.getText().isBlank() || priceField.getText().isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Missing Data", "Please fill in all details for the lesson.");
            return;
        }

        String subject = subjectField.getText().trim();
        String day = dayComboBox.getValue().toUpperCase();
        String time = timeField.getText().trim();
        String priceText = priceField.getText().trim();

        try {
            double price = Double.parseDouble(priceText);

            SearchLessonBean lessonBean = new SearchLessonBean();
            lessonBean.setSubject(subject);
            lessonBean.setDay(day);
            lessonBean.setTimeSlot(time);
            lessonBean.setMaxPrice(price);

            BookingController bookingController = new BookingController();
            boolean success = bookingController.addLesson(lessonBean, this.tutorEmail);

            if (success) {
                logger.log(Level.INFO, "New lesson successfully added into BookingController by: {0}", this.tutorEmail);
                showAlert(Alert.AlertType.INFORMATION, "Success", "You've inserted the lesson successfully!");
                subjectField.clear();
                dayComboBox.setValue(null);
                timeField.clear();
                priceField.clear();
                goBackHome();
            }
            else {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to save the lesson into the system.");
            }
        }
        catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Format error for the price entered: {0}", priceText);
            showAlert(Alert.AlertType.ERROR, "Invalid Price", "Please enter a valid numeric value for the price.");
        }
    }

    @FXML
    void goBack() {
        goBackHome();
    }

    private void goBackHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/tutorhomepage.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("Cannot find FXML file: /fxml/tutorhomepage.fxml");
            }
            Parent root = loader.load();

            TutorHomeGui tutorHome = loader.getController();
            tutorHome.setTutorEmail(this.tutorEmail);

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();

        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "Error returning to tutorhomepage.fxml", e);
            showAlert(Alert.AlertType.ERROR, "System Error", "Unable to load tutor home.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
