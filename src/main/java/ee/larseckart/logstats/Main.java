package ee.larseckart.logstats;

import ee.larseckart.logstats.input.LogFileInput;
import ee.larseckart.logstats.input.LogFileLineParser;
import ee.larseckart.logstats.input.LogFileReader;
import ee.larseckart.logstats.model.TimedResource;

import java.time.Clock;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Main {

    public static void main(String[] args) {
        final LogFileReader logFileReader = new LogFileReader();
        final LogFileLineParser logFileLineParser = new LogFileLineParser();
        final Function<String, List<TimedResource>> provider = new LogFileInput(logFileReader, logFileLineParser);

        final Console console = new Console();
        final BiConsumer<Integer, List<TimedResource>> consumer = new AverageDurationCalculator(console);

        final Clock clock = Clock.systemDefaultZone();

        new LogStats(clock, console, provider, consumer).execute(args);
    }
}
