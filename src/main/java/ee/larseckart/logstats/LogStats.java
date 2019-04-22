package ee.larseckart.logstats;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

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

        try (var bufferedReader = Files.newBufferedReader(arguments.file().toPath(), StandardCharsets.UTF_8)) {
            String line = bufferedReader.readLine();
            if (line != null) {
                int index = line.indexOf("]");
                int startIndex = index + 2;
                String substring = line.substring(startIndex);
                String[] splitted = substring.split(" ");
                String resource = splitted[0];
                String requestTime = splitted[splitted.length - 1];
                console.print("\n" + resource + " " + requestTime);
            }
        } catch (IOException e) {

        }
    }
}
