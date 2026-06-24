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
import tutorapplication.bean.BookingBean;
import tutorapplication.bean.LessonBean;
import tutorapplication.controller.BookingController;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewBookingGui {
    private static final Logger logger = Logger.getLogger(ViewBookingGui.class.getName());

    private String studentEmail;
    private final BookingController bookingController = new BookingController();

    @FXML
    private TableView<BookingBean> bookingsTable;
    @FXML
    private TableColumn<BookingBean, Integer> colBookingId;
    @FXML
    private TableColumn<BookingBean, String> colStatus;
    @FXML
    private TableColumn<BookingBean, String> colSubject;
    @FXML
    private TableColumn<BookingBean, String> colTutor;
    @FXML
    private TableColumn<BookingBean, String> colDay;
    @FXML
    private TableColumn<BookingBean, String> colTime;

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
        colStatus.setCellValueFactory(cellData -> new SimpleStringProperty(formatStatus(cellData.getValue().getStatus())));
        colSubject.setCellValueFactory(cellData -> new SimpleStringProperty(getLessonField(cellData.getValue().getId(), "SUBJECT")));
        colTutor.setCellValueFactory(cellData -> new SimpleStringProperty(getLessonField(cellData.getValue().getId(), "TUTOR")));
        colDay.setCellValueFactory(cellData -> new SimpleStringProperty(getLessonField(cellData.getValue().getId(), "DAY")));
        colTime.setCellValueFactory(cellData -> new SimpleStringProperty(getLessonField(cellData.getValue().getId(), "TIME")));

        bookingsTable.getSelectionModel().selectedItemProperty().addListener(observable -> {
            logger.log(Level.FINE, "Selection track altered: {0}", observable);
            BookingBean selected = bookingsTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                actionBox.setVisible(true);
                actionBox.setManaged(true);
            } else {
                actionBox.setVisible(false);
                actionBox.setManaged(false);
            }
        });
    }

    private String formatStatus(String rawStatus) {
        if (rawStatus.equalsIgnoreCase("accepted"))
            return "ACCEPTED";
        if (rawStatus.equalsIgnoreCase("rejected"))
            return "REJECTED";

        return "BOOKED";
    }

    private String getLessonField(int lessonId, String fieldType) {
        LessonBean l = bookingController.getLessonDetails(lessonId);
        if (l == null)
            return "N/A";

        return switch (fieldType) {
            case "SUBJECT" -> l.getSubject().toUpperCase();
            case "TUTOR" -> l.getTutorEmail();
            case "DAY" -> l.getDay().toUpperCase();
            case "TIME" -> l.getTimeSlot();
            default -> "N/A";
        };
    }

    private void loadStudentBookings() {
        List<BookingBean> myBookings = bookingController.getAllStudentBookings(this.studentEmail);
        bookingsTable.setItems(FXCollections.observableArrayList(myBookings));
    }

    @FXML
    void cancelBooking() {
        BookingBean selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
        if (selectedBooking == null)
            return;

        if (selectedBooking.getStatus().equalsIgnoreCase("rejected")) {
            showAlert("Error",
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
            showAlert("System Error", "Unable to open cancellation layout.");
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

