package tutorapplication.bean;

public class LoginBean {
    private String email;
    private String password;
    private boolean isTutor;

    public LoginBean() {
        // Default constructor for framework initialization
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public boolean isTutor() {
        return isTutor;
    }
    public void setTutor(boolean tutor) {
        isTutor = tutor;
    }
}

