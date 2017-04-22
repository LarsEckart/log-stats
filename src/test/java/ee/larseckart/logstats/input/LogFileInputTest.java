package ee.larseckart.logstats.input;

import ee.larseckart.logstats.AnyTimedResource;
import ee.larseckart.logstats.model.TimedResource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class LogFileInputTest {

    private static final String ANY_FILE_NAME = "any_file_name";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private LogFileReader logFileReader;

    private final Function<String, TimedResource> logFileLineParser =
            text -> new AnyTimedResource("any", 42L);

    private LogFileInput logFileInput;

    @Before
    public void initialize() throws Exception {
        this.logFileInput = new LogFileInput(this.logFileReader, this.logFileLineParser);
    }

    @Test
    public void should_pass_file_name_to_log_file_reader() throws Exception {
        // given
        given(this.logFileReader.read(ANY_FILE_NAME)).willReturn("any_file_content");

        // when
        this.logFileInput.apply(ANY_FILE_NAME);

        // then
        verify(this.logFileReader).read(ANY_FILE_NAME);
    }

    @Test
    public void should_return_a_timed_resource_for_each_line() throws Exception {
        // given
        given(this.logFileReader.read(ANY_FILE_NAME)).willReturn("line1\nline2\nline3");

        // when
        final List<TimedResource> timedResources = this.logFileInput.apply(ANY_FILE_NAME);

        // then
        assertThat(timedResources).hasSize(3);
    }
}