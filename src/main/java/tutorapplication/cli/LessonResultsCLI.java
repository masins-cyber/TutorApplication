package tutorapplication.cli;

import tutorapplication.model.Lesson;
import tutorapplication.others.Print;
import tutorapplication.pattern.AbstractState;
import tutorapplication.pattern.StateMachineImpl;

import java.util.List;
import java.util.Scanner;

public class LessonResultsCLI extends AbstractState {
    private final List<Lesson> lessonList;

    public LessonResultsCLI(List<Lesson> lessons) {
        super();
        this.lessonList = lessons;
    }

    @Override
    public void action(StateMachineImpl context) {
        display();

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();

        switch (input) {
            case "1":
                Print.print("Insert the lesson ID that you want to book: ");
                try {
                    int id = Integer.parseInt(scanner.nextLine().trim());
                    Print.println("Getting lesson with ID " + id);
                    goNext(context, new ConfirmBookingCLI(id));
                }
                catch (NumberFormatException _) {
                    Print.println("\n[ERROR] Insert a valid Id number!");
                }
                break;
            case "2":
                Print.println("Going back to the searching page!");
                goBack(context);
                break;
            case "3":
                Print.println("Going to the student page...");
                goBack(context);
                goBack(context);
                break;
            default:
                Print.println("Invalid option. Please try again.");
                break;
        }
    }

    @Override
    public void display() {
        printHeader("Search Results");
        for (Lesson l : lessonList) {
            Print.println("ID: " + l.getId() + " | Subject: " + l.getSubject() + " | Day: " + l.getDate() + " | Time: " + l.getTime() + " | Price: " + l.getPrice() + "€" + " | Tutor: " + l.getTutorEmail());
        }
        Print.println("\nOPTIONS:");
        Print.println("1) Book a lesson through the ID.");
        Print.println("2) Search for a new lesson.");
        Print.println("3) Go to the student page.");
        Print.print("Select an option: ");
    }
}