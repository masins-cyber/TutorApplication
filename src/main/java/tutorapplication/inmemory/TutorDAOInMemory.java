package tutorapplication.inmemory;

import tutorapplication.dao.TutorDAO;
import tutorapplication.exception.EmailAlreadyInUseException;
import tutorapplication.exception.WrongCredentialsException;
import tutorapplication.model.Tutor;
import tutorapplication.others.PasswordHasher;

import java.util.ArrayList;
import java.util.List;

public class TutorDAOInMemory implements TutorDAO {
    private static final List<Tutor> tutorsTable = new ArrayList<>();

    @Override
    public Tutor findTutorByEmailAndPassword(String email, String password) throws WrongCredentialsException {
        for (int i = 0; i < tutorsTable.size(); i++) {
            Tutor t = tutorsTable.get(i);
            if ("TUTOR".equalsIgnoreCase(t.getRole()) && t.getEmail().equalsIgnoreCase(email) && PasswordHasher.checkPassword(password, t.getPassword())) {
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

        for (int i = 0; i < tutorsTable.size(); i++) {
            Tutor t = tutorsTable.get(i);

            if (t.getEmail().equalsIgnoreCase(tutor.getEmail())) {
                throw new EmailAlreadyInUseException(tutor.getEmail());
            }
        }

        Tutor tutorToSave = new Tutor(tutor.getEmail(), PasswordHasher.hashPassword(tutor.getPassword()), tutor.getName(), tutor.getSurname(), tutor.getRole());
        tutorsTable.add(tutorToSave);
    }

    @Override
    public boolean existsByEmail(String email) {
        for (int i = 0; i < tutorsTable.size(); i++) {
            Tutor t = tutorsTable.get(i);
            if (t.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }
}
