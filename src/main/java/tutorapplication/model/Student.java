package tutorapplication.model;

public class Student extends User {
    public Student(String email, String password, String name, String surname, String role, String studentId) {
        super(email, password, name, surname, role);
        this.studentId = studentId;
    }
}
