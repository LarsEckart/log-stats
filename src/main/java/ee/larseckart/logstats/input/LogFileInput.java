package ee.larseckart.logstats.input;

import ee.larseckart.logstats.model.TimedResource;

import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LogFileInput implements Function<String, List<TimedResource>> {

    private final LogFileReader fileReader;
    private final Function<String, TimedResource> logFileLineParser;

    public LogFileInput(LogFileReader fileReader, Function<String, TimedResource> logFileLineParser) {
        this.fileReader = fileReader;
        this.logFileLineParser = logFileLineParser;
    }

    @Override
    public List<TimedResource> apply(String fileName) {
        final String fileContent = this.fileReader.read(fileName);
        return Pattern.compile("\n")
                      .splitAsStream(fileContent)
                      .map(line -> this.logFileLineParser.apply(line))
                      .collect(Collectors.toList());
    }
}
