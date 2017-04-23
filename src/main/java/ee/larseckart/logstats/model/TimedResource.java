package ee.larseckart.logstats.model;

import java.time.LocalTime;

public interface TimedResource {

    public String getResource();

    public long getDuration();

    public LocalTime getTimestamp();
}
