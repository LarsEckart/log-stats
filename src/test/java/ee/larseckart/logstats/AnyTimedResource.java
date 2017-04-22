package ee.larseckart.logstats;

import ee.larseckart.logstats.model.TimedResource;

public class AnyTimedResource implements TimedResource {

    private final String resource;
    private final long duration;

    public AnyTimedResource(String resource, long duration) {
        this.resource = resource;
        this.duration = duration;
    }

    @Override
    public String getResource() {
        return this.resource;
    }

    @Override
    public long getDuration() {
        return this.duration;
    }
}
