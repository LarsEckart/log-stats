package ee.larseckart.logstats;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import ee.larseckart.logstats.model.TimedResource;

import javax.inject.Named;

@Named("Calculator")
public class AverageDurationCalculator implements BiConsumer<Integer, List<TimedResource>> {

    private final Console console;

    public AverageDurationCalculator(Console console) {
        this.console = console;
    }

    @Override
    public void accept(Integer topN, List<TimedResource> timedResources) {

        Map<String, DoubleSummaryStatistics> resourceStatistics = aggregateData(timedResources);

        List<AggregationResult> averageDurations = prepareSortedResultList(topN, resourceStatistics);

        averageDurations.forEach(ad -> this.console.printLine(ad.toString()));
    }

    private Map<String, DoubleSummaryStatistics> aggregateData(List<TimedResource> timedResources) {
        return timedResources.stream()
                .collect(Collectors.groupingBy(
                        timedResource -> timedResource.getResource(),
                        Collectors.summarizingDouble(timedResource -> timedResource.getDuration())));
    }

    private List<AggregationResult> prepareSortedResultList(int topN, Map<String, DoubleSummaryStatistics> aggregated) {
        return aggregated.entrySet()
                .stream()
                .map(entry -> new AggregationResult(entry.getKey(), entry.getValue().getAverage()))
                .sorted((s1, s2) -> s2.getValue().compareTo(s1.getValue()))
                .limit(topN)
                .collect(Collectors.toList());
    }

    class AggregationResult {

        private final String name;
        private final Double value;

        private AggregationResult(String name, Double value) {
            this.name = name;
            this.value = value;
        }

        public Double getValue() {
            return this.value;
        }

        @Override
        public String toString() {
            return String.format("%s: %.2f ms", this.name, this.value);
        }
    }
}
