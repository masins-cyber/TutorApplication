package tutorapplication.controller;

import tutorapplication.bean.LoginBean;
import tutorapplication.bean.UserBean;
import tutorapplication.dao.UserDAO;
import tutorapplication.exception.UserNotPresentException;
import tutorapplication.exception.WrongCredentialsException;
import tutorapplication.model.User;
import tutorapplication.others.FactoryDAO;

public class LoginController {
    private final UserDAO userDAO;

    public LoginController() {
        this.userDAO = FactoryDAO.getUserDAO();
    }

    public UserBean login(LoginBean loginBean) throws UserNotPresentException, WrongCredentialsException {

        if (!userDAO.existsByEmail(loginBean.getEmail())) {
            throw new UserNotPresentException(loginBean.getEmail());
        }
        User user = userDAO.findUserByEmailAndPassword(loginBean.getEmail(), loginBean.getPassword());

        if (user == null) {
            throw new WrongCredentialsException();
        }
        String exceptedRole = "STUDENT";
        if (loginBean.isTutor()) {
            exceptedRole = "TUTOR";
        }
        if (!user.getRole().equalsIgnoreCase(exceptedRole)) {
            throw new WrongCredentialsException();
        }

        UserBean authenticatedUserBean = new UserBean();
        authenticatedUserBean.setEmail(user.getEmail());
        authenticatedUserBean.setName(user.getName());
        authenticatedUserBean.setSurname(user.getSurname());
        authenticatedUserBean.setRole(user.getRole());
        authenticatedUserBean.setStudentId(user.getStudentId());

        return authenticatedUserBean;
    }
}

