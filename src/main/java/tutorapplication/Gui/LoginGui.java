package tutorapplication.Gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tutorapplication.bean.LoginBean;
import tutorapplication.controller.LoginController;
import tutorapplication.exception.UserNotPresentException;
import tutorapplication.exception.WrongCredentialsException;
import tutorapplication.model.User;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginGui {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private CheckBox tutorCheckBox;

    @FXML
    private Button loginButton;

    private final LoginController loginController = new LoginController();
    private static final String SYSTEM_ERROR = "System Error";
    private static final Logger logger = Logger.getLogger(LoginGui.class.getName());

    @FXML
    void login() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Incomplete fields", "Please fill in all fields!");
            return;
        }

        LoginBean loginBean = new LoginBean();
        loginBean.setEmail(email);
        loginBean.setPassword(password);
        loginBean.setTutor(tutorCheckBox.isSelected());

        try {
            User user = loginController.login(loginBean);
            if (user != null) {
                showAlert(Alert.AlertType.INFORMATION, "Access done!", "Welcome " + user.getName() + "!");
                if ("TUTOR".equalsIgnoreCase(user.getRole())) {
                    tutorPage(email);
                }
                else {
                    studentPage(email);
                }
            }

        }
        catch (UserNotPresentException e) {
            showAlert(Alert.AlertType.ERROR, "User not found!", e.getMessage());
        }
        catch (WrongCredentialsException e) {
            showAlert(Alert.AlertType.ERROR, "Wrong credentials", e.getMessage());
        }
    }

    private void tutorPage(String email) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/tutorhomepage.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("Cannot find FXML file: /fxml/tutorhomepage.fxml");
            }
            Parent root = loader.load();

            TutorHomeGui tutorHome = loader.getController();
            tutorHome.setTutorEmail(email);

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
            logger.log(Level.INFO, "Tutor homepage loaded successfully for: {0}", email);
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading tutorhomepage.fxml", e);
            showAlert(Alert.AlertType.ERROR, SYSTEM_ERROR, "Failed to load tutor homepage.");
        }
    }

    private void studentPage(String email) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/studenthomepage.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("Cannot find FXML file: /fxml/studenthomepage.fxml");
            }
            Parent root = loader.load();

            StudentHomeGui studentHome = loader.getController();
            studentHome.setStudentEmail(email);

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();

            logger.log(Level.INFO, "Student homepage loaded successfully for: {0}", email);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading studenthomepage.fxml", e);
            showAlert(Alert.AlertType.ERROR, SYSTEM_ERROR, "Failed to load student homepage.");
        }
    }

    @FXML
    void registration() {
        changeScreen();
    }

    private void changeScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/registration.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("The FXML file could not be found in the specified path.");
            }
            Parent root = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading registration.fxml", e);
            showAlert(Alert.AlertType.ERROR, SYSTEM_ERROR, "Failed to load registration.fxml page");
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
