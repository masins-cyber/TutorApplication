package tutorapplication.model;

public class Lesson {
    private int id;
    private String subject;
    private String date;
    private String time;
    private double price;
    private String tutorEmail;
    private boolean isAvailable;

    public Lesson(String subject, String date, String time, double price, String tutorEmail) {
        this.subject = subject;
        this.date = date;
        this.time = time;
        this.price = price;
        this.tutorEmail = tutorEmail;
        this.isAvailable = true;
    }

    public Lesson(int id, String subject, String day, String time, double price, String tutorEmail, boolean isAvailable) {
        this.id = id;
        this.subject = subject;
        this.date = day;
        this.time = time;
        this.price = price;
        this.tutorEmail = tutorEmail;
        this.isAvailable = isAvailable;
    }

    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getTutorEmail() {
        return tutorEmail;
    }
    public void setTutorEmail(String tutorEmail) {
        this.tutorEmail = tutorEmail;
    }
    public boolean isAvailable() {
        return isAvailable;
    }
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}
