package tutorapplication.pattern;

import tutorapplication.bean.UserBean;

import java.util.ArrayDeque;
import java.util.Deque;

public class StateMachineImpl implements StateMachine {
    private final Deque<AbstractState> stateHistory;
    private AbstractState currentState;
    private UserBean sessionUser;
    private boolean isRunning = true;

    public StateMachineImpl() {
        this.stateHistory = new ArrayDeque<>();
        this.currentState = new InitialState();
    }

    @Override
    public void run() {
        this.currentState.entry(this);
        while (isRunning) {
            goNext();
        }
    }

    @Override
    public void goNext() {
        if (currentState != null) {
            this.currentState.action(this);
        }
    }

    @Override
    public void goBack() {
        if (!stateHistory.isEmpty()) {
            this.currentState.exit(this);
            this.currentState = stateHistory.pop();
            this.currentState.entry(this);
        }
    }

    @Override
    public void transition(AbstractState nextState) {
        if (currentState != null) {
            currentState.exit(this);
            stateHistory.push(currentState);
        }
        currentState = nextState;
        currentState.entry(this);
    }

    public void terminate() {
        this.isRunning = false;
    }

    public UserBean getSessionUser() {
        return sessionUser;
    }

    public void setSessionUser(UserBean sessionUser) {
        this.sessionUser = sessionUser;
    }
}