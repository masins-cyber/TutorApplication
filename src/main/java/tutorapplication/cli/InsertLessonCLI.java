package tutorapplication.cli;

import tutorapplication.bean.SearchLessonBean;
import tutorapplication.controller.BookingController;
import tutorapplication.others.Print;
import tutorapplication.pattern.AbstractState;
import tutorapplication.pattern.StateMachineImpl;

import java.util.Scanner;

public class InsertLessonCLI extends AbstractState {

    @Override
    public void action(StateMachineImpl context) {
        display();

        Scanner scanner = new Scanner(System.in);
        SearchLessonBean lessonBean = new SearchLessonBean();

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
        } catch (NumberFormatException e) {
            Print.println("Error in the price format.");
            return;
        }

        BookingController bookingController = new BookingController();
        String tutorEmail = context.getSessionUser().getEmail();

        if (bookingController.addLesson(lessonBean, tutorEmail)) {
            Print.println("\n[SUCCESS] Lesson added successfully!");
        }
        else {
            Print.println("\n[ERROR] You can't insert this lesson! You already have an available lesson for the same day and time!");
        }

        goBack(context);
    }

    @Override
    public void display() {
        printHeader("Availability page");
    }
}
