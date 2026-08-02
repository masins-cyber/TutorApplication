package tutorapplication.inmemory;

import tutorapplication.dao.StudentDAO;
import tutorapplication.exception.EmailAlreadyInUseException;
import tutorapplication.exception.WrongCredentialsException;
import tutorapplication.model.Student;
import tutorapplication.others.PasswordHasher;

import java.util.ArrayList;
import java.util.List;

public class StudentDAOInMemory implements StudentDAO {
    private static final List<Student> studentsTable = new ArrayList<>();

    @Override
    public Student findStudentByEmailAndPassword(String email, String password) throws WrongCredentialsException {
        for (int i = 0; i < studentsTable.size(); i++) {
            Student s = studentsTable.get(i);
            if ("STUDENT".equalsIgnoreCase(s.getRole()) && s.getEmail().equalsIgnoreCase(email) && PasswordHasher.checkPassword(password, s.getPassword())) {
                return s;
            }
        }
        throw new WrongCredentialsException();
    }

    @Override
    public void saveStudent(Student student) throws EmailAlreadyInUseException {
        if (student == null) {
            return;
        }

        for (int i = 0; i < studentsTable.size(); i++) {
            Student s = studentsTable.get(i);

            if (s.getEmail().equalsIgnoreCase(student.getEmail())) {
                throw new EmailAlreadyInUseException(student.getEmail());
            }
        }

        Student studentToSave = new Student(student.getEmail(), PasswordHasher.hashPassword(student.getPassword()), student.getName(), student.getSurname(), student.getRole(), student.getStudentId());
        studentsTable.add(studentToSave);
    }

    @Override
    public boolean existsByEmail(String email) {
        for (int i = 0; i < studentsTable.size(); i++) {
            Student s = studentsTable.get(i);
            if (s.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }
}

