package tutorapplication.bean;

public class BookingBean {
    private int id;
    private String studentEmail;

    public BookingBean() {
        // Default constructor for framework initialization
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getStudentEmail() {
        return studentEmail;
    }
    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }
}

