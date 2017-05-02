package ee.larseckart.logstats.input;

import ee.larseckart.logstats.AnyTimedResource;
import ee.larseckart.logstats.model.TimedResource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class LogFileInputTest {

    private static final String ANY_FILE_NAME = "any_file_name";

    private final Function<String, TimedResource> logFileLineParser =
            text -> new AnyTimedResource("any", 42L);

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private LogFileInput logFileInput;
    private Path filePath;

    @Before
    public void initialize() throws Exception {
        this.initializeTempFile();
        this.logFileInput = new LogFileInput(this.logFileLineParser);
    }

    @Test
    public void should_return_a_timed_resource_for_each_line_when_unix_line_end() throws Exception {
        // given

        // when
        final List<TimedResource> timedResources = this.logFileInput.apply(this.filePath.toString());

        // then
        assertThat(timedResources).hasSize(2);
    }

    private void initializeTempFile() throws IOException {
        this.temporaryFolder.newFile(ANY_FILE_NAME);
        List<String> lines = Arrays.asList("line1", "line");
        this.filePath = Paths.get(this.temporaryFolder.getRoot() + "/" + ANY_FILE_NAME);
        Files.write(this.filePath, lines, StandardCharsets.UTF_8);
    }
}