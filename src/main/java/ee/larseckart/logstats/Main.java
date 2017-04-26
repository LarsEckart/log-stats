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
        LogFileReader logFileReader = new LogFileReader();
        Function<String, TimedResource> logFileLineParser = new LogFileLineParser();
        Function<String, List<TimedResource>> provider = new LogFileInput(logFileReader, logFileLineParser);

        Console console = new Console();
        BiConsumer<Integer, List<TimedResource>> topNconsumer = new AverageDurationCalculator(console);

        Clock clock = Clock.systemDefaultZone();

        new LogStats(clock, console, provider, topNconsumer).execute(args);
    }
}
