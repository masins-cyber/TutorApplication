package tutorapplication.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import tutorapplication.bean.BookingBean;
import tutorapplication.bean.LessonBean;
import tutorapplication.controller.BookingController;
import tutorapplication.exception.LessonAlreadyBookedException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfirmBookingGui {
    private static final Logger logger = Logger.getLogger(ConfirmBookingGui.class.getName());
    private int lessonId;
    private String studentEmail;
    private LessonBean lesson;
    private final BookingController controller = new BookingController();

    @FXML
    private Label idLabel;
    @FXML
    private Label subjectLabel;
    @FXML
    private Label dayLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Label tutorLabel;

    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;

    public void setBookingData(int id, String studentEmail) {
        this.lessonId = id;
        this.studentEmail = studentEmail;
        this.lesson = controller.getLessonDetails(id);
        if (this.lesson == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "The requested lesson does not exist or has been removed.");
            returnToStudentHome();
            return;
        }
        idLabel.setText("#" + lesson.getId());
        subjectLabel.setText(lesson.getSubject().toUpperCase());
        dayLabel.setText(lesson.getDay().toUpperCase());
        timeLabel.setText(lesson.getTimeSlot());
        priceLabel.setText(lesson.getMaxPrice() + "€");
        tutorLabel.setText(lesson.getTutorEmail());
    }

    @FXML
    void confirmBooking() {
        if (this.lesson == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "The requested lesson does not exist or has been removed.");
            returnToStudentHome();
            return;
        }

        BookingBean bookingBean = new BookingBean();
        bookingBean.setId(this.lessonId);
        bookingBean.setStudentEmail(this.studentEmail);

        try {
            int bookingId = controller.bookLesson(bookingBean);
            if (bookingId != -1) {
                logger.log(Level.INFO, "Booking confirmed with ID: {0}", bookingId);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Great! Lesson booked successfully.\nReceipt ID: #" + bookingId);
                returnToStudentHome();
            }
            else {
                showAlert(Alert.AlertType.ERROR, "Critical Error", "Error saving the reservation in the database.");
            }
        }
        catch (LessonAlreadyBookedException e) {
            logger.log(Level.WARNING, "Booking failed!", e);
            showAlert(Alert.AlertType.ERROR, "Booking Failed", e.getMessage() + "\nThis slot is no longer available.");
            returnToStudentHome();
        }
    }

    @FXML
    void cancel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/searchlesson.fxml"));
            Parent root = loader.load();

            SearchLessonGui searchGui = loader.getController();
            searchGui.setStudentEmail(this.studentEmail);

            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "Error returning to search filters", e);
        }
    }

    private void returnToStudentHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/studenthomepage.fxml"));
            Parent root = loader.load();

            StudentHomeGui studentHome = loader.getController();
            studentHome.setStudentEmail(this.studentEmail);

            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error shifting to student dashboard", e);
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
