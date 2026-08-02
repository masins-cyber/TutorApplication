package tutorapplication.dao;

import tutorapplication.exception.EmailAlreadyInUseException;
import tutorapplication.exception.WrongCredentialsException;
import tutorapplication.model.Tutor;

public interface TutorDAO {

    Tutor findTutorByEmailAndPassword(String email, String password) throws WrongCredentialsException;

    void saveTutor(Tutor tutor) throws EmailAlreadyInUseException;

    boolean existsByEmail(String email);
}
