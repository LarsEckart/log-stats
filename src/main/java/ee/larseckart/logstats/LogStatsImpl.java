package ee.larseckart.logstats;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class LogStatsImpl implements LogStats {

    private final Console console;
    private final FileContentProcessor fileContentProcessor;

    public LogStatsImpl(Console console, FileContentProcessor fileContentProcessor) {
        this.console = console;
        this.fileContentProcessor = fileContentProcessor;
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
                fileContentProcessor.process(line);
            }
        } catch (IOException exception) {
            console.printStackTrace(exception);
        }
        fileContentProcessor.print(arguments.topN());
        System.out.println();
    }
}
