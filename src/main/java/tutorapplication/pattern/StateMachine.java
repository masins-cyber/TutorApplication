package tutorapplication.pattern;

public interface StateMachine {
    void goNext();
    void goBack();
    void transition(AbstractState nextState);
    void run();
}

