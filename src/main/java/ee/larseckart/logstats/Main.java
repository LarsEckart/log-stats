package ee.larseckart.logstats;

import ee.larseckart.logstats.model.TimedResource;

import java.util.List;
import java.util.function.BiConsumer;

public class Main {

    public static void main(String[] args) {
        final Console console = new Console();
        final LogFileReader logFileReader = new LogFileReader();
        final LogFileLineParser lineParser = new LogFileLineParser();
        final LogFileParser fileParser = new LogFileParser(lineParser);
        final BiConsumer<Integer, List<TimedResource>> consumer = new AverageDurationCalculator(console);

        new LogStats(logFileReader, fileParser, console, consumer).start(args);
    }
}
