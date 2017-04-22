package ee.larseckart.logstats;

import ee.larseckart.logstats.model.RequestInfo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LogFileParser {

    public List<RequestInfo> parse(String fileContent) {

        return Pattern.compile("\n")
                      .splitAsStream(fileContent)
                      .map(line -> parseLine(line))
                      .collect(Collectors.toList());
    }

    private RequestInfo parseLine(String line) {
        final Pattern splitArgument = Pattern.compile(" ");
        final String[] lineItems = line.split(splitArgument.pattern());

        final RequestInfo.Builder builder = new RequestInfo.Builder();

        LocalDate date = LocalDate.parse(lineItems[0]);
        builder.date(date);

        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");
        LocalTime time = LocalTime.parse(lineItems[1], format);
        builder.timestamp(time);

        return builder.build();
    }
}
