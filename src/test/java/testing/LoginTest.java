package testing;

import tutorapplication.bean.LoginBean;
import tutorapplication.bean.UserBean;
import tutorapplication.controller.LoginController;
import tutorapplication.controller.RegistrationController;
import tutorapplication.exception.EmailAlreadyInUseException;
import tutorapplication.exception.UserNotPresentException;
import tutorapplication.exception.WrongCredentialsException;
import tutorapplication.model.User;
import tutorapplication.others.Config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tutorapplication.others.Print;

import static org.junit.jupiter.api.Assertions.*;

class LoginTest {
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        Config.setPersistenceType("mysql");
        loginController = new LoginController();
    }

    @Test
    void testLoginSuccess() {

        LoginBean loginBean = new LoginBean();
        loginBean.setEmail("simix@gmail.com");
        loginBean.setPassword("simix");
        loginBean.setTutor(true);

        try {
            User result = loginController.login(loginBean);

            assertNotNull(result, "Login failed (returned null). Please check your connection to MySQL.");

            assertEquals("Simone", result.getName(), "MySQL authenticated user name does not match.");

            Print.println("Test Login Success: OK (Authentication success on MySQL for " + result.getEmail() + ")");

        }
        catch (Exception e) {
            fail("The test threw an unexpected exception: " + e.getMessage());
        }
    }

    @Test
    void testLoginWrongCredentials() {

        LoginBean loginBean = new LoginBean();
        loginBean.setEmail("simix@gmail.com");
        loginBean.setPassword("simix");
        loginBean.setTutor(false);

        assertThrows(WrongCredentialsException.class, () -> loginController.login(loginBean), "The controller had to block access because the declared role does not match the DB.");

        Print.println("Wrong Credentials Test: OK (WrongCredentialsException successfully thrown)");
    }

    @Test
    void testLoginUserNotPresent() {
        LoginBean loginBean = new LoginBean();
        loginBean.setEmail("noexistaccount@test.it");
        loginBean.setPassword("whatever");
        loginBean.setTutor(false);

        assertThrows(UserNotPresentException.class, () -> loginController.login(loginBean), "The system should have thrown UserNotPresentException for an unregistered email.");

        Print.println("User Not Present Test: OK (UserNotPresentException successfully thrown)");
    }

    @Test
    void testRegistrationEmailAlreadyInUse() {

        RegistrationController registration = new RegistrationController();

        UserBean userBean = new UserBean();
        userBean.setEmail("simix@gmail.com");
        userBean.setPassword("new");
        userBean.setName("Charles");
        userBean.setSurname("Leclerc");
        userBean.setRole("STUDENT");
        userBean.setStudentId("0310756");

        assertThrows(EmailAlreadyInUseException.class, () -> registration.register(userBean), "The controller should prevent the recording of an existing email");

        Print.println("Duplicate Registration Test: OK (EmailAlreadyInUseException thrown)");
    }

    @Test
    void testRegistrationTutorFlow() {
        RegistrationController registration = new RegistrationController();

        String randomEmail = "tutor_" + System.currentTimeMillis() + "@test.it";

        UserBean tutor = new UserBean();
        tutor.setEmail(randomEmail);
        tutor.setPassword("new1");
        tutor.setName("LeBron");
        tutor.setSurname("James");
        tutor.setRole("TUTOR");

        assertDoesNotThrow(() -> {
            registration.register(tutor);
        }, "Registration of a new instructor should be successful");

        Print.println("Instructor Registration Test: OK for " + randomEmail);
    }
}
