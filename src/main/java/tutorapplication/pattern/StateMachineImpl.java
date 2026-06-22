package tutorapplication.pattern;

import java.util.Scanner;

public class StateMachineImpl implements StateMachine {
    private AbstractState currentState;
    private final Scanner scanner = new Scanner(System.in);
    private boolean isRunning = true;

    public StateMachineImpl() {
        this.currentState = new InitialState(this);
    }

    @Override
    public void run() {
        while (isRunning) {
            AbstractState stateBeforeDisplay = currentState;

            currentState.display();

            if(stateBeforeDisplay != currentState) {
                continue;
            }

            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                isRunning = false;
                System.out.println("Closing application...");
            }
            else {
                currentState.handleInput(input);
            }
        }
    }

    @Override
    public void setState(AbstractState newState) {
        this.currentState = newState;
    }

}

