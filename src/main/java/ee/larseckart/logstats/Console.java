package ee.larseckart.logstats;

import java.io.File;
import java.io.PrintStream;
import java.time.Duration;

public class Console {

    private final PrintStream printStream;

    void print(String any) {
        this.printStream.print(any);
    }

    public Console(PrintStream printStream) {
        this.printStream = printStream;
    }

    void printNoArgs() {
        printStream.print("No arguments provided. Run program with -h flag for help.\n");
    }

    void printHelp() {
        printStream.print(
                "Usage: 2 arguments required, filename to parse and number of entries to display.\nFor example: ~/Documents/timing.log 3\n");
    }

    void printNaN() {
        printStream.print("Illegal argument, second argument must be a number.\n");
    }

    void printFileError(File f) {
        printStream.print("Illegal argument, no file at " + f.getAbsolutePath() + ".\n");
    }

    void printProcessing(int topN, File file) {
        printStream.print("Processing " + file.getName() + " for top " + topN + " requests\n");
    }

    void printProgramExecutionTime(Duration duration) {
        printStream.print("Execution time: " + duration.toMillis() + "ms");
    }

    void printBadLine(String line) {
        printStream.print("Invalid line: " + line);
    }

    void printStackTrace(Exception exception) {
        exception.printStackTrace(printStream);
    }
}
