package tutorapplication.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tutorapplication.controller.BookingController;
import tutorapplication.exception.UserNotPresentException;
import tutorapplication.model.Booking;
import tutorapplication.model.Lesson;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfirmBookingTutorGui {
    private static final Logger logger = Logger.getLogger(ConfirmBookingTutorGui.class.getName());

    private String tutorEmail;
    private final BookingController bookingController = new BookingController();

    @FXML
    private TableView<Booking> requestsTable;
    @FXML
    private TableColumn<Booking, Integer> colBookingId;
    @FXML
    private TableColumn<Booking, String> colStudent;
    @FXML
    private TableColumn<Booking, String> colStatus;
    @FXML
    private TableColumn<Booking, String> colSubject;
    @FXML
    private TableColumn<Booking, String> colDay;
    @FXML
    private TableColumn<Booking, String> colTime;
    @FXML
    private VBox decisionBox;
    @FXML
    private Button backButton;

    public void setTutorEmail(String email) {
        this.tutorEmail = email;
        loadPendingBookings();
    }

    @FXML
    public void initialize() {
        colBookingId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        colStudent.setCellValueFactory(cellData -> {
            String email = cellData.getValue().getStudentEmail();
            if (email == null || email.isBlank()) {
                return new javafx.beans.property.SimpleStringProperty("N/A");
            }
            return new javafx.beans.property.SimpleStringProperty(email);
        });

        colSubject.setCellValueFactory(cellData -> {
            int lessonId = cellData.getValue().getId();
            Lesson l = bookingController.getLessonDetails(lessonId);
            if (l != null) {
                return new SimpleStringProperty(l.getSubject().toUpperCase());
            } else {
                return new SimpleStringProperty("N/A");
            }
        });

        colDay.setCellValueFactory(cellData -> {
            int lessonId = cellData.getValue().getId();
            Lesson l = bookingController.getLessonDetails(lessonId);
            if (l != null) {
                return new SimpleStringProperty(l.getDate().toUpperCase());
            } else {
                return new SimpleStringProperty("N/A");
            }
        });

        colTime.setCellValueFactory(cellData -> {
            int lessonId = cellData.getValue().getId();
            Lesson l = bookingController.getLessonDetails(lessonId);
            if (l != null) {
                return new SimpleStringProperty(l.getTime());
            } else {
                return new SimpleStringProperty("N/A");
            }
        });

        requestsTable.getSelectionModel().selectedItemProperty().addListener(observable -> {
            logger.log(Level.FINE, "Track select: ", observable);
            Booking selected = requestsTable.getSelectionModel().getSelectedItem();

            if (selected != null) {
                decisionBox.setVisible(true);
                decisionBox.setManaged(true);
            } else {
                decisionBox.setVisible(false);
                decisionBox.setManaged(false);
            }
        });
    }

    private void loadPendingBookings() {
        List<Booking> pending = bookingController.getPendingBookings(this.tutorEmail);
        requestsTable.setItems(FXCollections.observableArrayList(pending));
    }

    @FXML
    void accept() {
        processDecision("accept");
    }

    @FXML
    void reject() {
        processDecision("reject");
    }

    private void processDecision(String decision) {
        Booking selectedBooking = requestsTable.getSelectionModel().getSelectedItem();
        if (selectedBooking == null)
            return;

        try {
            boolean success = bookingController.processTutorDecision(selectedBooking.getBookingId(), selectedBooking.getId(), decision);

            if (success) {
                String finalState;
                if (decision.equals("accept")) {
                    finalState = "accepted";
                } else {
                    finalState = "rejected";
                }

                String infoMessage = "Decision recorded! Status changed to: '" + finalState + "'";
                if (decision.equals("reject")) {
                    infoMessage += "\nThe lesson is available again for other students.";
                }

                showAlert(Alert.AlertType.INFORMATION, "Success", infoMessage);
                loadPendingBookings();
            }
            else {
                showAlert(Alert.AlertType.ERROR, "Error", "Error updating database.");
            }

        } catch (UserNotPresentException e) {
            logger.log(Level.SEVERE, "Security context error", e);
            if (e.getMessage() != null && e.getMessage().contains(this.tutorEmail)) {
                showAlert(Alert.AlertType.ERROR, "CRITICAL ALERT", e.getMessage());
                forceLogoutToLoginScreen();
            }
            else {
                showAlert(Alert.AlertType.WARNING, "Warning", e.getMessage());
            }
        }
    }

    @FXML
    void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/tutorhomepage.fxml"));
            Parent root = loader.load();

            TutorHomeGui tutorHome = loader.getController();
            tutorHome.setTutorEmail(this.tutorEmail);

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error backing to home dashboard", e);
        }
    }

    private void forceLogoutToLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) requestsTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "Error forcing security logout", e);
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
