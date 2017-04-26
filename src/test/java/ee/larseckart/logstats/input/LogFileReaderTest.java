package ee.larseckart.logstats.input;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class LogFileReaderTest {

    private static final String ANY_FILE_NAME = "any_file_name";

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private LogFileReader logFileReader;

    @Before
    public void initialize() throws Exception {
        initializeTempFile();

        this.logFileReader = new LogFileReader();
    }

    @Test
    public void should_read_file_and_return_content() throws Exception {
        // given
        String filePath = this.temporaryFolder.getRoot() + "/" + ANY_FILE_NAME;

        // when
        String fileContent = this.logFileReader.read(filePath);

        // then
        assertThat(fileContent).isEqualTo("line1" + System.lineSeparator() + "line2" + System.lineSeparator());
    }

    private void initializeTempFile() throws IOException {
        this.temporaryFolder.newFile(ANY_FILE_NAME);
        List<String> lines = Arrays.asList("line1", "line2");
        Path filePath = Paths.get(this.temporaryFolder.getRoot() + "/" + ANY_FILE_NAME);
        Files.write(filePath, lines);
    }
}