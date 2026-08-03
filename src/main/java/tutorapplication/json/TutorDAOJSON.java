package tutorapplication.json;

import tutorapplication.dao.TutorDAO;
import tutorapplication.exception.EmailAlreadyInUseException;
import tutorapplication.exception.WrongCredentialsException;
import tutorapplication.model.Tutor;
import tutorapplication.others.PasswordHasher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class TutorDAOJSON implements TutorDAO {

    private static final String FILE_PATH = "data/tut.txt";
    private static final Logger logger = Logger.getLogger(TutorDAOJSON.class.getName());

    private List<Tutor> loadTutorsFromFile() {
        List<Tutor> tutors = new ArrayList<>();
        Path path = Paths.get(FILE_PATH);

        if (!Files.exists(path)) {
            return tutors;
        }

        try {
            String content = Files.readString(path).trim();
            if (content.isEmpty() || content.equals("[]")) {
                return tutors;
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

                if (email != null && "TUTOR".equalsIgnoreCase(role)) {
                    Tutor t = new Tutor(email, password, name, surname, role);
                    tutors.add(t);
                }
            }
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "JSON Parsing Engine: Unable to read from local data file storage", e);
        }
        return tutors;
    }

    private void saveTutorsToFile(List<Tutor> tutors) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[\n");
        for (int i = 0; i < tutors.size(); i++) {
            Tutor t = tutors.get(i);
            jsonBuilder.append("  {");
            jsonBuilder.append("\"email\":\"").append(t.getEmail()).append("\",");
            jsonBuilder.append("\"password\":\"").append(t.getPassword()).append("\",");
            jsonBuilder.append("\"name\":\"").append(t.getName()).append("\",");
            jsonBuilder.append("\"surname\":\"").append(t.getSurname()).append("\",");
            jsonBuilder.append("\"role\":\"").append(t.getRole()).append("\"");
            jsonBuilder.append("}");
            if (i < tutors.size() - 1) {
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
    public Tutor findTutorByEmailAndPassword(String email, String password) throws WrongCredentialsException {
        List<Tutor> db = loadTutorsFromFile();
        for (int i = 0; i < db.size(); i++) {
            Tutor t = db.get(i);
            if (t.getEmail().equalsIgnoreCase(email) && PasswordHasher.checkPassword(password, t.getPassword())) {
                return t;
            }
        }
        throw new WrongCredentialsException();
    }

    @Override
    public void saveTutor(Tutor tutor) throws EmailAlreadyInUseException {
        if (tutor == null) {
            return;
        }
        List<Tutor> db = loadTutorsFromFile();
        for (int i = 0; i < db.size(); i++) {
            Tutor t = db.get(i);
            if (t.getEmail().equalsIgnoreCase(tutor.getEmail())) {
                throw new EmailAlreadyInUseException(tutor.getEmail());
            }
        }
        if (!existsByEmail(tutor.getEmail())) {
            String hashedPassword = PasswordHasher.hashPassword(tutor.getPassword());

            Tutor tutorToSave = new Tutor(tutor.getEmail(), hashedPassword, tutor.getName(), tutor.getSurname(), tutor.getRole());

            db.add(tutorToSave);
            saveTutorsToFile(db);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        List<Tutor> db = loadTutorsFromFile();
        for (int i = 0; i < db.size(); i++) {
            Tutor t = db.get(i);
            if (t.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }
}