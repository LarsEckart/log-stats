package ee.larseckart.logstats;

import ee.larseckart.logstats.model.RequestInfo;

import java.util.List;
import java.util.function.BiConsumer;

public class AverageDurationCalculator implements BiConsumer<Integer, List<RequestInfo>> {

    @Override
    public void accept(Integer topN, List<RequestInfo> requestInfos) {

    }
}
