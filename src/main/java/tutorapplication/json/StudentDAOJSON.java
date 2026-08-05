package tutorapplication.json;

import tutorapplication.dao.StudentDAO;
import tutorapplication.exception.EmailAlreadyInUseException;
import tutorapplication.exception.WrongCredentialsException;
import tutorapplication.model.Student;
import tutorapplication.others.BaseUserDAOJSON;
import tutorapplication.others.PasswordHasher;

public class StudentDAOJSON extends BaseUserDAOJSON<Student> implements StudentDAO {

    public StudentDAOJSON() {
        super("data/stud.txt", "STUDENT");
    }

    @Override
    protected String getEmail(Student user) {
        return user.getEmail();
    }

    @Override
    protected String getPassword(Student user) {
        return user.getPassword();
    }

    @Override
    protected Student buildUserFromBlock(String block, String email, String password, String name, String surname, String role) {
        String studentId = extractJsonValue(block, "student_id");
        return new Student(email, password, name, surname, role, studentId);
    }

    @Override
    protected String serializeSpecificFieldsToJson(Student user) {
        String finalStudentId = user.getStudentId() != null ? user.getStudentId() : "";
        return "\"student_id\":\"" + finalStudentId + "\"";
    }

    @Override
    protected String extractJsonValueFromObject(Student user, String fieldName) {
        return switch (fieldName) {
            case "name" -> user.getName();
            case "surname" -> user.getSurname();
            case "role" -> user.getRole();
            default -> "";
        };
    }

    @Override
    public Student findStudentByEmailAndPassword(String email, String password) throws WrongCredentialsException {
        return findByEmailAndPassword(email, password);
    }

    @Override
    public void saveStudent(Student student) throws EmailAlreadyInUseException {
        if (student == null) return;
        String hashedPassword = PasswordHasher.hashPassword(student.getPassword());
        Student studentToSave = new Student(student.getEmail(), hashedPassword, student.getName(), student.getSurname(), student.getRole(), student.getStudentId());
        save(student, studentToSave);
    }

    @Override
    public boolean existsByEmail(String email) {
        return super.existsByEmail(email);
    }
}