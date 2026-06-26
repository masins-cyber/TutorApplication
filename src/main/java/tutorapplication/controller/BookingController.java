package tutorapplication.controller;

import tutorapplication.bean.BookingBean;
import tutorapplication.bean.LessonBean;
import tutorapplication.dao.*;
import tutorapplication.exception.LessonAlreadyBookedException;
import tutorapplication.exception.LessonAlreadyInsertedException;
import tutorapplication.exception.LessonsNotFoundException;
import tutorapplication.model.Booking;
import tutorapplication.model.Lesson;
import tutorapplication.others.FactoryDAO;

import java.util.ArrayList;
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

    public void addLesson(LessonBean lessonBean, String tutorEmail) throws LessonAlreadyInsertedException {
        Lesson lesson = new Lesson(lessonBean.getSubject(), lessonBean.getDay(), lessonBean.getTimeSlot(), lessonBean.getMaxPrice(), tutorEmail);
        lesson.setAvailable(true);

        lessonDAO.saveLesson(lesson);
    }

    public List<LessonBean> searchLessons(LessonBean searchBean) throws LessonsNotFoundException {
        List<Lesson> lessons = lessonDAO.findLessonsByFilters(searchBean.getSubject(), searchBean.getDay(), searchBean.getTimeSlot(), searchBean.getMaxPrice());

        if (lessons == null || lessons.isEmpty()) {
            throw new LessonsNotFoundException();
        }

        List<LessonBean> beanList = new ArrayList<>();
        for (Lesson l : lessons) {
            beanList.add(mapToLessonBean(l));
        }
        return beanList;
    }

    public LessonBean getLessonDetails(int id) {
        Lesson lesson = lessonDAO.findLessonById(id);
        if (lesson != null) {
            return mapToLessonBean(lesson);
        }
        return null;
    }

    public int bookLesson(BookingBean bookingBean) throws LessonAlreadyBookedException {
        Lesson lesson = lessonDAO.findLessonById(bookingBean.getId());
        if (lesson == null || !lesson.isAvailable()) {
            throw new LessonAlreadyBookedException(bookingBean.getId());
        }

        Booking booking = new Booking(bookingBean.getId(), bookingBean.getStudentEmail());
        int generatedBookingId = bookingDAO.saveBooking(booking);

        if (generatedBookingId != -1) {
            lessonDAO.updateLessonStatus(lesson, false);
            return generatedBookingId;
        }
        return -1;
    }

    public List<BookingBean> getPendingBookings(String tutorEmail) {
        List<Booking> bookings = bookingDAO.findPendingBookingsByTutor(tutorEmail);
        List<BookingBean> beanList = new ArrayList<>();
        for (Booking b : bookings) {
            Lesson l = lessonDAO.findLessonById(b.getId());
            beanList.add(mapToBookingBean(b, l));
        }
        return beanList;
    }

    public boolean processTutorDecision(int bookingId, int lessonId, String decision) {
        Booking booking = bookingDAO.findBookingById(bookingId);
        if (booking == null) {
            return false;
        }

        Lesson lesson = lessonDAO.findLessonById(lessonId);
        if (lesson != null && userDAO.existsByEmail(lesson.getTutorEmail())) {
            if (decision.equalsIgnoreCase("accept")) {
                return bookingDAO.updateBookingStatus(bookingId, "accepted");
            }
            else if (decision.equalsIgnoreCase("reject")) {
                boolean bookingUpdated = bookingDAO.updateBookingStatus(bookingId, "rejected");
                if (bookingUpdated) {
                    lessonDAO.updateLessonStatus(lesson, true);
                    return userDAO.existsByEmail(booking.getStudentEmail());
                }
            }
        }
        return false;
    }

    public List<BookingBean> getAllStudentBookings(String studentEmail) {
        List<Booking> bookings = bookingDAO.findAllBookingsByStudent(studentEmail);
        List<BookingBean> beanList = new ArrayList<>();
        for (Booking b : bookings) {
            Lesson l = lessonDAO.findLessonById(b.getId());
            beanList.add(mapToBookingBean(b, l));
        }
        return beanList;
    }

    public boolean cancelStudentBooking(int bookingId, int lessonId) {
        Lesson lesson = lessonDAO.findLessonById(lessonId);
        boolean bookingDeleted = bookingDAO.deleteBooking(bookingId);

        if (bookingDeleted && lesson != null) {
            lessonDAO.updateLessonStatus(lesson, true);
            return true;
        }
        return false;
    }

    private LessonBean mapToLessonBean(Lesson l) {
        LessonBean bean = new LessonBean();
        bean.setId(l.getId());
        bean.setSubject(l.getSubject());
        bean.setDay(l.getDate());
        bean.setTimeSlot(l.getTime());
        bean.setMaxPrice(l.getPrice());
        bean.setTutorEmail(l.getTutorEmail());
        bean.setAvailable(l.isAvailable());
        return bean;
    }

    private BookingBean mapToBookingBean(Booking b, Lesson l) {
        BookingBean bean = new BookingBean();
        bean.setBookingId(b.getBookingId());
        bean.setId(b.getId());
        bean.setStudentEmail(b.getStudentEmail());
        bean.setStatus(b.getStatus());
        if (l != null) {
            bean.setSubject(l.getSubject());
            bean.setTutorEmail(l.getTutorEmail());
            bean.setDate(l.getDate());
            bean.setTime(l.getTime());
        }
        return bean;
    }
}

