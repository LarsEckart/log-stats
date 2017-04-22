package ee.larseckart.logstats;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class LogFileReaderTest {

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
        String tempFilePath = this.temporaryFolder.getRoot() + "/anyFile";

        // when
        final String fileContent = this.logFileReader.read(tempFilePath);

        // then
        assertThat(fileContent).isEqualTo("line1\nline2\n");
    }

    private void initializeTempFile() throws IOException {
        this.temporaryFolder.newFile("anyFile");
        List<String> lines = Arrays.asList("line1", "line2");
        Files.write(Paths.get(this.temporaryFolder.getRoot() + "/anyFile"), lines);
    }
}