package ee.larseckart.logstats.input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LogFileReader {

    public String read(String logFileName) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(logFileName)));
            return content;
        } catch (IOException e) {
            // TODO: figure out how to deal with exceptions
            throw new RuntimeException(e);
        }
    }
}
