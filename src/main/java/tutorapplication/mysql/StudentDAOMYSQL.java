package tutorapplication.mysql;

import tutorapplication.dao.StudentDAO;
import tutorapplication.exception.EmailAlreadyInUseException;
import tutorapplication.exception.WrongCredentialsException;
import java.util.logging.Logger;
import java.util.logging.Level;

import tutorapplication.model.Student;
import tutorapplication.others.PasswordHasher;
import tutorapplication.others.QueryHelper;

import java.sql.*;

public class StudentDAOMYSQL implements StudentDAO {

    private static final Logger logger = Logger.getLogger(StudentDAOMYSQL.class.getName());
    private static final String COLUMN_PASSWORD = "password";

    @Override
    public Student findStudentByEmailAndPassword(String email, String password) throws WrongCredentialsException {
        String query = "SELECT email, password, name, surname, role, student_id FROM users WHERE email = ? AND role = 'STUDENT'";
        try {
            Student student = QueryHelper.executeQuery(query, stmt -> stmt.setString(1, email), rs -> {
                        if (rs.next()) {
                            String hashedPasswordFromDB = rs.getString(COLUMN_PASSWORD);
                            if (PasswordHasher.checkPassword(password, hashedPasswordFromDB)) {
                                return new Student(rs.getString("email"), rs.getString(COLUMN_PASSWORD), rs.getString("name"), rs.getString("surname"), rs.getString("role"), rs.getString("student_id"));
                            }
                        }
                        return null;
                    }
            );

            if (student != null) {
                return student;
            }

            throw new WrongCredentialsException();

        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error during student login authentication", e);
            return null;
        }
    }

    @Override
    public void saveStudent(Student student) throws EmailAlreadyInUseException {
        if (student == null) {
            return;
        }

        if (existsByEmail(student.getEmail())) {
            throw new EmailAlreadyInUseException(student.getEmail());
        }

        String query = "INSERT INTO users (email, password, name, surname, role, student_id) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            QueryHelper.executeUpdate(query, stmt -> {
                        stmt.setString(1, student.getEmail());
                        String encryptedPassword = PasswordHasher.hashPassword(student.getPassword());
                        stmt.setString(2, encryptedPassword);
                        stmt.setString(3, student.getName());
                        stmt.setString(4, student.getSurname());
                        stmt.setString(5, student.getRole());
                        stmt.setString(6, student.getStudentId());
                    }
            );
        }
        catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new EmailAlreadyInUseException(student.getEmail());
            }
            logger.log(Level.SEVERE, "Database error during user persistence/registration", e);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        String query = "SELECT 1 FROM users WHERE email = ?";
        try {
            Boolean exists = QueryHelper.executeQuery(query, stmt -> stmt.setString(1, email), ResultSet::next);
            return Boolean.TRUE.equals(exists);
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Database connection error during unique email validation check", e);
            return false;
        }
    }
}

