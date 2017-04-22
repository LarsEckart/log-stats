package ee.larseckart.logstats;

import ee.larseckart.logstats.model.TimedResource;

import java.util.List;
import java.util.function.BiConsumer;

public class LogStats {

    private final Console console;
    private final LogFileReader logFileReader;
    private final LogFileParser logFileParser;
    private final BiConsumer<Integer, List<TimedResource>> consumer;

    public LogStats(Console console, LogFileReader logFileReader, LogFileParser logFileParser, BiConsumer<Integer, List<TimedResource>> consumer) {
        this.console = console;
        this.logFileReader = logFileReader;
        this.logFileParser = logFileParser;
        this.consumer = consumer;
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
            } else if (hasTwoArguments(args)) {
                try {
                    int topN = Integer.parseInt(args[1]);
                    final String fileContent = this.logFileReader.read(args[0]);
                    final List<TimedResource> requestInfos = this.logFileParser.parse(fileContent);
                    this.consumer.accept(topN, requestInfos);
                } catch (NumberFormatException exception) {
                    printUnknownArgumentsMessage();
                }

            } else if (hasTooManyArguments(args)) {
                printUnknownArgumentsMessage();
            }
        }
    }

    private boolean hasNoArguments(String[] args) {
        return args.length == 0;
    }

    private void printInfoMessage() {
        this.console.printLine("No args provided, run with -h flag for help.");
    }

    private boolean hasOneArgument(String[] args) {
        return args.length == 1;
    }

    private boolean isHelpFlag(String arg) {
        return "-h".equals(arg);
    }

    private void printHelpMessage() {
        this.console.printLine("Provide 2 arguments.\n"
                + "First argument must be String s where s is the name of the log file.\n"
                + "Second argument must be a number n where n denotes how many resources to print out.");
    }

    private boolean hasTwoArguments(String[] args) {
        return args.length == 2;
    }

    private boolean hasTooManyArguments(String[] args) {
        return args.length > 2;
    }

    private void printUnknownArgumentsMessage() {
        this.console.printLine("Unknown argument(s), run with -h flag for help.");
    }
}
