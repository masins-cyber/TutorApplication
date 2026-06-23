package tutorapplication.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import tutorapplication.controller.BookingController;
import tutorapplication.model.Booking;
import tutorapplication.model.Lesson;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CancelBookingGui {
    private static final Logger logger = Logger.getLogger(CancelBookingGui.class.getName());

    private String studentEmail;
    private Booking targetBooking;
    private final BookingController bookingController = new BookingController();

    @FXML
    private Label bookingIdLabel;
    @FXML
    private Label subjectLabel;
    @FXML
    private Label tutorLabel;
    @FXML
    private Label dayLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Button abortButton;

    public void setCancellationTarget(Booking booking, String email) {
        this.studentEmail = email;
        this.targetBooking = booking;

        if (this.targetBooking != null) {
            bookingIdLabel.setText("#" + targetBooking.getBookingId());
            statusLabel.setText("[" + targetBooking.getStatus().toUpperCase() + "]");

            Lesson l = bookingController.getLessonDetails(targetBooking.getId());
            if (l != null) {
                subjectLabel.setText(l.getSubject().toUpperCase());
                tutorLabel.setText(l.getTutorEmail());
                dayLabel.setText(l.getDate().toUpperCase());
                timeLabel.setText(l.getTime());
            }
        }
    }

    @FXML
    void confirmCancellation() {
        if (targetBooking == null)
            return;

        boolean success = bookingController.cancelStudentBooking(targetBooking.getBookingId(), targetBooking.getId());

        if (success) {
            logger.log(Level.INFO, "Booking #{0} successfully canceled by student.", targetBooking.getBookingId());
            showAlert(Alert.AlertType.INFORMATION, "Success", "Booking canceled successfully!\nThe lesson is available again!");
            returnToViewBookings();
        }
        else {
            logger.log(Level.SEVERE, "Technical error while updating the database for booking: {0}", targetBooking.getBookingId());
            showAlert(Alert.AlertType.ERROR, "Error", "Technical error while updating the database.");
        }
    }

    @FXML
    void abort() {
        logger.log(Level.INFO, "Operation cancelled. The reservation remains unchanged.");
        returnToViewBookings();
    }

    private void returnToViewBookings() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/viewbooking.fxml"));
            Parent root = loader.load();

            ViewBookingGui viewGui = loader.getController();
            viewGui.setStudentEmail(this.studentEmail);

            Stage stage = (Stage) abortButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "Error returning to viewbooking.fxml board", e);
            showAlert(Alert.AlertType.ERROR, "System Error", "Unable to redirect to bookings board.");
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
