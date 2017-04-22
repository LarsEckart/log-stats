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

        final String rawThreadId = lineItems[2];
        final String threadId = rawThreadId.substring(1, rawThreadId.length() - 1);
        builder.threadId(threadId);

        final String rawUserContext = lineItems[3];
        String userContext = rawUserContext.substring(1, rawUserContext.length() - 1);
        builder.userContext(userContext);
        // TODO: user context is optional, test what happens when log file contains []

        return builder.build();
    }
}
