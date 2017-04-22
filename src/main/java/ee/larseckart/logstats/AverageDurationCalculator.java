package ee.larseckart.logstats;

import ee.larseckart.logstats.model.TimedResource;

import java.util.List;
import java.util.function.BiConsumer;

public class AverageDurationCalculator implements BiConsumer<Integer, List<TimedResource>> {

    @Override
    public void accept(Integer topN, List<TimedResource> requestInfos) {

    }
}
