package tutorapplication.CLI;

import tutorapplication.bean.SearchLessonBean;
import tutorapplication.controller.BookingController;
import tutorapplication.exception.LessonsNotFoundException;
import tutorapplication.model.Lesson;
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
        System.out.println("Fill in the filters to find a tutor.");

        SearchLessonBean searchBean = new SearchLessonBean();

        System.out.print("Subject you're searching for: ");
        searchBean.setSubject(scanner.nextLine().toLowerCase());

        System.out.print("Preferred day (Click enter for whatever day): ");
        searchBean.setDay(scanner.nextLine().toLowerCase());

        System.out.print("Preferred time (Click enter for whatever time): ");
        searchBean.setTimeSlot(scanner.nextLine());

        System.out.print("Your budget: ");
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
                System.out.println("\n[ERROR] Invalid price format! Budget set to maximum default.");
                searchBean.setMaxPrice(999.0);
            }
        }

        BookingController controller = new BookingController();
        try {
            List<Lesson> foundLessons = controller.searchLessons(searchBean);

            System.out.println("\n[SUCCESS] Results found!");
            stateMachine.setState(new LessonResultsCLI(stateMachine, foundLessons, this.studentEmail));
        }
        catch (LessonsNotFoundException e) {
            System.out.println("\n=================================================");
            System.out.println("[INFO] " + e.getMessage());
            System.out.println("[NOTICE] Returning to the student home menu.");
            System.out.println("=================================================");
            stateMachine.setState(new StudentHomeCLI(stateMachine, this.studentEmail));
        }
    }
}
