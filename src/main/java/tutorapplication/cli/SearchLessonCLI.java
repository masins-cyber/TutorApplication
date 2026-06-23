package tutorapplication.cli;

import tutorapplication.bean.SearchLessonBean;
import tutorapplication.controller.BookingController;
import tutorapplication.exception.LessonsNotFoundException;
import tutorapplication.model.Lesson;
import tutorapplication.others.Print;
import tutorapplication.pattern.AbstractState;
import tutorapplication.pattern.StateMachineImpl;

import java.util.List;
import java.util.Scanner;

public class SearchLessonCLI extends AbstractState {

    @Override
    public void action(StateMachineImpl context) {
        display();

        Scanner scanner = new Scanner(System.in);
        SearchLessonBean searchBean = new SearchLessonBean();

        Print.print("Subject you're searching for: ");
        searchBean.setSubject(scanner.nextLine().trim().toLowerCase());

        Print.print("Preferred day (Click enter for whatever day): ");
        searchBean.setDay(scanner.nextLine().trim().toLowerCase());

        Print.print("Preferred time (Click enter for whatever time): ");
        searchBean.setTimeSlot(scanner.nextLine().trim());

        Print.print("Your budget: ");
        String priceStr = scanner.nextLine().trim();
        if (priceStr.isEmpty()) {
            searchBean.setMaxPrice(999.0);
        } else {
            try {
                double price = Double.parseDouble(priceStr);
                searchBean.setMaxPrice(price);
            } catch (NumberFormatException _) {
                Print.println("\n[ERROR] Invalid price format! Budget set to maximum default.");
                searchBean.setMaxPrice(999.0);
            }
        }

        BookingController controller = new BookingController();
        try {
            List<Lesson> foundLessons = controller.searchLessons(searchBean);
            Print.println("\n[SUCCESS] Results found!");
            goNext(context, new LessonResultsCLI(foundLessons));
        }
        catch (LessonsNotFoundException e) {
            Print.println("\n=================================================");
            Print.println("[INFO] " + e.getMessage());
            Print.println("[NOTICE] Returning to the student home menu.");
            Print.println("=================================================");
            goBack(context);
        }
    }

    @Override
    public void display() {
        printHeader("Search Lesson");
        Print.println("Fill in the filters to find a tutor.");
    }
}