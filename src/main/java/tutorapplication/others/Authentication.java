package tutorapplication.others;

import tutorapplication.dao.UserDAO;
import tutorapplication.exception.WrongCredentialsException;
import tutorapplication.model.User;

public class Authentication {
    private UserDAO userDAO;

    public Authentication(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User login(String email, String password) throws WrongCredentialsException {
        User user = userDAO.findUserByEmailAndPassword(email, password);
        if (user != null) {
            return user;
        }
        return null;
    }
}