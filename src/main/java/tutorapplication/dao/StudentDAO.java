package tutorapplication.dao;

import tutorapplication.exception.EmailAlreadyInUseException;
import tutorapplication.exception.WrongCredentialsException;
import tutorapplication.model.Student;

public interface StudentDAO {

    Student findStudentByEmailAndPassword(String email, String password) throws WrongCredentialsException;

    void saveStudent(Student student) throws EmailAlreadyInUseException;

    boolean existsByEmail(String email);
}

