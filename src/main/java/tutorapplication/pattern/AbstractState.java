package tutorapplication.pattern;

import tutorapplication.others.Print;

import java.util.Scanner;

public abstract class AbstractState {

    private final Scanner scanner = new Scanner(System.in);

    protected AbstractState() {}

    public void entry(StateMachineImpl context) {}
    public void exit(StateMachineImpl context) {}

    protected void goBack(StateMachineImpl context) {
        context.goBack();
    }

    protected void goNext(StateMachineImpl context, AbstractState nextState) {
        context.transition(nextState);
    }

    public abstract void action(StateMachineImpl context);
    public void display() {}

    protected String showMenuAndGetInput() {
        display();
        return scanner.nextLine().trim();
    }

    public void printHeader(String title) {
        Print.println("\n============");
        Print.println("Tutor Application - " + title.toUpperCase());
        Print.println("============");
    }
}


