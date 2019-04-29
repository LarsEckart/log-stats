package ee.larseckart.logstats;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class LogStatsImpl implements LogStats {

    private final Console console;
    private final AverageRequestTime averageRequestTime;

    public LogStatsImpl(Console console, AverageRequestTime averageRequestTime) {
        this.console = console;
        this.averageRequestTime = averageRequestTime;
    }

    @Override
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

        try (var bufferedReader = Files.newBufferedReader(arguments.file(), StandardCharsets.UTF_8)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                averageRequestTime.process(line);
            }
        } catch (IOException exception) {
            console.printStackTrace(exception);
        }
        averageRequestTime.print(arguments.topN());
        System.out.println();
    }
}
