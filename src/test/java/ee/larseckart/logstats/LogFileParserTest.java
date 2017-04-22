package ee.larseckart.logstats;

import ee.larseckart.logstats.model.RequestInfo;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LogFileParserTest {

    private LogFileParser parser;

    @Before
    public void initialize() throws Exception {
        this.parser = new LogFileParser(text -> new RequestInfo.Builder().build());
    }

    @Test
    public void should_parse_file_content_with_just_1_line_and_create_1_request_info_object_from_it() throws Exception {
        // given
        String example = "anyLine";

        // when
        final List<RequestInfo> requestInfos = this.parser.parse(example);

        // then
        assertThat(requestInfos).hasSize(1);
    }

    @Test
    public void should_parse_file_content_with_multiple_lines_and_create_request_info_objects_from_it() throws Exception {
        // given
        String example = "line1\nline2\nline3\n";

        // when
        final List<RequestInfo> requestInfos = this.parser.parse(example);

        // then
        assertThat(requestInfos).hasSize(3);
    }
}