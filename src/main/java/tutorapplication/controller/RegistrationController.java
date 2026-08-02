package tutorapplication.controller;

import tutorapplication.bean.UserBean;
import tutorapplication.dao.StudentDAO;
import tutorapplication.dao.TutorDAO;
import tutorapplication.exception.EmailAlreadyInUseException;
import tutorapplication.exception.InvalidEmailException;
import tutorapplication.model.Student;
import tutorapplication.model.Tutor;
import tutorapplication.others.FactoryDAO;

public class RegistrationController {

    private final StudentDAO studentDAO;
    private final TutorDAO tutorDAO;

    public RegistrationController() {
        this.studentDAO = FactoryDAO.getStudentDAO();
        this.tutorDAO = FactoryDAO.getTutorDAO();
    }

    public boolean register(UserBean userBean) throws EmailAlreadyInUseException, InvalidEmailException {

        if (userBean.getEmail() == null || !userBean.getEmail().contains("@") || !userBean.getEmail().contains(".")) {
            throw new InvalidEmailException(userBean.getEmail());
        }

        if (studentDAO.existsByEmail(userBean.getEmail()) || tutorDAO.existsByEmail(userBean.getEmail())) {
            throw new EmailAlreadyInUseException(userBean.getEmail());
        }

        if ("STUDENT".equalsIgnoreCase(userBean.getRole())) {
            Student newStudent = new Student(userBean.getEmail(), userBean.getPassword(), userBean.getName(), userBean.getSurname(), userBean.getRole(), userBean.getStudentId());
            studentDAO.saveStudent(newStudent);
        }
        else {
            Tutor newTutor = new Tutor(userBean.getEmail(), userBean.getPassword(), userBean.getName(), userBean.getSurname(), userBean.getRole());
            tutorDAO.saveTutor(newTutor);
        }
        return true;
    }
}

