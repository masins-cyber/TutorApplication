package tutorapplication.dao;

import tutorapplication.exception.LessonsNotFoundException;
import tutorapplication.model.Lesson;
import tutorapplication.others.Connect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class LessonDAOMYSQL implements LessonDAO {

    private static final Logger logger = Logger.getLogger(LessonDAOMYSQL.class.getName());
    @Override
    public boolean saveLesson(Lesson lesson) {

        String query = "INSERT INTO lessons (subject, day, time_slot, price, tutor_email, available) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Connect.getInstance().getDBConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, lesson.getSubject());
            stmt.setString(2, lesson.getDate());
            stmt.setString(3, lesson.getTime());
            stmt.setDouble(4, lesson.getPrice());
            stmt.setString(5, lesson.getTutorEmail());
            stmt.setBoolean(6, true);

            return stmt.executeUpdate() > 0;

        }
        catch (SQLException e) {
            if(e.getErrorCode() == 1062) {
                logger.log(Level.WARNING, "Attempted to insert a duplicate lesson slot for tutor: {0}", lesson.getTutorEmail());
            }
            else {
                logger.log(Level.SEVERE, "Database error during lesson persistence process", e);
            }
            return false;
        }
    }

    @Override
    public List<Lesson> findLessonsByFilters(String subject, String date, String timeSlot, Double maxPrice) throws LessonsNotFoundException {

        List<Lesson> lessons = new ArrayList<>();

        String query = "SELECT id, subject, day, time_slot, price, tutor_email, available FROM lessons WHERE LOWER(subject) = LOWER(?) " + "AND (LOWER(day) = LOWER(?) OR ? = '') " + "AND (time_slot = ? OR ? = '') " + "AND price <= ? AND available = 1";

        try (Connection conn = Connect.getInstance().getDBConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, subject);
            stmt.setString(2, date);
            stmt.setString(3, date);
            stmt.setString(4, timeSlot);
            stmt.setString(5, timeSlot);

            double finalPrice = java.util.Objects.requireNonNullElse(maxPrice, 999.99);
            stmt.setDouble(6, finalPrice);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Lesson lesson = new Lesson(rs.getInt("id"), rs.getString("subject"), rs.getString("day"), rs.getString("time_slot"), rs.getDouble("price"), rs.getString("tutor_email"), rs.getBoolean("available"));
                lessons.add(lesson);
            }
            if (lessons.isEmpty()) {
                throw new LessonsNotFoundException();
            }
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error while retrieving filtered lessons query mapping", e);
        }
        return lessons;
    }

    public Lesson findLessonById(int lessonId) {

        String query = "SELECT id, subject, day, time_slot, price, tutor_email, available FROM lessons WHERE id = ?";

        try (Connection conn = Connect.getInstance().getDBConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, lessonId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Lesson(rs.getInt("id"), rs.getString("subject"), rs.getString("day"), rs.getString("time_slot"), rs.getDouble("price"), rs.getString("tutor_email"), rs.getBoolean("available"));
                }
            }
        } catch (SQLException _) {
            logger.log(Level.SEVERE, "Database error while looking up lesson with ID: {0}", lessonId);
        }
        return null;
    }

    @Override
    public void updateLessonStatus(Lesson lesson, boolean available) {

        String query = "UPDATE lessons SET available = ? WHERE tutor_email = ? AND day = ? AND time_slot = ?";

        try (Connection conn = Connect.getInstance().getDBConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setBoolean(1, available);
            stmt.setString(2, lesson.getTutorEmail());
            stmt.setString(3, lesson.getDate());
            stmt.setString(4, lesson.getTime());

            stmt.executeUpdate();

            lesson.setAvailable(available);

        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error while updating available flag status for lesson", e);
        }
    }
}

