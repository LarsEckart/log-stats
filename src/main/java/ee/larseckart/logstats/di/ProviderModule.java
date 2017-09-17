package ee.larseckart.logstats.di;

import dagger.Module;
import dagger.Provides;
import ee.larseckart.logstats.AverageDurationCalculator;
import ee.larseckart.logstats.Console;
import ee.larseckart.logstats.input.LogFileInput;
import ee.larseckart.logstats.input.LogFileLineParser;
import ee.larseckart.logstats.model.TimedResource;

import java.time.Clock;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Module
public class ProviderModule {

    @Provides
    Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Provides
    Function<String, TimedResource> logFileLineParser() {
        return new LogFileLineParser();
    }

    @Provides
    Function<String, List<TimedResource>> dataProvider(Function<String, TimedResource> logFileLineParser){
        return new LogFileInput(logFileLineParser);
    }

    @Provides
    BiConsumer<Integer, List<TimedResource>> topNconsumer(Console console){
        return new AverageDurationCalculator(console);
    }
}

