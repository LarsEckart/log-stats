package ee.larseckart.logstats;

import ee.larseckart.logstats.model.TimedResource;

import java.time.Clock;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class LogStats {

    private final Clock clock;
    private final Console console;
    private final Function<String, List<TimedResource>> provider;
    private final BiConsumer<Integer, List<TimedResource>> consumer;

    public LogStats(
            Clock clock, Console console,
            Function<String, List<TimedResource>> provider, BiConsumer<Integer, List<TimedResource>> consumer)
    {
        this.clock = clock;
        this.console = console;
        this.provider = provider;
        this.consumer = consumer;
    }

    public void execute(String[] args) {
        final long start = this.clock.millis();
        if (hasNoArguments(args)) {
            printInfoMessage();
        } else {
            processArguments(args);
        }
        final long duration = this.clock.millis() - start;
        this.console.printLine("Execution took " + duration + " milliseconds.");
    }

    private boolean hasNoArguments(String[] args) {
        return args.length == 0;
    }

    private void printInfoMessage() {
        this.console.printLine("No args provided, run with -h flag for help.");
    }

    private void processArguments(String[] args) {
        if (hasOneArgument(args)) {
            processOneArgument(args[0]);
        } else if (hasTwoArguments(args)) {
            processTwoArguments(args);
        } else {
            processTooManyArguments();
        }
    }

    private boolean hasOneArgument(String[] args) {
        return args.length == 1;
    }

    private void processOneArgument(String arg) {
        if (isHelpFlag(arg)) {
            printHelpMessage();
        } else {
            printUnknownArgumentsMessage();
        }
    }

    private boolean isHelpFlag(String arg) {
        return "-h".equals(arg);
    }

    private void printHelpMessage() {
        this.console.printLine("Provide 2 arguments.");
        this.console.printLine("First argument must be String s where s is the name of the log file.");
        this.console.printLine("Second argument must be a number n where n denotes how many resources to print out.");
    }

    private boolean hasTwoArguments(String[] args) {
        return args.length == 2;
    }

    private void processTwoArguments(String[] args) {
        final String fileName = args[0];
        final String rawTopN = args[1];
        try {
            int topN = Integer.parseInt(rawTopN);
            final List<TimedResource> timedResources = this.provider.apply(fileName);
            this.consumer.accept(topN, timedResources);
        } catch (NumberFormatException exception) {
            printUnknownArgumentsMessage();
        } catch (IllegalArgumentException exception) {
            printFileErrorMessage(exception.getMessage());
        }
    }

    private void printUnknownArgumentsMessage() {
        this.console.printLine("Unknown argument(s), run with -h flag for help.");
    }

    private void printFileErrorMessage(String errorMessage) {
        this.console.printLine(errorMessage);
    }

    private void processTooManyArguments() {
        printUnknownArgumentsMessage();
    }
}
