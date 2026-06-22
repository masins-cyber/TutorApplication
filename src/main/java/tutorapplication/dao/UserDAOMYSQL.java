package tutorapplication.dao;

import tutorapplication.exception.EmailAlreadyInUseException;
import tutorapplication.exception.UserNotPresentException;
import tutorapplication.exception.WrongCredentialsException;
import java.util.logging.Logger;
import java.util.logging.Level;
import tutorapplication.model.User;
import tutorapplication.others.Connect;
import tutorapplication.others.PasswordHasher;

import java.sql.*;

public class UserDAOMYSQL implements UserDAO {

    private static final Logger logger = Logger.getLogger(UserDAOMYSQL.class.getName());
    @Override
    public User findUserByEmailAndPassword(String email, String password) throws WrongCredentialsException {
        String query = "SELECT * FROM users WHERE email = ?";
        try (java.sql.Connection conn = Connect.getInstance().getDBConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPasswordFromDB = rs.getString("password");

                if (PasswordHasher.checkPassword(password, hashedPasswordFromDB)) {
                    User user = new User(rs.getString("email"), rs.getString("password"), rs.getString("name"), rs.getString("surname"), rs.getString("role"));
                    user.setStudentId(rs.getString("student_id"));
                    return user;
                }
            }
            throw new WrongCredentialsException();
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error during user login authentication", e);
        }
        return null;
    }

    @Override
    public User findUserByEmail(String email) throws UserNotPresentException {
        String query = "SELECT * FROM users WHERE email = ?";
        try (java.sql.Connection conn = Connect.getInstance().getDBConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User(rs.getString("email"), rs.getString("password"), rs.getString("name"), rs.getString("surname"), rs.getString("role"));
                user.setStudentId(rs.getString("student_id"));
                return user;
            }
            throw new UserNotPresentException(email);
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error while searching user by email: " + email, e);
        }
        return null;
    }

    @Override
    public boolean saveUser(User user) throws EmailAlreadyInUseException {
        if (user == null) {
            return false;
        }

        if (existsByEmail(user.getEmail())) {
            throw new EmailAlreadyInUseException(user.getEmail());
        }

        String query = "INSERT INTO users (email, password, name, surname, role, student_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (java.sql.Connection conn = Connect.getInstance().getDBConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user.getEmail());
            String encryptedPassword = PasswordHasher.hashPassword(user.getPassword());
            stmt.setString(2, encryptedPassword);
            stmt.setString(3, user.getName());
            stmt.setString(4, user.getSurname());
            stmt.setString(5, user.getRole());

            if("STUDENT".equals(user.getRole())) {
                stmt.setString(6, user.getStudentId());
            }
            else if("TUTOR".equals(user.getRole())) {
                stmt.setNull(6, Types.VARCHAR);
            }

            return stmt.executeUpdate() > 0;
        }
        catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new EmailAlreadyInUseException(user.getEmail());
            }
            logger.log(Level.SEVERE, "Database error during user persistence/registration", e);
            return false;
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        String query = "SELECT 1 FROM users WHERE email = ?";
        try (java.sql.Connection conn = Connect.getInstance().getDBConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            return stmt.executeQuery().next();
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Database connection error during unique email validation check", e);
            return false;
        }
    }
}
