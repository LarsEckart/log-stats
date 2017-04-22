package ee.larseckart.logstats;

import ee.larseckart.logstats.model.RequestInfo;

import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LogFileParser {

    private final Function<String, RequestInfo> lineParser;

    public LogFileParser(Function<String, RequestInfo> lineParser) {
        this.lineParser = lineParser;
    }

    public List<RequestInfo> parse(String fileContent) {
        return Pattern.compile("\n")
                      .splitAsStream(fileContent)
                      .map(line -> this.lineParser.apply(line))
                      .collect(Collectors.toList());
    }
}
