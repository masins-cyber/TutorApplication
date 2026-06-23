package tutorapplication.pattern;

import tutorapplication.others.Print;

public abstract class AbstractState {

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

    public void printHeader(String title) {
        Print.println("\n============");
        Print.println("Tutor Application - " + title.toUpperCase());
        Print.println("============");
    }
}


