package tutorapplication.mysql;

import tutorapplication.dao.TutorDAO;
import tutorapplication.exception.EmailAlreadyInUseException;
import tutorapplication.exception.WrongCredentialsException;
import java.util.logging.Logger;
import java.util.logging.Level;
import tutorapplication.model.Tutor;
import tutorapplication.others.Connect;
import tutorapplication.others.PasswordHasher;

import java.sql.*;

public class TutorDAOMYSQL implements TutorDAO {

    private static final Logger logger = Logger.getLogger(TutorDAOMYSQL.class.getName());
    private static final String COLUMN_PASSWORD = "password";

    @Override
    public Tutor findTutorByEmailAndPassword(String email, String password) throws WrongCredentialsException {
        String query = "SELECT email, password, name, surname, role FROM users WHERE email = ? AND role = 'TUTOR'";
        try (Connection conn = Connect.getInstance().getDBConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPasswordFromDB = rs.getString(COLUMN_PASSWORD);

                if (PasswordHasher.checkPassword(password, hashedPasswordFromDB)) {
                    return new Tutor(rs.getString("email"), rs.getString(COLUMN_PASSWORD), rs.getString("name"), rs.getString("surname"), rs.getString("role"));
                }
            }
            throw new WrongCredentialsException();
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error during tutor login authentication", e);
        }
        return null;
    }

    @Override
    public void saveTutor(Tutor tutor) throws EmailAlreadyInUseException {
        if (tutor == null) {
            return;
        }

        if (existsByEmail(tutor.getEmail())) {
            throw new EmailAlreadyInUseException(tutor.getEmail());
        }

        String query = "INSERT INTO users (email, password, name, surname, role, student_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Connect.getInstance().getDBConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, tutor.getEmail());
            String encryptedPassword = PasswordHasher.hashPassword(tutor.getPassword());
            stmt.setString(2, encryptedPassword);
            stmt.setString(3, tutor.getName());
            stmt.setString(4, tutor.getSurname());
            stmt.setString(5, tutor.getRole());
            stmt.setNull(6, Types.VARCHAR);

            stmt.executeUpdate();
        }
        catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new EmailAlreadyInUseException(tutor.getEmail());
            }
            logger.log(Level.SEVERE, "Database error during tutor persistence/registration", e);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        String query = "SELECT 1 FROM users WHERE email = ?";
        try (Connection conn = Connect.getInstance().getDBConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            return stmt.executeQuery().next();
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Database connection error during unique email validation check", e);
            return false;
        }
    }
}
