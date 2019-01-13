package ee.larseckart.logstats.input;

import ee.larseckart.logstats.model.RequestInfo;
import ee.larseckart.logstats.model.TimedResource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LogFileLineParser implements Function<String, TimedResource> {

    private static final String LINE_ITEM_SEPARATOR = " ";
    private static final int DATE_INDEX = 0;
    private static final int TIMESTAMP_INDEX = 1;
    private static final int THREAD_ID_INDEX = 2;
    private static final int USER_CONTEXT_INDEX = 3;
    private static final int RESOURCE_INDEX = 4;
    private static final int PAYLOAD_ELEMENTS_INDEX = 5;

    private static final String ACTION_QUERY_PARAM = "action=";
    private static final String QUERY_SEPARATOR = "&";

    @Inject
    public LogFileLineParser() {
    }

    @Override
    public RequestInfo apply(String text) {
        String[] lineItems = text.split(LINE_ITEM_SEPARATOR);

        RequestInfo.Builder builder = new RequestInfo.Builder();

        parseDate(lineItems[DATE_INDEX], builder);
        parseTimestamp(lineItems[TIMESTAMP_INDEX], builder);
        parseThreadId(lineItems[THREAD_ID_INDEX], builder);
        parseUserContext(lineItems[USER_CONTEXT_INDEX], builder);
        parseResource(lineItems[RESOURCE_INDEX], builder);
        parsePayloadElements(lineItems, builder);
        int durationIndex = lineItems.length - 1;
        parseDuration(lineItems[durationIndex], builder);

        return builder.build();
    }

    private void parseDate(String lineItem, RequestInfo.Builder builder) {
        LocalDate date = LocalDate.parse(lineItem, DateTimeFormatter.ISO_LOCAL_DATE);
        builder.date(date);
    }

    private void parseTimestamp(String lineItem, RequestInfo.Builder builder) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");
        LocalTime time = LocalTime.parse(lineItem, format);
        builder.timestamp(time);
    }

    private void parseThreadId(String lineItem, RequestInfo.Builder builder) {
        String threadId = removeFirstAndLastCharacter(lineItem);
        builder.threadId(threadId);
    }

    private void parseUserContext(String lineItem, RequestInfo.Builder builder) {
        String userContext = removeFirstAndLastCharacter(lineItem);
        builder.userContext(userContext);
    }

    private String removeFirstAndLastCharacter(String text) {
        return text.substring(1, text.length() - 1);
    }

    private void parseResource(String rawResource, RequestInfo.Builder builder) {
        if (isUriWithQuery(rawResource)) {
            parseUriWithQuery(rawResource, builder);
        } else {
            builder.resource(rawResource);
        }
    }

    private boolean isUriWithQuery(String rawResource) {
        return rawResource.startsWith("/") && rawResource.contains("?");
    }

    private void parseUriWithQuery(String rawResource, RequestInfo.Builder builder) {
        if (isUriWithActionQuery(rawResource)) {
            parseUriWithActionQuery(rawResource, builder);
        } else {
            final String uriWithoutQuery = rawResource.substring(0, rawResource.indexOf("?"));
            builder.resource(uriWithoutQuery);
        }
    }

    private boolean isUriWithActionQuery(String rawResource) {
        return rawResource.contains(ACTION_QUERY_PARAM);
    }

    private void parseUriWithActionQuery(String rawResource, RequestInfo.Builder builder) {
        int endOfActionIndex = calculateEndOfAction(rawResource);
        String uriWithAction = rawResource.substring(0, endOfActionIndex);
        builder.resource(uriWithAction);
    }

    /** Assumes that action is always the first query parameter! */
    private int calculateEndOfAction(String rawResource) {
        int actionKeyStartIndex = rawResource.indexOf(ACTION_QUERY_PARAM);
        int offset = ACTION_QUERY_PARAM.length();
        int actionValueStartIndex = actionKeyStartIndex + offset;

        String uriAfterActionQueryKey = rawResource.substring(actionValueStartIndex, rawResource.length() - 1);
        if (hasQueryparamsAfterAction(uriAfterActionQueryKey)) {
            int actionValueEndIndex = uriAfterActionQueryKey.indexOf(QUERY_SEPARATOR);
            return actionValueStartIndex + actionValueEndIndex;
        } else {
            return rawResource.length();
        }
    }

    private boolean hasQueryparamsAfterAction(String uriAfterActionQueryKey) {
        return uriAfterActionQueryKey.contains(QUERY_SEPARATOR);
    }

    private void parsePayloadElements(String[] lineItems, RequestInfo.Builder builder) {
        for (int i = PAYLOAD_ELEMENTS_INDEX; !("in".equals(lineItems[i])); i++) {
            builder.payloadElement(lineItems[i]);
        }
    }

    private void parseDuration(String lineItem, RequestInfo.Builder builder) {
        builder.duration(Long.parseLong(lineItem));
    }
}
