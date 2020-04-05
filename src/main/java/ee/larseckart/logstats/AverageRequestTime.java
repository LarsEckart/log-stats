package ee.larseckart.logstats;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

public class AverageRequestTime implements FileContentProcessor {

    private Console console;
    private Map<String, Resource> map;

    public AverageRequestTime(Console console) {
        this.console = console;
        this.map = new LinkedHashMap<>();
    }

    @Override
    public void process(String line) {
        int index = line.indexOf("]");
        int startIndex = index + 2;
        String substring = line.substring(startIndex);
        String[] splitted = substring.split(" ");
        String resource = extractResource(splitted[0]);
        try {
            int requestTime;
            requestTime = Integer.parseInt(splitted[splitted.length - 1]);
            map.merge(resource, new Resource(requestTime), (prev, cur) -> prev.add(new Resource(requestTime)));
        } catch (NumberFormatException e) {
            console.printBadLine(line);
        }
    }

    /**
     * wonder if that's a logging issue, query string can be bla?hello=world&tubli=poiss?action=x
     * so ? appears twice. that's why we split resource with limit 2 and the query string by ? and &.
     */
    private String extractResource(String resource) {
        if (resource.contains("?")) {
            String[] UriAndQueryParams = resource.split("\\?", 2);
            String[] queryParams = UriAndQueryParams[1].split("\\?|&");
            for (String queryParam : queryParams) {
                if (queryParam.startsWith("action")) {
                    return UriAndQueryParams[0] + "?" + queryParam;
                }
            }
            return UriAndQueryParams[0];
        }
        return resource;
    }

    @Override
    public void print(int limit) {
        map.entrySet()
            .stream()
            .sorted(Collections.reverseOrder(Comparator.comparingDouble(e -> e.getValue().avg())))
            .limit(limit)
            .forEach((entry) -> console.print("\n" + entry.getKey() + " " + entry.getValue().avg()));
    }
}
