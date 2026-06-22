package testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tutorapplication.bean.BookingBean;
import tutorapplication.bean.SearchLessonBean;
import tutorapplication.bean.UserBean;
import tutorapplication.controller.BookingController;
import tutorapplication.controller.RegistrationController;
import tutorapplication.exception.EmailAlreadyInUseException;
import tutorapplication.exception.LessonAlreadyBookedException;
import tutorapplication.exception.LessonsNotFoundException;
import tutorapplication.model.Lesson;
import tutorapplication.others.Config;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private static final Logger logger = Logger.getLogger(UserTest.class.getName());
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        Config.setPersistenceType("mysql");
        bookingController = new BookingController();
    }

    @Test
    void testSearchLessonNonExistentFilters() {
        SearchLessonBean lessonBean = new SearchLessonBean();

        lessonBean.setSubject("NonExistentLesson");
        lessonBean.setDay("");
        lessonBean.setTimeSlot("");
        lessonBean.setMaxPrice(0.0);

        LessonsNotFoundException ex = assertThrows(LessonsNotFoundException.class, () -> bookingController.searchLessons(lessonBean), "The system should throw LessonsNotFoundException");

        assertEquals("No available lessons match the search criteria entered.", ex.getMessage());

        logger.log(Level.INFO, "Non-existent Filters Test: OK (LessonsNotFoundException correctly verified)");
    }

    @Test
    void testRequestDuplicateBooking() {
        RegistrationController registrationController = new RegistrationController();

        String tutorEmail = "simix@gmail.com";
        String studentEmail = "student_" + System.currentTimeMillis() + "@test.it";
        String uniqueTimeSlot = "T_" + (System.currentTimeMillis() % 10000);

        try {
            UserBean tutorBean = new UserBean();
            tutorBean.setEmail(tutorEmail);
            tutorBean.setPassword("simix");
            tutorBean.setName("Simone");
            tutorBean.setSurname("Tutor");
            tutorBean.setRole("TUTOR");
            registrationController.register(tutorBean);
        }
        catch (EmailAlreadyInUseException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        catch (Exception e) {
            logger.log(Level.WARNING, "Setup Tutor warning: " + e.getMessage());
        }

        SearchLessonBean lessonBean = new SearchLessonBean();
        lessonBean.setSubject("Test");
        lessonBean.setDay("MONDAY");
        lessonBean.setTimeSlot(uniqueTimeSlot);
        lessonBean.setMaxPrice(50.0);

        boolean lessonCreated = bookingController.addLesson(lessonBean, tutorEmail);
        assertTrue(lessonCreated, "The trial lesson must be successfully inserted into the DB.");

        int realLessonId = -1;
        try {
            List<Lesson> lessonsFound = bookingController.searchLessons(lessonBean);
            assertFalse(lessonsFound.isEmpty(), "The lesson must be findable through filters.");
            realLessonId = lessonsFound.getFirst().getId();
        }
        catch (Exception e) {
            fail("Failure to retrieve the inserted lesson ID: " + e.getMessage());
        }

        BookingBean bookingBean = new BookingBean();
        bookingBean.setId(realLessonId);
        bookingBean.setStudentEmail(studentEmail);

        assertDoesNotThrow(() -> {
            int bookingId = bookingController.bookLesson(bookingBean);
            assertNotEquals(-1, bookingId, "Your first reservation must generate a valid ID.");
        }, "The first booking on an available lesson should not raise exceptions.");

        assertThrows(LessonAlreadyBookedException.class, () -> bookingController.bookLesson(bookingBean), "The controller must prevent a second booking by raising LessonAlreadyBookedException.");

        logger.log(Level.INFO, "Duplicate Booking Test: OK (the controller successfully prevents double booking)");
    }
}

