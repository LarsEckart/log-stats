package ee.larseckart.logstats;

public class LogStats {

    private final Console console;

    public LogStats(Console console) {
        this.console = console;
    }

    public void run(String[] args) {
        Arguments arguments = new Arguments(args);
        if (arguments.isEmpty()) {
            console.printNoArgs();
            return;
        }

        if (arguments.isHelpFlag()) {
            console.printHelp();
            return;
        }

        if (!arguments.isTopNArgumentProvided()) {
            console.printNaN();
            return;
        }

        if (!arguments.isFileArgumentProvided()) {
            console.printFileError(arguments.file());
            return;
        }

        console.printProcessing(arguments.topN(), arguments.file());
    }
}
