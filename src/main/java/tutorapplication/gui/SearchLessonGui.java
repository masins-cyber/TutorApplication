package tutorapplication.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tutorapplication.bean.SearchLessonBean;
import tutorapplication.controller.BookingController;
import tutorapplication.exception.LessonsNotFoundException;
import tutorapplication.model.Lesson;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SearchLessonGui {
    private static final Logger logger = Logger.getLogger(SearchLessonGui.class.getName());
    private String studentEmail;

    @FXML
    private TextField subjectField;
    @FXML
    private TextField priceField;
    @FXML
    private ComboBox<String> dayComboBox;
    @FXML
    private TextField timeField;
    @FXML
    private Button searchButton;
    @FXML
    private Button backButton;

    public void setStudentEmail(String email) {
        this.studentEmail = email;
    }

    @FXML
    void search() {
        String subject = subjectField.getText().trim();
        String priceText = priceField.getText().trim();

        if (subject.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Filter", "Subject is mandatory to start the search!");
            return;
        }

        String day = "";
        if (dayComboBox.getValue() != null) {
            day = dayComboBox.getValue().toLowerCase();
        }

        String time = timeField.getText().trim();

        try {
            SearchLessonBean searchBean = new SearchLessonBean();
            searchBean.setSubject(subject.toLowerCase());
            searchBean.setDay(day);
            searchBean.setTimeSlot(time);

            if (priceText.isEmpty()) {
                searchBean.setMaxPrice(999.0);
            }
            else {
                searchBean.setMaxPrice(Double.parseDouble(priceText));
            }

            BookingController bookingController = new BookingController();
            List<Lesson> foundLessons = bookingController.searchLessons(searchBean);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/lessonresults.fxml"));
            Parent root = loader.load();

            LessonResultsGui resultsGui = loader.getController();
            resultsGui.setLessonsData(foundLessons);
            resultsGui.setStudentEmail(this.studentEmail);

            Stage stage = (Stage) searchButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();

        }
        catch (NumberFormatException _) {
            logger.log(Level.WARNING, "Invalid max price format!" );
            showAlert(Alert.AlertType.ERROR, "Invalid Price", "Please enter a valid numeric value for the budget.");
        }
        catch (LessonsNotFoundException e) { //
            logger.log(Level.INFO, "No lessons found with the provided filters.");
            showAlert(Alert.AlertType.INFORMATION, "No Results", e.getMessage());
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "Error moving to lessonresults.fxml", e);
            showAlert(Alert.AlertType.ERROR, "System Error", "Unable to load results screen.");
        }
    }

    @FXML
    void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/studenthomepage.fxml"));
            Parent root = loader.load();

            StudentHomeGui studentHome = loader.getController();
            studentHome.setStudentEmail(this.studentEmail);

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "Error returning to student home", e);
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
