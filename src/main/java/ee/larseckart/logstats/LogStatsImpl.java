package ee.larseckart.logstats;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class LogStatsImpl implements LogStats {

    private final Console console;

    public LogStatsImpl(Console console) {
        this.console = console;
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

        console.printProcessing(arguments.topN(), arguments.file());

        AverageRequestTime averageRequestTime = new AverageRequestTime(console);
        try (var bufferedReader = Files.newBufferedReader(arguments.file().toPath(), StandardCharsets.UTF_8)) {
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
