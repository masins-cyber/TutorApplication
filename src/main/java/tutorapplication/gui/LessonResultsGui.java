package tutorapplication.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tutorapplication.bean.LessonBean;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LessonResultsGui {
    private static final Logger logger = Logger.getLogger(LessonResultsGui.class.getName());
    private static final String SYSTEM_ERROR = "System Error";
    private String studentEmail;

    @FXML
    private TableView<LessonBean> lessonsTable;
    @FXML
    private TableColumn<LessonBean, Integer> colId;
    @FXML
    private TableColumn<LessonBean, String> colSubject;
    @FXML
    private TableColumn<LessonBean, String> colDay;
    @FXML
    private TableColumn<LessonBean, String> colTime;
    @FXML
    private TableColumn<LessonBean, Double> colPrice;
    @FXML
    private TableColumn<LessonBean, String> colTutor;

    @FXML
    private Button backToSearchButton;
    @FXML
    private Hyperlink backToHomeLink;
    @FXML
    private Button bookLessonButton;

    public void setStudentEmail(String email) {
        this.studentEmail = email;
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSubject.setCellValueFactory(new PropertyValueFactory<>("subject"));
        colDay.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colTutor.setCellValueFactory(new PropertyValueFactory<>("tutorEmail"));

        lessonsTable.getSelectionModel().selectedItemProperty().addListener(observable -> {
            logger.log(Level.FINE, "Selection property invalidated: {0}", observable);
            LessonBean selected = lessonsTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                bookLessonButton.setVisible(true);
                bookLessonButton.setManaged(true);
            }
            else {
                bookLessonButton.setVisible(false);
                bookLessonButton.setManaged(false);
            }
        });
    }
    public void setLessonsData(List<LessonBean> lessons) {
        if (lessons != null) {
            lessonsTable.setItems(FXCollections.observableArrayList(lessons));
        }
    }

    @FXML
    void bookLesson() {
        LessonBean selectedLesson = lessonsTable.getSelectionModel().getSelectedItem();

        if (selectedLesson == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a lesson from the table first!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/confirmbooking.fxml"));
            Parent root = loader.load();

            ConfirmBookingGui confirmGui = loader.getController();
            confirmGui.setBookingData(selectedLesson.getId(), this.studentEmail);

            Stage stage = (Stage) bookLessonButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error moving to confirmbooking.fxml", e);
            showAlert(Alert.AlertType.ERROR, SYSTEM_ERROR, "Unable to open booking confirmation window.");
        }
    }

    @FXML
    void backToSearch() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/searchlesson.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backToSearchButton.getScene().getWindow();

            SearchLessonGui searchGui = loader.getController();
            searchGui.setStudentEmail(this.studentEmail);

            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "Error returning to search filters", e);
            showAlert(Alert.AlertType.ERROR, SYSTEM_ERROR, "Unable to load search window.");
        }
    }

    @FXML
    void backToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/studenthomepage.fxml"));
            Parent root = loader.load();

            StudentHomeGui studentHome = loader.getController();
            studentHome.setStudentEmail(this.studentEmail);

            Stage stage = (Stage) backToHomeLink.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();

            logger.log(Level.INFO, "Successfully returned to student homepage for: {0}", this.studentEmail);
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "Error returning to student home from results", e);
            showAlert(Alert.AlertType.ERROR, SYSTEM_ERROR, "Unable to load student home.");
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
