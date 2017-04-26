package ee.larseckart.logstats.input;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LogFileReader {

    public String read(String logFileName) {
        try {
            return new String(Files.readAllBytes(Paths.get(logFileName)), StandardCharsets.UTF_8);
        } catch (FileNotFoundException exception) {
            throw new IllegalArgumentException("File " + logFileName + " not found.", exception);
        } catch (IOException exception) {
            throw new IllegalArgumentException("Could not read file.", exception);
        }
    }
}
