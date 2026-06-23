package tutorapplication.cli;

import tutorapplication.bean.SearchLessonBean;
import tutorapplication.controller.BookingController;
import tutorapplication.exception.LessonsNotFoundException;
import tutorapplication.model.Lesson;
import tutorapplication.others.Print;
import tutorapplication.pattern.AbstractState;

import tutorapplication.pattern.StateMachine;

import java.util.List;
import java.util.Scanner;

public class SearchLessonCLI extends AbstractState {
    private final Scanner scanner = new Scanner(System.in);
    private final String studentEmail;

    public SearchLessonCLI(StateMachine stateMachine, String email) {
        super(stateMachine);
        this.studentEmail = email;
    }

    @Override
    public void display() {
        printHeader("Search Lesson");
        Print.println("Fill in the filters to find a tutor.");

        SearchLessonBean searchBean = new SearchLessonBean();

        Print.print("Subject you're searching for: ");
        searchBean.setSubject(scanner.nextLine().toLowerCase());

        Print.print("Preferred day (Click enter for whatever day): ");
        searchBean.setDay(scanner.nextLine().toLowerCase());

        Print.print("Preferred time (Click enter for whatever time): ");
        searchBean.setTimeSlot(scanner.nextLine());

        Print.print("Your budget: ");
        String priceStr = scanner.nextLine();
        if (priceStr.isEmpty()) {
            searchBean.setMaxPrice(999.0);
        }
        else {
            try {
                double price = Double.parseDouble(priceStr);
                searchBean.setMaxPrice(price);
            }
            catch (NumberFormatException e) {
                Print.println("\n[ERROR] Invalid price format! Budget set to maximum default.");
                searchBean.setMaxPrice(999.0);
            }
        }

        BookingController controller = new BookingController();
        try {
            List<Lesson> foundLessons = controller.searchLessons(searchBean);

            Print.println("\n[SUCCESS] Results found!");
            stateMachine.setState(new LessonResultsCLI(stateMachine, foundLessons, this.studentEmail));
        }
        catch (LessonsNotFoundException e) {
            Print.println("\n=================================================");
            Print.println("[INFO] " + e.getMessage());
            Print.println("[NOTICE] Returning to the student home menu.");
            Print.println("=================================================");
            stateMachine.setState(new StudentHomeCLI(stateMachine, this.studentEmail));
        }
    }
}

