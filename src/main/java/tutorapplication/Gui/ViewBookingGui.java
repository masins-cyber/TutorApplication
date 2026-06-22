package tutorapplication.Gui;

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
import tutorapplication.model.Booking;
import tutorapplication.model.Lesson;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewBookingGui {
    private static final Logger logger = Logger.getLogger(ViewBookingGui.class.getName());

    private String studentEmail;
    private final BookingController bookingController = new BookingController();

    @FXML
    private TableView<Booking> bookingsTable;
    @FXML
    private TableColumn<Booking, Integer> colBookingId;
    @FXML
    private TableColumn<Booking, String> colStatus;
    @FXML
    private TableColumn<Booking, String> colSubject;
    @FXML
    private TableColumn<Booking, String> colTutor;
    @FXML
    private TableColumn<Booking, String> colDay;
    @FXML
    private TableColumn<Booking, String> colTime;

    @FXML
    private VBox actionBox;
    @FXML
    private Button cancelBookingButton;
    @FXML
    private Button backButton;

    public void setStudentEmail(String email) {
        this.studentEmail = email;
        loadStudentBookings();
    }

    @FXML
    public void initialize() {
        colBookingId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        colStatus.setCellValueFactory(cellData -> {
            String rawStatus = cellData.getValue().getStatus();
            if (rawStatus.equalsIgnoreCase("accepted")) {
                return new SimpleStringProperty("ACCEPTED");
            } else if (rawStatus.equalsIgnoreCase("rejected")) {
                return new SimpleStringProperty("REJECTED");
            } else {
                return new SimpleStringProperty("BOOKED");
            }
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

        colTutor.setCellValueFactory(cellData -> {
            int lessonId = cellData.getValue().getId();
            Lesson l = bookingController.getLessonDetails(lessonId);
            if (l != null) {
                return new SimpleStringProperty(l.getTutorEmail());
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

        bookingsTable.getSelectionModel().selectedItemProperty().addListener(observable -> {
            logger.log(Level.FINE, "Selection track altered: {0}", observable);
            Booking selected = bookingsTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                actionBox.setVisible(true);
                actionBox.setManaged(true);
            } else {
                actionBox.setVisible(false);
                actionBox.setManaged(false);
            }
        });
    }

    private void loadStudentBookings() {
        List<Booking> myBookings = bookingController.getAllStudentBookings(this.studentEmail);
        bookingsTable.setItems(FXCollections.observableArrayList(myBookings));
    }

    @FXML
    void cancelBooking() {
        Booking selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
        if (selectedBooking == null)
            return;

        if (selectedBooking.getStatus().equalsIgnoreCase("rejected")) {
            showAlert(Alert.AlertType.ERROR, "Error",
                    "This booking has already been rejected by the tutor, there is no need to cancel it.");
            return;
        }

        logger.log(Level.INFO, "Going to delete the booking...");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/cancelbooking.fxml"));
            Parent root = loader.load();

            CancelBookingGui cancelGui = loader.getController();
            cancelGui.setCancellationTarget(selectedBooking, this.studentEmail);

            Stage stage = (Stage) cancelBookingButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "Error moving to cancel booking workspace", e);
            showAlert(Alert.AlertType.ERROR, "System Error", "Unable to open cancellation layout.");
        }
    }

    @FXML
    void goBack() {
        logger.log(Level.INFO, "Going to the student page...");
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
            logger.log(Level.SEVERE, "Error returning to student dashboard", e);
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
