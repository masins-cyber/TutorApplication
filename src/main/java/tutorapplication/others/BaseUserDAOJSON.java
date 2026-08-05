package tutorapplication.others;

import tutorapplication.exception.EmailAlreadyInUseException;
import tutorapplication.exception.WrongCredentialsException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class BaseUserDAOJSON<T> {

    private static final Logger logger = Logger.getLogger(BaseUserDAOJSON.class.getName());
    private final String filePath;
    private final String roleFilter;

    protected BaseUserDAOJSON(String filePath, String roleFilter) {
        this.filePath = filePath;
        this.roleFilter = roleFilter;
    }

    protected abstract String getEmail(T user);
    protected abstract String getPassword(T user);
    protected abstract T buildUserFromBlock(String block, String email, String password, String name, String surname, String role);
    protected abstract String serializeSpecificFieldsToJson(T user);
    protected abstract String extractJsonValueFromObject(T user, String fieldName);

    protected List<T> loadUsersFromFile() {
        List<T> users = new ArrayList<>();
        Path path = Paths.get(filePath);

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

            for (String userBlock : userBlocks) {
                String block = userBlock.trim();
                if (!block.endsWith("}")) {
                    block = block + "}";
                }
                String email = extractJsonValue(block, "email");
                String password = extractJsonValue(block, "password");
                String name = extractJsonValue(block, "name");
                String surname = extractJsonValue(block, "surname");
                String role = extractJsonValue(block, "role");

                if (email != null && roleFilter.equalsIgnoreCase(role)) {
                    T user = buildUserFromBlock(block, email, password, name, surname, role);
                    if (user != null) {
                        users.add(user);
                    }
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "JSON Parsing Engine: Unable to read from local data file storage", e);
        }
        return users;
    }

    protected void saveUsersToFile(List<T> users) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[\n");
        for (int i = 0; i < users.size(); i++) {
            T user = users.get(i);
            jsonBuilder.append("  {");
            jsonBuilder.append("\"email\":\"").append(getEmail(user)).append("\",");
            jsonBuilder.append("\"password\":\"").append(getPassword(user)).append("\",");
            jsonBuilder.append("\"name\":\"").append(extractJsonValueFromObject(user, "name")).append("\",");
            jsonBuilder.append("\"surname\":\"").append(extractJsonValueFromObject(user, "surname")).append("\",");
            jsonBuilder.append("\"role\":\"").append(extractJsonValueFromObject(user, "role")).append("\"");

            String extraFields = serializeSpecificFieldsToJson(user);
            if (extraFields != null && !extraFields.isEmpty()) {
                jsonBuilder.append(",").append(extraFields);
            }

            jsonBuilder.append("}");
            if (i < users.size() - 1) {
                jsonBuilder.append(",\n");
            } else {
                jsonBuilder.append("\n");
            }
        }
        jsonBuilder.append("]");
        try {
            Files.writeString(Paths.get(filePath), jsonBuilder.toString());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "JSON Serialization Engine: Unable to write to local data file storage", e);
        }
    }

    protected String extractJsonValue(String block, String key) {
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

    public T findByEmailAndPassword(String email, String password) throws WrongCredentialsException {
        List<T> db = loadUsersFromFile();
        for (T u : db) {
            if (getEmail(u).equalsIgnoreCase(email) && PasswordHasher.checkPassword(password, getPassword(u))) {
                return u;
            }
        }
        throw new WrongCredentialsException();
    }

    public void save(T user, T userToSave) throws EmailAlreadyInUseException {
        if (user == null) {
            return;
        }
        if (existsByEmail(getEmail(user))) {
            throw new EmailAlreadyInUseException(getEmail(user));
        }

        List<T> db = loadUsersFromFile();
        db.add(userToSave);
        saveUsersToFile(db);
    }

    public boolean existsByEmail(String email) {
        List<T> db = loadUsersFromFile();
        for (T u : db) {
            if (getEmail(u).equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }
}