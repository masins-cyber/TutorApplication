package tutorapplication.json;

import tutorapplication.dao.TutorDAO;
import tutorapplication.exception.EmailAlreadyInUseException;
import tutorapplication.exception.WrongCredentialsException;
import tutorapplication.model.Tutor;
import tutorapplication.others.BaseUserDAOJSON;
import tutorapplication.others.PasswordHasher;

public class TutorDAOJSON extends BaseUserDAOJSON<Tutor> implements TutorDAO {

    public TutorDAOJSON() {
        super("data/tut.txt", "TUTOR");
    }

    @Override
    protected String getEmail(Tutor user) {
        return user.getEmail();
    }

    @Override
    protected String getPassword(Tutor user) {
        return user.getPassword();
    }

    @Override
    protected Tutor buildUserFromBlock(String block, String email, String password, String name, String surname, String role) {
        return new Tutor(email, password, name, surname, role);
    }

    @Override
    protected String serializeSpecificFieldsToJson(Tutor user) {
        return "";
    }

    @Override
    protected String extractJsonValueFromObject(Tutor user, String fieldName) {
        return switch (fieldName) {
            case "name" -> user.getName();
            case "surname" -> user.getSurname();
            case "role" -> user.getRole();
            default -> "";
        };
    }

    @Override
    public Tutor findTutorByEmailAndPassword(String email, String password) throws WrongCredentialsException {
        return findByEmailAndPassword(email, password);
    }

    @Override
    public void saveTutor(Tutor tutor) throws EmailAlreadyInUseException {
        if (tutor == null) return;
        String hashedPassword = PasswordHasher.hashPassword(tutor.getPassword());
        Tutor tutorToSave = new Tutor(tutor.getEmail(), hashedPassword, tutor.getName(), tutor.getSurname(), tutor.getRole());
        save(tutor, tutorToSave);
    }

    @Override
    public boolean existsByEmail(String email) {
        return super.existsByEmail(email);
    }
}