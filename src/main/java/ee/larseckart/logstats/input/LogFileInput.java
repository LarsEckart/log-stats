package ee.larseckart.logstats.input;

import ee.larseckart.logstats.model.TimedResource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LogFileInput implements Function<String, List<TimedResource>> {

    private final Function<String, TimedResource> logFileLineParser;

    public LogFileInput(Function<String, TimedResource> logFileLineParser) {
        this.logFileLineParser = logFileLineParser;
    }

    @Override
    public List<TimedResource> apply(String fileName) {
        try {
            return Files.lines(Paths.get(fileName), StandardCharsets.UTF_8)
                        .map(line -> this.logFileLineParser.apply(line))
                        .collect(Collectors.toList());
        } catch (IOException exception) {
            throw new IllegalArgumentException("Could not read file '" + fileName + "'.", exception);
        }
    }
}
