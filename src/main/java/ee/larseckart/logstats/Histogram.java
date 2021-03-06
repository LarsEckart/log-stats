package ee.larseckart.logstats;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Histogram implements FileContentProcessor {

    private final Console console;
    private final Map<String, MutableInteger> hours;

    public Histogram(Console console) {
        this.console = console;
        this.hours = IntStream.range(0, 24)
            .mapToObj(i -> String.format("%02d", i))
            .collect(Collectors.toMap(
                Function.identity(),
                v -> new MutableInteger(0),
                (u, v) -> {
                    throw new IllegalStateException(String.format("Duplicate key %s", u));
                },
                LinkedHashMap::new));
    }

    @Override
    public void process(String line) {
        String hour = line.substring(11, 13);
        updateOccurrences(hour);
    }

    private void updateOccurrences(String hour) {
        MutableInteger value = hours.get(hour);
        if (value != null) {
            value.set(value.get() + 1);
        } else {
            hours.put(hour, new MutableInteger(1));
        }
    }

    @Override
    public void print(int limit) {
        // untested
        Comparator<MutableInteger> comparator = Comparator.comparing(MutableInteger::get);
        int maxOccurrences = Collections.max(hours.values(), comparator).get();
        int maxBarLength = 80;
        int scaleFactor = maxOccurrences / maxBarLength;
        int s = Math.max(scaleFactor, 1);
        // untested

        this.hours.forEach((k, v) -> {
            int scaledDownOccurrences = v.val / s;
            console.print(k + ": " + getSymbol(scaledDownOccurrences) + "\n");
        });
    }

    private String getSymbol(int i) {
        return "#".repeat(Math.max(0, i));
    }

    static class MutableInteger {

        private int val;

        MutableInteger(int val) {
            this.val = val;
        }

        int get() {
            return this.val;
        }

        void set(int val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return Integer.toString(val);
        }
    }
}
