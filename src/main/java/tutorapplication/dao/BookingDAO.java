package tutorapplication.dao;

import tutorapplication.exception.LessonAlreadyBookedException;
import tutorapplication.model.Booking;

import java.util.List;

public interface BookingDAO {

    int saveBooking(Booking booking) throws LessonAlreadyBookedException;

    List<Booking> findAllBookingsByStudent(String studentEmail);

    List<Booking> findPendingBookingsByTutor(String tutorEmail);

    boolean updateBookingStatus(int bookingId, String newStatus);

    boolean deleteBooking(int bookingId);

    Booking findBookingById(int bookingId);
}
