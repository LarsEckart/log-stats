package ee.larseckart.logstats;

import ee.larseckart.logstats.model.RequestInfo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public class LogFileLineParser implements Function<String, RequestInfo> {

    private static final String LINE_ITEM_SPLIT_ARGUMENT = " ";

    @Override
    public RequestInfo apply(String text) {
        final String[] lineItems = text.split(LINE_ITEM_SPLIT_ARGUMENT);

        final RequestInfo.Builder builder = new RequestInfo.Builder();

        parseDate(lineItems[0], builder);
        parseTimestamp(lineItems[1], builder);
        parseThreadId(lineItems[2], builder);
        parseUserContext(lineItems[3], builder);
        parseResource(lineItems[4], builder);
        parsePayloadElements(lineItems, builder);
        parseDuration(lineItems[lineItems.length - 1], builder);

        return builder.build();
    }

    private void parseDate(String lineItem, RequestInfo.Builder builder) {
        LocalDate date = LocalDate.parse(lineItem);
        builder.date(date);
    }

    private void parseTimestamp(String lineItem, RequestInfo.Builder builder) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");
        LocalTime time = LocalTime.parse(lineItem, format);
        builder.timestamp(time);
    }

    private void parseThreadId(String lineItem, RequestInfo.Builder builder) {
        final String rawThreadId = lineItem;
        final String threadId = rawThreadId.substring(1, rawThreadId.length() - 1);
        builder.threadId(threadId);
    }

    private void parseUserContext(String lineItem, RequestInfo.Builder builder) {
        final String rawUserContext = lineItem;
        String userContext = rawUserContext.substring(1, rawUserContext.length() - 1);
        builder.userContext(userContext);
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

    private boolean isUriWithQuery(String rawResource) {
        return rawResource.startsWith("/") && rawResource.contains("?");
    }

    // TODO: complicated piece of code
    private int determineEndOfAction(String rawResource) {
        final int actionKeyStart = rawResource.indexOf("action=");
        int actionStartIndex = actionKeyStart + 7;
        final String queryStringAfterAction = rawResource.substring(actionStartIndex, rawResource.length() - 1);
        final int actionEndIndex = queryStringAfterAction.indexOf("&");
        return actionStartIndex + actionEndIndex;
    }

    private void parsePayloadElements(String[] lineItems, RequestInfo.Builder builder) {
        for (int i = 5; !("in".equals(lineItems[i])); i++) {
            builder.payloadElement(lineItems[i]);
        }
    }

    private void parseDuration(String lineItem, RequestInfo.Builder builder) {
        builder.duration(Long.parseLong(lineItem));
    }
}
