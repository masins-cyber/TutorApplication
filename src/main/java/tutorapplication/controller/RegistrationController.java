package tutorapplication.controller;

import tutorapplication.bean.UserBean;
import tutorapplication.dao.UserDAO;
import tutorapplication.exception.EmailAlreadyInUseException;
import tutorapplication.exception.InvalidEmailException;
import tutorapplication.model.Student;
import tutorapplication.model.Tutor;
import tutorapplication.model.User;
import tutorapplication.others.FactoryDAO;

public class RegistrationController {
    private final UserDAO userDAO;

    public RegistrationController() {
        this.userDAO = FactoryDAO.getUserDAO();
    }

    public boolean register(UserBean userBean) throws EmailAlreadyInUseException, InvalidEmailException {

        if (userBean.getEmail() == null || !userBean.getEmail().contains("@") || !userBean.getEmail().contains(".")) {
            throw new InvalidEmailException(userBean.getEmail());
        }

        if (userDAO.existsByEmail(userBean.getEmail())) {
            throw new EmailAlreadyInUseException(userBean.getEmail());
        }

        User newUser;
        if ("STUDENT".equalsIgnoreCase(userBean.getRole())) {
            newUser = new Student(userBean.getEmail(), userBean.getPassword(), userBean.getName(), userBean.getSurname(), userBean.getRole(), userBean.getStudentId());
        }
        else {
            newUser = new Tutor(userBean.getEmail(), userBean.getPassword(), userBean.getName(), userBean.getSurname(), userBean.getRole());
        }
        return userDAO.saveUser(newUser);
    }
}

