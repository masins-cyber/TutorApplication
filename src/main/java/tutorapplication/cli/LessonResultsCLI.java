package tutorapplication.cli;

import tutorapplication.model.Lesson;
import tutorapplication.others.Print;
import tutorapplication.pattern.AbstractState;
import tutorapplication.pattern.StateMachine;

import java.util.List;
import java.util.Scanner;

public class LessonResultsCLI extends AbstractState {
    private final List<Lesson> lessonList;
    private final Scanner scanner = new Scanner(System.in);
    private final String studentEmail;


    public LessonResultsCLI(StateMachine stateMachine, List<Lesson> lessons, String email) {
        super(stateMachine);
        this.lessonList = lessons;
        this.studentEmail = email;
    }

    @Override
    public void display() {
        printHeader("Search Results");
        for (int i = 0; i < lessonList.size(); i++) {
            Lesson l = lessonList.get(i);
            Print.println("ID: " + l.getId() + " | Subject: " + l.getSubject() + " | Day: " + l.getDate() + " | Time: " + l.getTime() + " | Price: " + l.getPrice() + "€" + " | Tutor: " + l.getTutorEmail());
        }

        Print.println("\nOPTIONS:");
        Print.println("1) Book a lesson through the ID.");
        Print.println("2) Search for a new lesson.");
        Print.println("3) Go to the student page.");
        Print.print("Select an option: ");
    }

    @Override
    public void handleInput(String input) {
        switch (input) {
            case "1":
                Print.print("Insert the lesson ID that you want to book: ");
                int id = Integer.parseInt(scanner.nextLine().trim());
                try {
                    Print.println("Getting lesson with ID " + id);
                    stateMachine.setState(new ConfirmBookingCLI(stateMachine, id, this.studentEmail));
                }
                catch (NumberFormatException e) {
                    Print.println("\n[ERROR] Insert a valid Id number!");
                }
                break;
            case "2":
                Print.println("Going to the searching page!");
                stateMachine.setState(new SearchLessonCLI(stateMachine, this.studentEmail));
                break;
            case "3":
                Print.println("Going to the student page!");
                stateMachine.setState(new StudentHomeCLI(stateMachine, this.studentEmail));
                break;
            default:
                Print.println("Invalid option. Please try again.");
                break;
        }
    }
}
