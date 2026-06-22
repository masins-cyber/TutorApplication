package tutorapplication.dao;

import tutorapplication.exception.EmailAlreadyInUseException;
import tutorapplication.exception.UserNotPresentException;
import tutorapplication.exception.WrongCredentialsException;
import tutorapplication.model.User;
import tutorapplication.others.PasswordHasher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class UserDAOJSON implements UserDAO {

    private static final String FILE_PATH = "data/users.txt";
    private static final Logger logger = Logger.getLogger(UserDAOJSON.class.getName());

    private List<User> loadUsersFromFile() {
        List<User> users = new ArrayList<>();
        Path path = Paths.get(FILE_PATH);

        if (!Files.exists(path)) {
            return users;
        }

        try {
            String content = Files.readString(path).trim();
            if (content.isEmpty() || content.equals("[]")) {
                return users;
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
                if (email != null) {
                    User u = new User(email, password, name, surname, role);
                    if (studentId != null && !studentId.equals("null") && !studentId.isEmpty()) {
                        u.setStudentId(studentId);
                    }
                    users.add(u);
                }
            }
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "JSON Parsing Engine: Unable to read from local data file storage", e);
        }
        return users;
    }

    private void saveUsersToFile(List<User> users) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[\n");
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            jsonBuilder.append("  {");
            jsonBuilder.append("\"email\":\"").append(u.getEmail()).append("\",");
            jsonBuilder.append("\"password\":\"").append(u.getPassword()).append("\",");
            jsonBuilder.append("\"name\":\"").append(u.getName()).append("\",");
            jsonBuilder.append("\"surname\":\"").append(u.getSurname()).append("\",");
            jsonBuilder.append("\"role\":\"").append(u.getRole()).append("\",");

            String finalStudentId;
            if (u.getStudentId() != null) {
                finalStudentId = u.getStudentId();
            }
            else {
                finalStudentId = "";
            }
            jsonBuilder.append("\"student_id\":\"").append(finalStudentId).append("\"");
            jsonBuilder.append("}");
            if (i < users.size() - 1) {
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
    public User findUserByEmailAndPassword(String email, String password) throws WrongCredentialsException {
        List<User> db = loadUsersFromFile();
        for (int i = 0; i < db.size(); i++) {
            User u = db.get(i);
            if (u.getEmail().equalsIgnoreCase(email)) {
                if (PasswordHasher.checkPassword(password, u.getPassword())) {
                    return u;
                }
            }
        }
        throw new WrongCredentialsException();
    }

    @Override
    public boolean saveUser(User user) throws EmailAlreadyInUseException {
        if (user == null) {
            return false;
        }
        List<User> db = loadUsersFromFile();
        for (int i = 0; i < db.size(); i++) {
            User u = db.get(i);
            if (u.getEmail().equalsIgnoreCase(user.getEmail())) {
                throw new EmailAlreadyInUseException(user.getEmail());
            }
        }
        if (!existsByEmail(user.getEmail())) {
            String hashedPassword = PasswordHasher.hashPassword(user.getPassword());

            User userToSave = new User(user.getEmail(), hashedPassword, user.getName(), user.getSurname(), user.getRole());
            userToSave.setStudentId(user.getStudentId());

            db.add(userToSave);
            saveUsersToFile(db);
            return true;
        }
        return false;
    }

    @Override
    public boolean existsByEmail(String email) {
        List<User> db = loadUsersFromFile();
        for (int i = 0; i < db.size(); i++) {
            User u = db.get(i);
            if (u.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public User findUserByEmail(String email) throws UserNotPresentException {
        List<User> db = loadUsersFromFile();
        for (int i = 0; i < db.size(); i++) {
            User u = db.get(i);
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        throw new UserNotPresentException(email);
    }
}
