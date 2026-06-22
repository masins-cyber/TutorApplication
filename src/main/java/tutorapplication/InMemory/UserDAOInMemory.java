package tutorapplication.InMemory;

import tutorapplication.dao.UserDAO;
import tutorapplication.exception.EmailAlreadyInUseException;
import tutorapplication.exception.UserNotPresentException;
import tutorapplication.exception.WrongCredentialsException;
import tutorapplication.model.User;
import tutorapplication.others.PasswordHasher;

import java.util.ArrayList;
import java.util.List;

public class UserDAOInMemory implements UserDAO {
    private static final List<User> usersTable = new ArrayList<>();

    @Override
    public User findUserByEmailAndPassword(String email, String password) throws WrongCredentialsException {
        for (int i = 0; i < usersTable.size(); i++) {
            User u = usersTable.get(i);
            if (u.getEmail().equalsIgnoreCase(email)) {
                if (PasswordHasher.checkPassword(password, u.getPassword())) {
                    return u;
                }
            }
        }
        throw new WrongCredentialsException();
    }

    @Override
    public User findUserByEmail(String email) throws UserNotPresentException {
        for (int i = 0; i < usersTable.size(); i++) {
            User u = usersTable.get(i);
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        throw new UserNotPresentException(email);
    }

    @Override
    public boolean saveUser(User user) throws EmailAlreadyInUseException {
        if (user == null) {
            return false;
        }

        for (int i = 0; i < usersTable.size(); i++) {
            User u = usersTable.get(i);

            if (u.getEmail().equalsIgnoreCase(user.getEmail())) {
                throw new EmailAlreadyInUseException(user.getEmail());
            }
        }

        User userToSave = new User(user.getEmail(), PasswordHasher.hashPassword(user.getPassword()), user.getName(), user.getSurname(), user.getRole());
        userToSave.setStudentId(user.getStudentId());
        usersTable.add(userToSave);
        return true;
    }

    @Override
    public boolean existsByEmail(String email) {
        for (int i = 0; i < usersTable.size(); i++) {
            User u = usersTable.get(i);
            if (u.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }
}
