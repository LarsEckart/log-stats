package ee.larseckart.logstats;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

public class LogStats {

    private final Console console;

    public LogStats(Console console) {
        this.console = console;
    }

    public void run(String[] args) {
        Arguments arguments = new Arguments(args);
        if (arguments.isEmpty()) {
            console.printNoArgs();
            return;
        }

        if (arguments.isHelpFlag()) {
            console.printHelp();
            return;
        }

        if (!arguments.isTopNArgumentProvided()) {
            console.printNaN();
            return;
        }

        if (!arguments.isFileArgumentProvided()) {
            console.printFileError(arguments.file());
            return;
        }

        console.printProcessing(arguments.topN(), arguments.file());

        Map<String, Resource> map = new LinkedHashMap<>();
        try (var bufferedReader = Files.newBufferedReader(arguments.file().toPath(), StandardCharsets.UTF_8)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                int index = line.indexOf("]");
                String[] splitted = getRelevantPart(line, index);
                String resource = splitted[0];
                int requestTime = Integer.parseInt(splitted[splitted.length - 1]);
                map.merge(resource, new Resource(requestTime), (prev, cur) -> prev.add(new Resource(requestTime)));
            }
        } catch (IOException e) {
        }
        // TODO: limit
        map.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Comparator.comparingDouble(e -> e.getValue().avg())))
                .limit(arguments.topN())
                .forEach((entry) -> console.print("\n" + entry.getKey() + " " + entry.getValue().avg()));
    }

    private String[] getRelevantPart(String line, int index) {
        int startIndex = index + 2;
        String substring = line.substring(startIndex);
        return substring.split(" ");
    }
}
