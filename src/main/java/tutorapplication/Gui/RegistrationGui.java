package tutorapplication.Gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import tutorapplication.bean.UserBean;
import tutorapplication.controller.RegistrationController;
import tutorapplication.exception.EmailAlreadyInUseException;
import tutorapplication.exception.InvalidEmailException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrationGui {

    private static final Logger logger = Logger.getLogger(RegistrationGui.class.getName());

    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private VBox studentIdContainer;
    @FXML
    private TextField studentIdField;

    @FXML
    private Button registerButton;

    @FXML
    public void initialize() {
        roleComboBox.setItems(FXCollections.observableArrayList("Student", "Tutor"));
    }

    @FXML
    void handleRoleSelection() {
        String selectedRole = roleComboBox.getValue();

        if ("Student".equals(selectedRole)) {
            studentIdContainer.setVisible(true);
            studentIdContainer.setManaged(true);
        }
        else {
            studentIdContainer.setVisible(false);
            studentIdContainer.setManaged(false);
            studentIdField.clear();
        }
    }

    @FXML
    void handleRegisterSubmit() {
        if (nameField.getText().isBlank() || surnameField.getText().isBlank() || emailField.getText().isBlank() || passwordField.getText().isBlank() || roleComboBox.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Missing Fields", "Please fill in all mandatory fields.");
            return;
        }
        if ("Student".equals(roleComboBox.getValue()) && studentIdField.getText().isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Missing Student ID", "Please insert your Student ID.");
            return;
        }

        try {
            UserBean userBean = new UserBean();
            userBean.setName(nameField.getText().trim());
            userBean.setSurname(surnameField.getText().trim());
            userBean.setEmail(emailField.getText().trim());
            userBean.setPassword(passwordField.getText());
            userBean.setRole(roleComboBox.getValue().toUpperCase());
            userBean.setStudentId(studentIdField.getText().trim());

            RegistrationController coreController = new RegistrationController();

            boolean success = coreController.register(userBean);

            if (success) {
                logger.log(Level.INFO, "Registration successful for user: ", userBean.getEmail());
                showAlert(Alert.AlertType.INFORMATION, "Success", "Registration completed! Back to Login.");
                changeScreen();
            } else {
                showAlert(Alert.AlertType.ERROR, "Database Error", "The system encountered an error saving the user. Please try again.");
            }

        } catch (InvalidEmailException e) {
            logger.log(Level.WARNING, "Invalid email", e);
            showAlert(Alert.AlertType.ERROR, "Invalid email", e.getMessage());

        } catch (EmailAlreadyInUseException e) {
            logger.log(Level.WARNING, "Email in use", e);
            showAlert(Alert.AlertType.ERROR, "Email In Use", e.getMessage());

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error during registration", e);
            showAlert(Alert.AlertType.ERROR, "System Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML
    void backToLogin() {
        changeScreen();
    }

    private void changeScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("Cannot find FXML file: /fxml/login.fxml");
            }

            Parent root = loader.load();
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();

            logger.log(Level.INFO, "Returned to the login screen successfully.");

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error switching screen to login.fxml", e);
            showAlert(Alert.AlertType.ERROR, "System Error", "Unable to load login page");
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
