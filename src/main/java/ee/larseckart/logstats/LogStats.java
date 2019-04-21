package ee.larseckart.logstats;

import java.io.File;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LogStats {

    private final PrintStream printStream;

    public LogStats(PrintStream printStream) {
        this.printStream = printStream;
    }

    public void run(String[] args) {
        if (args.length == 0) {
            printStream.print("No arguments provided. Run program with -h flag for help.\n");
            return;
        }

        if ("-h".equals(args[0])) {
            printStream.print(
                    "Usage: 2 arguments required, filename to parse and number of entries to display.\nFor example: ~/Documents/timing.log 3\n");
            return;
        }

        int topN;
        try {
            topN = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            printStream.print("Illegal argument, second argument must be a number.\n");
            return;
        }

        Path path = Paths.get(args[0]);

        File f = path.toFile();
        if (!(f.exists() && f.isFile())) {
            printStream.print("Illegal argument, no file at " + f.getAbsolutePath() + ".\n");
            return;
        }

        printStream.print("Processing " + f.getName() + " for top " + topN + " requests\n");
    }
}
