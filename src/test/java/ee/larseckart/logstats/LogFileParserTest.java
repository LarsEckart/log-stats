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
        this.parser = new LogFileParser();
    }

    @Test
    public void should_parse_file_lines_to_request_info() throws Exception {
        // given
        String example =
                "2015-08-19 00:00:22,428 (http--0.0.0.0-28080-259) [USER:300407044035] getSubcriptionCampaigns 300407044035 true in 2669";

        // when
        final List<RequestInfo> requestInfos = this.parser.parse(example);

        // then
        assertThat(requestInfos).hasSize(1);
    }
}