package tutorapplication.pattern;

import tutorapplication.others.Print;

public abstract class AbstractState {
    protected StateMachine stateMachine;

    protected AbstractState(StateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    public void display() {}
    public void handleInput(String input) {}

    public void printHeader(String title) {
        Print.println("\n============");
        Print.println("Tutor Application - " + title.toUpperCase());
        Print.println("============");
    }
}


