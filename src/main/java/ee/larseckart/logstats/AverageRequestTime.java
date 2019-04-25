package ee.larseckart.logstats;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

public class AverageRequestTime {

    private Console console;
    private Map<String, Resource> map;

    public AverageRequestTime(Console console) {
        this.console = console;
        this.map = new LinkedHashMap<>();
    }

    public void process(String line) {
        int index = line.indexOf("]");
        int startIndex = index + 2;
        String substring = line.substring(startIndex);
        String[] splitted = substring.split(" ");
        String resource = splitted[0];
        try {
            int requestTime;
            requestTime = Integer.parseInt(splitted[splitted.length - 1]);
            map.merge(resource, new Resource(requestTime), (prev, cur) -> prev.add(new Resource(requestTime)));
        } catch (NumberFormatException e) {
            console.printBadLine(line);
        }
    }

    public void print(int limit) {
        map.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Comparator.comparingDouble(e -> e.getValue().avg())))
                .limit(limit)
                .forEach((entry) -> console.print("\n" + entry.getKey() + " " + entry.getValue().avg()));
    }
}
