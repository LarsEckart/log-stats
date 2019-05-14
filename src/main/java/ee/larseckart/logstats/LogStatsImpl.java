package ee.larseckart.logstats;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Collections;

public class LogStatsImpl implements LogStats {

    private final Console console;
    private final Collection<FileContentProcessor> processors;

    public LogStatsImpl(Console console, FileContentProcessor fileContentProcessor) {
        this(console, Collections.singleton(fileContentProcessor));
    }

    public LogStatsImpl(Console console, Collection<FileContentProcessor> processors) {
        this.console = console;
        this.processors = processors;
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
                for (FileContentProcessor processor : processors) {
                    processor.process(line);
                }
            }
        } catch (IOException exception) {
            console.printStackTrace(exception);
        }
        for (FileContentProcessor processor : processors) {
            processor.print(arguments.topN());
            if (processors.size() > 1.) {
                console.print("\n");
            }
        }
        System.out.println();
    }
}
