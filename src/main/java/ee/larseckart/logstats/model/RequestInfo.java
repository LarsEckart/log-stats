package ee.larseckart.logstats.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class RequestInfo {

    private final LocalDate date;
    private final LocalTime timestamp;
    private final String threadId;
    private final String userContext;
    private final String uri;
    private final List<String> payloadElements;
    private final long duration;

    private RequestInfo(Builder builder) {
        this.date = builder.date;
        this.timestamp = builder.timestamp;
        this.threadId = builder.threadId;
        this.userContext = builder.userContext;
        this.uri = builder.uri;
        this.payloadElements = builder.payloadElements;
        this.duration = builder.duration;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public LocalTime getTimestamp() {
        return this.timestamp;
    }

    public String getThreadId() {
        return this.threadId;
    }

    public String getUserContext() {
        return this.userContext;
    }

    public String getUri() {
        return this.uri;
    }

    public List<String> getPayloadElements() {
        return this.payloadElements;
    }

    public long getDuration() {
        return this.duration;
    }

    public static class Builder {

        private LocalDate date;
        private LocalTime timestamp;
        private String threadId;
        private String userContext;
        private String uri;
        private List<String> payloadElements;
        private long duration;

        public Builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder timestamp(LocalTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder threadId(String threadId) {
            this.threadId = threadId;
            return this;
        }

        public Builder userContext(String userContext) {
            this.userContext = userContext;
            return this;
        }

        public Builder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder payloadElements(List<String> payloadElements) {
            this.payloadElements = payloadElements;
            return this;
        }

        public Builder duration(long duration) {
            this.duration = duration;
            return this;
        }

        public Builder fromPrototype(RequestInfo prototype) {
            this.date = prototype.date;
            this.timestamp = prototype.timestamp;
            this.threadId = prototype.threadId;
            this.userContext = prototype.userContext;
            this.uri = prototype.uri;
            this.payloadElements = prototype.payloadElements;
            this.duration = prototype.duration;
            return this;
        }

        public RequestInfo build() {
            return new RequestInfo(this);
        }
    }
}
