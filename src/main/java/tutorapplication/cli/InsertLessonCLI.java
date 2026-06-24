package tutorapplication.cli;

import tutorapplication.bean.LessonBean;
import tutorapplication.controller.BookingController;
import tutorapplication.exception.LessonAlreadyInsertedException;
import tutorapplication.others.Print;
import tutorapplication.pattern.AbstractState;
import tutorapplication.pattern.StateMachineImpl;

import java.util.Scanner;

public class InsertLessonCLI extends AbstractState {

    @Override
    public void action(StateMachineImpl context) {
        display();

        Scanner scanner = new Scanner(System.in);
        LessonBean lessonBean = new LessonBean();

        Print.print("Subject: ");
        String subject = scanner.nextLine().trim();
        if (subject.isEmpty()) {
            Print.println("\n[ERROR] Subject is mandatory! Please fill in all fields.");
            return;
        }
        lessonBean.setSubject(subject);

        Print.print("Day: ");
        String day = scanner.nextLine().trim().toUpperCase();
        if (day.isEmpty()) {
            Print.println("\n[ERROR] Day is mandatory! Please fill in all fields.");
            return;
        }
        lessonBean.setDay(day);

        Print.print("Time: ");
        String time = scanner.nextLine().trim();
        if (time.isEmpty()) {
            Print.println("\n[ERROR] Time is mandatory! Please fill in all fields.");
            return;
        }
        lessonBean.setTimeSlot(time);

        Print.print("Price: ");
        String priceStr = scanner.nextLine().trim();
        if (priceStr.isEmpty()) {
            Print.println("\n[ERROR] Price is mandatory! Please fill in all fields.");
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            lessonBean.setMaxPrice(price);
        } catch (NumberFormatException _) {
            Print.println("Error in the price format.");
            return;
        }

        BookingController bookingController = new BookingController();
        String tutorEmail = context.getSessionUser().getEmail();

        try {
            bookingController.addLesson(lessonBean, tutorEmail);
            Print.println("\n[SUCCESS] Lesson added successfully!");
        }
        catch (LessonAlreadyInsertedException e) {
            Print.println("\n[ERROR] " + e.getMessage());
        }
        goBack(context);
    }

    @Override
    public void display() {
        printHeader("Availability page");
    }
}
