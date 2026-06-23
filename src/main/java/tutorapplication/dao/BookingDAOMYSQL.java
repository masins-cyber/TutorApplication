package tutorapplication.dao;

import tutorapplication.exception.LessonAlreadyBookedException;
import tutorapplication.model.Booking;
import tutorapplication.others.Connect;

import java.sql.*;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class BookingDAOMYSQL implements BookingDAO {

    private static final Logger logger = Logger.getLogger(BookingDAOMYSQL.class.getName());
    private static final String COLUMN_LESSON_ID = "lesson_id";
    private static final String COLUMN_BOOKING_ID = "booking_id";
    private static final String COLUMN_STUDENT_EMAIL = "student_email";
    private static final String COLUMN_STATUS = "status";

    @Override
    public int saveBooking(Booking booking) throws LessonAlreadyBookedException {
        String checkQuery = "SELECT available FROM lessons WHERE id = ?";
        try (Connection conn = Connect.getInstance().getDBConnection(); PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, booking.getId());
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    boolean isAvailable = rs.getBoolean("available");
                    if (!isAvailable) {
                        throw new LessonAlreadyBookedException(booking.getId());
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error during lesson availability pre-check for booking ID: {0}", booking.getId());
            return -1;
        }

        String query = "INSERT INTO bookings (lesson_id, student_email, status) VALUES (?, ?, ?)";

        try (Connection conn = Connect.getInstance().getDBConnection(); PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, booking.getId());
            stmt.setString(2, booking.getStudentEmail());
            stmt.setString(3, booking.getStatus());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error while persisting new booking record", e);
        }
        return -1;
    }

    @Override
    public List<Booking> findPendingBookingsByTutor(String tutorEmail) {

        List<Booking> pendingBookings = new java.util.ArrayList<>();
        String query = "SELECT b.booking_id, b.lesson_id, b.student_email, b.status " + "FROM bookings b JOIN lessons l ON b.lesson_id = l.id " + "WHERE l.tutor_email = ? AND b.status = 'booked'";

        try (Connection conn = Connect.getInstance().getDBConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, tutorEmail);
            try (ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    Booking booking = new Booking(rs.getInt(COLUMN_BOOKING_ID), rs.getInt(COLUMN_LESSON_ID), rs.getString(COLUMN_STUDENT_EMAIL), rs.getString(COLUMN_STATUS));
                    pendingBookings.add(booking);
                }
            }
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error while retrieving pending bookings for tutor: {0}", tutorEmail);
        }
        return pendingBookings;
    }

    @Override
    public boolean updateBookingStatus(int bookingId, String newStatus) {
        String query = "UPDATE bookings SET status = ? WHERE booking_id = ?";
        try (Connection conn = Connect.getInstance().getDBConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, newStatus);
            stmt.setInt(2, bookingId);
            return stmt.executeUpdate() > 0;
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error while updating status for booking ID: {0}", bookingId);
            return false;
        }
    }


    @Override
    public List<Booking> findAllBookingsByStudent(String studentEmail) {
        List<Booking> bookings = new java.util.ArrayList<>();
        String query = "SELECT booking_id, lesson_id, student_email, status FROM bookings WHERE student_email = ?";

        try (Connection conn = Connect.getInstance().getDBConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, studentEmail);
            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    Booking booking = new Booking(rs.getInt(COLUMN_BOOKING_ID), rs.getInt(COLUMN_LESSON_ID), rs.getString(COLUMN_STUDENT_EMAIL), rs.getString(COLUMN_STATUS));
                    bookings.add(booking);
                }
            }
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error while fetching all bookings for student: {0}", studentEmail);
        }
        return bookings;
    }

    @Override
    public boolean deleteBooking(int bookingId) {
        String query = "DELETE FROM bookings WHERE booking_id = ?";

        try (Connection conn = Connect.getInstance().getDBConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookingId);

            return stmt.executeUpdate() > 0;
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error while deleting booking record with ID: {0}", bookingId);
            return false;
        }
    }

    @Override
    public Booking findBookingById(int bookingId) {
        String query = "SELECT booking_id, lesson_id, student_email, status FROM bookings WHERE booking_id = ?";

        try (Connection conn = Connect.getInstance().getDBConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Booking(rs.getInt(COLUMN_BOOKING_ID), rs.getInt(COLUMN_LESSON_ID), rs.getString(COLUMN_STUDENT_EMAIL), rs.getString(COLUMN_STATUS));
                }
            }
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error during single booking lookup for ID: {0}", bookingId);
        }
        return null;
    }
}

