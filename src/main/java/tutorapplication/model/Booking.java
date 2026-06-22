package tutorapplication.model;

public class Booking {
    private int bookingId;
    private int id;
    private String studentEmail;
    private String status;

    public Booking(int id, String studentEmail) {
        this.id = id;
        this.studentEmail = studentEmail;
        this.status = "booked";
    }

    public Booking(int bookingId, int id, String studentEmail, String status) {
        this.bookingId = bookingId;
        this.id = id;
        this.studentEmail = studentEmail;
        this.status = status;
    }

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getStudentEmail() {
        return studentEmail;
    }
    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}

