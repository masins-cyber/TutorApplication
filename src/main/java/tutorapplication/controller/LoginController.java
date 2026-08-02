package tutorapplication.controller;

import tutorapplication.bean.LoginBean;
import tutorapplication.bean.UserBean;
import tutorapplication.dao.StudentDAO;
import tutorapplication.dao.TutorDAO;
import tutorapplication.exception.WrongCredentialsException;
import tutorapplication.model.Student;
import tutorapplication.model.Tutor;
import tutorapplication.model.User;
import tutorapplication.others.FactoryDAO;

public class LoginController {

    private final StudentDAO studentDAO;
    private final TutorDAO tutorDAO;

    public LoginController() {
        this.studentDAO = FactoryDAO.getStudentDAO();
        this.tutorDAO = FactoryDAO.getTutorDAO();
    }

    public UserBean login(LoginBean loginBean) throws WrongCredentialsException {

        boolean isTutor = loginBean.isTutor();
        User user;

        if (isTutor) {
            Tutor tutor = tutorDAO.findTutorByEmailAndPassword(loginBean.getEmail(), loginBean.getPassword());
            if (tutor == null) {
                throw new WrongCredentialsException();
            }
            user = tutor;
        } else {
            Student student = studentDAO.findStudentByEmailAndPassword(loginBean.getEmail(), loginBean.getPassword());
            if (student == null) {
                throw new WrongCredentialsException();
            }
            user = student;
        }

        String expectedRole;

        if (isTutor) {
            expectedRole = "TUTOR";
        }
        else {
            expectedRole = "STUDENT";
        }

        if (!user.getRole().equalsIgnoreCase(expectedRole)) {
            throw new WrongCredentialsException();
        }

        UserBean authenticatedUserBean = new UserBean();
        authenticatedUserBean.setEmail(user.getEmail());
        authenticatedUserBean.setName(user.getName());
        authenticatedUserBean.setSurname(user.getSurname());
        authenticatedUserBean.setRole(user.getRole());

        if (user instanceof Student student) {
            authenticatedUserBean.setStudentId(student.getStudentId());
        }

        return authenticatedUserBean;
    }
}

