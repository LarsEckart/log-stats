package ee.larseckart.logstats;

public class LogStats {

    private final Console console;

    public LogStats(Console console) {
        this.console = console;
    }

    public void start(String[] args) {
        if (hasNoArguments(args)) {
            printInfoMessage();
        } else {
            if (hasOneArgument(args)) {
                if (isHelpFlag(args[0])) {
                    printHelpMessage();
                } else {
                    printUnknownArgumentsMessage();
                }
            }
        }
    }

    private boolean isHelpFlag(String arg) {
        return "-h".equals(arg);
    }

    private boolean hasOneArgument(String[] args) {
        return args.length == 1;
    }

    private boolean hasNoArguments(String[] args) {
        return args.length == 0;
    }

    private void printInfoMessage() {
        this.console.printLine("No args provided, run with -h flag for help.");
    }

    private void printHelpMessage() {
        this.console.printLine("Provide 2 arguments.\n"
                + "First argument must be String s where s is the name of the log file.\n"
                + "Second argument must be a number n where n denotes how many resources to print out.");
    }

    private void printUnknownArgumentsMessage() {
        this.console.printLine("Unknown argument(s), run with -h flag for help.");
    }
}
