package ee.larseckart.logstats;

import ee.larseckart.logstats.model.TimedResource;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class AverageDurationCalculator implements BiConsumer<Integer, List<TimedResource>> {

    private final Console console;

    public AverageDurationCalculator(Console console) {
        this.console = console;
    }

    @Override
    public void accept(Integer topN, List<TimedResource> requestInfos) {

        Map<String, DoubleSummaryStatistics> aggregated = aggregateData(requestInfos);

        List<AggregationResult> averageDuration = calculateAverageAndSortAscending(aggregated);

        for (int i = 0; i < topN; i++) {
            this.console.printLine(averageDuration.get(i).toString());
        }
    }

    private Map<String, DoubleSummaryStatistics> aggregateData(List<TimedResource> requestInfos) {
        return requestInfos.stream()
                           .collect(
                                   Collectors.groupingBy(
                                           timedResource -> timedResource.getResource(),
                                           Collectors.summarizingDouble(
                                                   timedResource -> timedResource.getDuration())));
    }

    private List<AggregationResult> calculateAverageAndSortAscending(Map<String, DoubleSummaryStatistics> aggregated) {
        return aggregated.entrySet()
                         .stream()
                         .map(entry -> AggregationResult.of(entry.getKey(), entry.getValue().getAverage()))
                         .sorted((s1, s2) -> s2.getValue().compareTo(s1.getValue()))
                         .collect(Collectors.toList());
    }
}

class AggregationResult {

    private final String name;
    private final Double value;

    private AggregationResult(String name, Double value) {
        this.name = name;
        this.value = value;
    }

    public static AggregationResult of(String name, Double value) {
        return new AggregationResult(name, value);
    }

    public Double getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.name + ": " + this.value;
    }
}

