package tutorapplication.pattern;

public abstract class AbstractState {
    protected StateMachine stateMachine;

    protected AbstractState(StateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    public void display() {}
    public void handleInput(String input) {}

    public void printHeader(String title) {
        System.out.println("\n============");
        System.out.println("Tutor Application - " + title.toUpperCase());
        System.out.println("============");
    }
}

