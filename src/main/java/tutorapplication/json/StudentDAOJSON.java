package tutorapplication.json;

import tutorapplication.dao.StudentDAO;
import tutorapplication.exception.EmailAlreadyInUseException;
import tutorapplication.exception.WrongCredentialsException;
import tutorapplication.model.Student;
import tutorapplication.others.PasswordHasher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class StudentDAOJSON implements StudentDAO {

    private static final String FILE_PATH = "data/stud.txt";
    private static final Logger logger = Logger.getLogger(StudentDAOJSON.class.getName());

    private List<Student> loadStudentsFromFile() {
        List<Student> students = new ArrayList<>();
        Path path = Paths.get(FILE_PATH);

        if (!Files.exists(path)) {
            return students;
        }

        try {
            String content = Files.readString(path).trim();
            if (content.isEmpty() || content.equals("[]")) {
                return students;
            }
            content = content.substring(1, content.length() - 1).trim();
            String[] userBlocks = content.split("},");
            for (int i = 0; i < userBlocks.length; i++) {
                String block = userBlocks[i].trim();
                if (!block.endsWith("}")) {
                    block = block + "}";
                }
                String email = extractJsonValue(block, "email");
                String password = extractJsonValue(block, "password");
                String name = extractJsonValue(block, "name");
                String surname = extractJsonValue(block, "surname");
                String role = extractJsonValue(block, "role");
                String studentId = extractJsonValue(block, "student_id");

                if (email != null && "STUDENT".equalsIgnoreCase(role)) {
                    Student s = new Student(email, password, name, surname, role, studentId);
                    students.add(s);
                }
            }
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "JSON Parsing Engine: Unable to read from local data file storage", e);
        }
        return students;
    }

    private void saveStudentsToFile(List<Student> students) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[\n");
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            jsonBuilder.append("  {");
            jsonBuilder.append("\"email\":\"").append(s.getEmail()).append("\",");
            jsonBuilder.append("\"password\":\"").append(s.getPassword()).append("\",");
            jsonBuilder.append("\"name\":\"").append(s.getName()).append("\",");
            jsonBuilder.append("\"surname\":\"").append(s.getSurname()).append("\",");
            jsonBuilder.append("\"role\":\"").append(s.getRole()).append("\",");

            String finalStudentId;
            if (s.getStudentId() != null) {
                finalStudentId = s.getStudentId();
            }
            else {
                finalStudentId = "";
            }
            jsonBuilder.append("\"student_id\":\"").append(finalStudentId).append("\"");
            jsonBuilder.append("}");
            if (i < students.size() - 1) {
                jsonBuilder.append(",\n");
            }
            else {
                jsonBuilder.append("\n");
            }
        }
        jsonBuilder.append("]");
        try {
            Files.writeString(Paths.get(FILE_PATH), jsonBuilder.toString());
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "JSON Serialization Engine: Unable to write to local data file storage", e);
        }
    }

    private String extractJsonValue(String block, String key) {
        String searchKey = "\"" + key + "\":\"";
        int startIndex = block.indexOf(searchKey);
        if (startIndex == -1) {
            return null;
        }
        startIndex += searchKey.length();
        int endIndex = block.indexOf("\"", startIndex);
        if (endIndex == -1) {
            return null;
        }
        return block.substring(startIndex, endIndex);
    }

    @Override
    public Student findStudentByEmailAndPassword(String email, String password) throws WrongCredentialsException {
        List<Student> db = loadStudentsFromFile();
        for (int i = 0; i < db.size(); i++) {
            Student s = db.get(i);
            if (s.getEmail().equalsIgnoreCase(email) && PasswordHasher.checkPassword(password, s.getPassword())) {
                return s;
            }
        }
        throw new WrongCredentialsException();
    }

    @Override
    public void saveStudent(Student student) throws EmailAlreadyInUseException {
        if (student == null) {
            return;
        }
        List<Student> db = loadStudentsFromFile();
        for (int i = 0; i < db.size(); i++) {
            Student s = db.get(i);
            if (s.getEmail().equalsIgnoreCase(student.getEmail())) {
                throw new EmailAlreadyInUseException(student.getEmail());
            }
        }
        if (!existsByEmail(student.getEmail())) {
            String hashedPassword = PasswordHasher.hashPassword(student.getPassword());

            Student studentToSave = new Student(student.getEmail(), hashedPassword, student.getName(), student.getSurname(), student.getRole(), student.getStudentId());
            db.add(studentToSave);
            saveStudentsToFile(db);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        List<Student> db = loadStudentsFromFile();
        for (int i = 0; i < db.size(); i++) {
            Student s = db.get(i);
            if (s.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }
}
