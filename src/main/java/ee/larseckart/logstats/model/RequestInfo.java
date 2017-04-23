package ee.larseckart.logstats.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RequestInfo implements TimedResource {

    private final LocalDate date;
    private final LocalTime timestamp;
    private final String threadId;
    private final String userContext;
    private final String resource;
    private final List<String> payloadElements;
    private final long duration;

    private RequestInfo(Builder builder) {
        this.date = builder.date;
        this.timestamp = builder.timestamp;
        this.threadId = builder.threadId;
        this.userContext = builder.userContext;
        this.resource = builder.resource;
        this.payloadElements = builder.payloadElements;
        this.duration = builder.duration;
    }

    public LocalDate getDate() {
        return this.date;
    }

    @Override
    public LocalTime getTimestamp() {
        return this.timestamp;
    }

    public String getThreadId() {
        return this.threadId;
    }

    public String getUserContext() {
        return this.userContext;
    }

    @Override
    public String getResource() {
        return this.resource;
    }

    public List<String> getPayloadElements() {
        return this.payloadElements;
    }

    @Override
    public long getDuration() {
        return this.duration;
    }

    public static class Builder {

        private LocalDate date;
        private LocalTime timestamp;
        private String threadId;
        private String userContext;
        private String resource;
        private List<String> payloadElements = new ArrayList<>();
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

        public Builder resource(String uri) {
            this.resource = uri;
            return this;
        }

        public Builder payloadElements(List<String> payloadElements) {
            this.payloadElements = payloadElements;
            return this;
        }

        public Builder payloadElement(String payloadElement) {
            this.payloadElements.add(payloadElement);
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
            this.resource = prototype.resource;
            this.payloadElements = prototype.payloadElements;
            this.duration = prototype.duration;
            return this;
        }

        public RequestInfo build() {
            return new RequestInfo(this);
        }
    }
}
