package ee.larseckart.logstats;

import ee.larseckart.logstats.model.RequestInfo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;
import java.util.regex.Pattern;

public class LogFileLineParser implements Function<String, RequestInfo> {

    @Override
    public RequestInfo apply(String text) {
        final Pattern splitArgument = Pattern.compile(" ");
        final String[] lineItems = text.split(splitArgument.pattern());

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

        parseResource(lineItems[4], builder);

        for (int i = 5; !("in".equals(lineItems[i])); i++) {
            builder.payloadElement(lineItems[i]);
        }

        builder.duration(Long.parseLong(lineItems[lineItems.length - 1]));

        return builder.build();
    }

    private void parseResource(String rawResource, RequestInfo.Builder builder) {
        if (isUriWithQuery(rawResource)) {
            if (rawResource.contains("?action=")) {
                int endOfAction = determineEndOfAction(rawResource);
                final String uriWithAction = rawResource.substring(0, endOfAction);
                builder.resource(uriWithAction);
            } else {
                final String uriWithoutQuery = rawResource.substring(0, rawResource.indexOf("?"));
                builder.resource(uriWithoutQuery);
            }
        } else {
            builder.resource(rawResource);
        }
    }

    // TODO: complicated piece of code
    private int determineEndOfAction(String rawResource) {
        final int actionKeyStart = rawResource.indexOf("action=");
        int actionStartIndex = actionKeyStart + 7;
        final String queryStringAfterAction = rawResource.substring(actionStartIndex, rawResource.length() - 1);
        final int actionEndIndex = queryStringAfterAction.indexOf("&");
        return actionStartIndex + actionEndIndex;
    }

    private boolean isUriWithQuery(String rawResource) {
        return rawResource.startsWith("/") && rawResource.contains("?");
    }
}
