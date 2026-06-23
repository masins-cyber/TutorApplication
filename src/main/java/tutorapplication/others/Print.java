package tutorapplication.others;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class Print {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RESET1 = "\u001B[0m";


    private static final PrintWriter consoleWriter = new PrintWriter(System.out, true, StandardCharsets.UTF_8);

    private Print() {}

    public static void print(String message) {
        consoleWriter.print(message);
        consoleWriter.flush();
    }

    public static void println(String message) {
        consoleWriter.println(message);
    }

    public static void printlnBlu(String message) {
        consoleWriter.println(ANSI_BLUE + message + ANSI_RESET1);
    }

    public static void printBlu(String message) {
        consoleWriter.print(ANSI_BLUE + message + ANSI_RESET1);
        consoleWriter.flush();
    }

    public static void errorPrint(String message) {
        consoleWriter.println(ANSI_RED + message + ANSI_RESET);
    }
}
