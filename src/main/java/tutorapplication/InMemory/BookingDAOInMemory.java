package tutorapplication.InMemory;

import tutorapplication.dao.BookingDAO;
import tutorapplication.exception.LessonAlreadyBookedException;
import tutorapplication.model.Booking;
import tutorapplication.model.Lesson;

import java.util.ArrayList;
import java.util.List;

public class BookingDAOInMemory implements BookingDAO {
    private static final List<Booking> bookingsTable = new ArrayList<>();
    private static int bookingIdCounter = 1;

    @Override
    public int saveBooking(Booking booking) throws LessonAlreadyBookedException {
        if (booking == null) {
            return -1;
        }
        LessonDAOInMemory lessonDAO = new LessonDAOInMemory();
        tutorapplication.model.Lesson associatedLesson = lessonDAO.findLessonById(booking.getId());

        if (associatedLesson != null && !associatedLesson.isAvailable()) {
            throw new LessonAlreadyBookedException(booking.getId());
        }
        Booking bookingToSave = new Booking(booking.getId(), booking.getStudentEmail());
        bookingToSave.setStatus(booking.getStatus());

        int generatedId = bookingIdCounter++;
        bookingToSave.setBookingId(generatedId);

        bookingsTable.add(bookingToSave);
        return generatedId;
    }

    @Override
    public List<Booking> findPendingBookingsByTutor(String tutorEmail) {
        List<Booking> pendingBookings = new ArrayList<>();
        LessonDAOInMemory lessonDAO = new LessonDAOInMemory();

        for (int i = 0; i < bookingsTable.size(); i++) {
            Booking b = bookingsTable.get(i);
            if ("booked".equalsIgnoreCase(b.getStatus())) {
                Lesson l = lessonDAO.findLessonById(b.getId());
                if (l != null && l.getTutorEmail().equalsIgnoreCase(tutorEmail)) {
                    pendingBookings.add(b);
                }
            }
        }
        return pendingBookings;
    }

    @Override
    public boolean updateBookingStatus(int bookingId, String newStatus) {
        for (int i = 0; i < bookingsTable.size(); i++) {
            Booking b = bookingsTable.get(i);
            if (b.getBookingId() == bookingId) {
                b.setStatus(newStatus);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Booking> findAllBookingsByStudent(String studentEmail) {
        List<Booking> bookings = new ArrayList<>();
        for (int i = 0; i < bookingsTable.size(); i++) {
            Booking b = bookingsTable.get(i);
            if (b.getStudentEmail().equalsIgnoreCase(studentEmail)) {
                bookings.add(b);
            }
        }
        return bookings;
    }

    @Override
    public boolean deleteBooking(int bookingId) {
        for (int i = 0; i < bookingsTable.size(); i++) {
            Booking b = bookingsTable.get(i);
            if (b.getBookingId() == bookingId) {
                bookingsTable.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public Booking findBookingById(int bookingId) {
        for (int i = 0; i < bookingsTable.size(); i++) {
            Booking b = bookingsTable.get(i);
            if (b.getBookingId() == bookingId) {
                return b;
            }
        }
        return null;
    }
}
