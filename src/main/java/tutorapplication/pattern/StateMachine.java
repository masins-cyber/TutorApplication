package tutorapplication.pattern;

public interface StateMachine {
    void setState(AbstractState newState);
    void run();
}
