package tutorapplication.dao;

import tutorapplication.exception.EmailAlreadyInUseException;
import tutorapplication.exception.UserNotPresentException;
import tutorapplication.exception.WrongCredentialsException;
import tutorapplication.model.User;

public interface UserDAO {

    User findUserByEmailAndPassword(String email, String password) throws WrongCredentialsException;

    boolean saveUser(User user) throws EmailAlreadyInUseException;

    boolean existsByEmail(String email);

    User findUserByEmail(String email) throws UserNotPresentException;
}
