package tutorapplication.controller;

import tutorapplication.bean.BookingBean;
import tutorapplication.bean.SearchLessonBean;
import tutorapplication.dao.*;
import tutorapplication.exception.LessonAlreadyBookedException;
import tutorapplication.exception.LessonsNotFoundException;
import tutorapplication.exception.UserNotPresentException;
import tutorapplication.model.Booking;
import tutorapplication.model.Lesson;
import tutorapplication.model.User;
import tutorapplication.others.FactoryDAO;

import java.util.List;

public class BookingController {
    private final LessonDAO lessonDAO;
    private final BookingDAO bookingDAO;
    private final UserDAO userDAO;

    public BookingController() {
        this.lessonDAO = FactoryDAO.getLessonDAO();
        this.bookingDAO = FactoryDAO.getBookingDAO();
        this.userDAO = FactoryDAO.getUserDAO();
    }

    public boolean addLesson(SearchLessonBean lessonBean, String tutorEmail) {
        Lesson lesson = new Lesson(lessonBean.getSubject(), lessonBean.getDay(), lessonBean.getTimeSlot(), lessonBean.getMaxPrice(), tutorEmail);
        lesson.setAvailable(true);
        return lessonDAO.saveLesson(lesson);
    }

    public List<Lesson> searchLessons(SearchLessonBean searchBean) throws LessonsNotFoundException{
        List<Lesson> lessons = lessonDAO.findLessonsByFilters(searchBean.getSubject(), searchBean.getDay(), searchBean.getTimeSlot(), searchBean.getMaxPrice());

        if (lessons == null || lessons.isEmpty()) {
            throw new LessonsNotFoundException();
        }
        return lessons;
    }

    public Lesson getLessonDetails(int id) {
        return lessonDAO.findLessonById(id);
    }

    public int bookLesson(BookingBean bookingBean) throws LessonAlreadyBookedException {
        Lesson lesson = lessonDAO.findLessonById(bookingBean.getId());
        if (lesson == null) {
            return -1;
        }

        if(!lesson.isAvailable()) {
            throw new LessonAlreadyBookedException(bookingBean.getId());
        }

        Booking booking = new Booking(bookingBean.getId(), bookingBean.getStudentEmail());
        int generatedBookingId = bookingDAO.saveBooking(booking);

        if(generatedBookingId != -1) {
            lessonDAO.updateLessonStatus(lesson, false);
            return generatedBookingId;
        }
        return -1;
    }

    public List<Booking> getPendingBookings(String tutorEmail) {
        return bookingDAO.findPendingBookingsByTutor(tutorEmail);
    }

    public boolean processTutorDecision(int bookingId, int id, String decision) throws UserNotPresentException {
        Booking booking = bookingDAO.findBookingById(bookingId);

        if (booking == null) {
            return false;
        }

        Lesson lesson = lessonDAO.findLessonById(id);
        if (lesson != null) {
            String tutorEmail = lesson.getTutorEmail();
            User tutorUser = userDAO.findUserByEmail(tutorEmail);

            if (tutorUser == null) {
                throw new UserNotPresentException(tutorEmail);
            }
        }

        if (decision.equalsIgnoreCase("accept")) {
            return bookingDAO.updateBookingStatus(bookingId, "accepted");
        }
        else if (decision.equalsIgnoreCase("reject")) {
            boolean bookingUpdated = bookingDAO.updateBookingStatus(bookingId, "rejected");

            if (bookingUpdated && lesson != null) {
                lessonDAO.updateLessonStatus(lesson, true);
                String studentEmail = booking.getStudentEmail();
                User studentUser = userDAO.findUserByEmail(studentEmail);

                if (studentUser == null) {
                    throw new UserNotPresentException(studentEmail);
                }
                return true;
            }
        }
        return false;
    }

    public List<Booking> getAllStudentBookings(String studentEmail) {
        return bookingDAO.findAllBookingsByStudent(studentEmail);
    }

    public boolean cancelStudentBooking(int bookingId, int id) {
        Lesson lesson = lessonDAO.findLessonById(id);
        boolean bookingDeleted = bookingDAO.deleteBooking(bookingId);

        if (bookingDeleted && lesson != null) {
            lessonDAO.updateLessonStatus(lesson, true);
            return true;
        }
        return false;
    }
}
