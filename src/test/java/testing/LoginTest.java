package testing;

import tutorapplication.bean.LoginBean;
import tutorapplication.bean.UserBean;
import tutorapplication.controller.LoginController;
import tutorapplication.controller.RegistrationController;
import tutorapplication.exception.EmailAlreadyInUseException;
import tutorapplication.exception.WrongCredentialsException;
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

        assertDoesNotThrow(() -> {
            UserBean result = loginController.login(loginBean);
            assertNotNull(result, "Login failed (returned null). Please check your connection to MySQL.");
            assertEquals("Simone", result.getName(), "MySQL authenticated user name does not match.");
            Print.println("Test Login Success: OK (Authentication success on MySQL for " + result.getEmail() + ")");
        }, "The single login pipeline should succeed without exceptions.");
    }

    @Test
    void testLoginNonExistentUser() {
        LoginBean loginBean = new LoginBean();
        loginBean.setEmail("noexistaccount@test.it");
        loginBean.setPassword("whatever");
        loginBean.setTutor(false);

        assertThrows(WrongCredentialsException.class, () -> loginController.login(loginBean),
                "The system should throw WrongCredentialsException for non-existent accounts.");

        Print.println("User Not Present Test: OK (WrongCredentialsException successfully thrown)");
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
    void testRegistrationStudentFlow() {
        RegistrationController registration = new RegistrationController();

        String randomEmail = "student_" + System.currentTimeMillis() + "@test.it";

        UserBean student = new UserBean();
        student.setEmail(randomEmail);
        student.setPassword("student123");
        student.setName("Lando");
        student.setSurname("Norris");
        student.setRole("STUDENT");
        student.setStudentId("0399887");

        assertDoesNotThrow(() -> registration.register(student), "Registration of a new student should be successful");

        Print.println("Student Registration Test: OK for " + randomEmail);
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

        assertDoesNotThrow(() -> registration.register(tutor), "Registration of a new tutor should be successful");

        Print.println("Tutor Registration Test: OK for " + randomEmail);
    }
}