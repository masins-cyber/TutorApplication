package tutorapplication.CLI;

import tutorapplication.bean.SearchLessonBean;
import tutorapplication.controller.BookingController;
import tutorapplication.pattern.AbstractState;
import tutorapplication.pattern.StateMachine;

import java.util.Scanner;

public class InsertLessonCLI extends AbstractState {
    private final Scanner scanner = new Scanner(System.in);
    private final String tutorEmail;

    public InsertLessonCLI(StateMachine stateMachine, String email) {
        super(stateMachine);
        this.tutorEmail = email;
    }

    @Override
    public void display() {
        printHeader("Availability page");

        SearchLessonBean lessonBean = new SearchLessonBean();

        System.out.print("Subject: ");
        String subject = scanner.nextLine().trim();
        if (subject.isEmpty()) {
            System.out.println("\n[ERROR] Subject is mandatory! Please fill in all fields.");
            stateMachine.setState(new InsertLessonCLI(stateMachine, this.tutorEmail));
            return;
        }
        lessonBean.setSubject(subject);

        System.out.print("Day: ");
        String day = scanner.nextLine().trim().toUpperCase();
        if (day.isEmpty()) {
            System.out.println("\n[ERROR] Day is mandatory! Please fill in all fields.");
            stateMachine.setState(new InsertLessonCLI(stateMachine, this.tutorEmail));
            return;
        }
        lessonBean.setDay(day);

        System.out.print("Time: ");
        String time = scanner.nextLine().trim();
        if (time.isEmpty()) {
            System.out.println("\n[ERROR] Time is mandatory! Please fill in all fields.");
            stateMachine.setState(new InsertLessonCLI(stateMachine, this.tutorEmail));
            return;
        }
        lessonBean.setTimeSlot(time);

        System.out.print("Price: ");
        String priceStr = scanner.nextLine().trim();
        if (priceStr.isEmpty()) {
            System.out.println("\n[ERROR] Price is mandatory! Please fill in all fields.");
            stateMachine.setState(new InsertLessonCLI(stateMachine, this.tutorEmail));
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            lessonBean.setMaxPrice(price);
        } catch (NumberFormatException e) {
            System.out.println("Error in the price format.");
            stateMachine.setState(new InsertLessonCLI(stateMachine, this.tutorEmail));
            return;
        }

        BookingController bookingController = new BookingController();
        if (bookingController.addLesson(lessonBean, this.tutorEmail)) {
            System.out.println("\n[SUCCESS] Lesson added successfully!");
        }
        else {
            System.out.println("\n[ERROR] You can't insert this lesson! You already have an available lesson for the same day and time!");
        }

        stateMachine.setState(new TutorHomeCLI(stateMachine, this.tutorEmail));
    }
}

