package ee.larseckart.logstats.input;

import ee.larseckart.logstats.model.RequestInfo;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

public class LogFileLineParserTest {

    private LogFileLineParser lineParser;

    @Before
    public void initialize() throws Exception {
        this.lineParser = new LogFileLineParser();
    }

    @Test
    public void should_parse_line_with_user_context_and_multiple_payload_elements() throws Exception {
        // given
        String example =
                "2015-08-19 00:00:22,428 (http--0.0.0.0-28080-259) [USER:300407044035] getSubcriptionCampaigns 300407044035 true in 2669";

        // when
        final RequestInfo requestInfo = this.lineParser.apply(example);

        // then
        SoftAssertions.assertSoftly(softly -> {
            assertThat(requestInfo.getDate()).isEqualTo(LocalDate.of(2015, 8, 19));
            assertThat(requestInfo.getTimestamp()).isEqualTo(LocalTime.of(0, 0, 22, 428000000));
            assertThat(requestInfo.getThreadId()).isEqualTo("http--0.0.0.0-28080-259");
            assertThat(requestInfo.getUserContext()).isEqualTo("USER:300407044035");
            assertThat(requestInfo.getResource()).isEqualTo("getSubcriptionCampaigns");
            assertThat(requestInfo.getPayloadElements()).hasSize(2);
            assertThat(requestInfo.getPayloadElements().get(0)).isEqualTo("300407044035");
            assertThat(requestInfo.getPayloadElements().get(1)).isEqualTo("true");
            assertThat(requestInfo.getDuration()).isEqualTo(2669);
        });
    }

    @Test
    public void should_parse_line_with_empty_user_context_and_no_payload_elements() throws Exception {
        // given
        String example = "2015-08-19 00:00:01,963 (http--0.0.0.0-28080-245) [] /checkSession.do in 113";

        // when
        final RequestInfo requestInfo = this.lineParser.apply(example);

        // then
        SoftAssertions.assertSoftly(softly -> {
            assertThat(requestInfo.getUserContext()).isEqualTo("");
            assertThat(requestInfo.getPayloadElements()).hasSize(0);
        });
    }

    @Test
    public void should_parse_line_with_uri_plus_query_string() throws Exception {
        // given
        String example =
                "2015-08-19 00:00:02,814 (http--0.0.0.0-28080-245) [CUST:CUS5T27233] /substypechange.do?msisdn=300501633574 in 17";

        // when
        final RequestInfo requestInfo = this.lineParser.apply(example);

        // then
        assertThat(requestInfo.getResource()).isEqualTo("/substypechange.do");
    }

    @Test
    public void should_parse_line_with_uri_plus_query_string_which_has_action_query_param() throws Exception {
        // given
        String example =
                "2015-08-19 00:00:03,260 (http--0.0.0.0-28080-245) [CUST:CUS5T27233] /mainContent.do?action=TOOLS&contentId=main_tools in 5";

        // when
        final RequestInfo requestInfo = this.lineParser.apply(example);

        // then
        assertThat(requestInfo.getResource()).isEqualTo("/mainContent.do?action=TOOLS");
    }

    @Test
    public void should_parse_line_with_uri_plus_query_string_which_has_only_action_query_param() throws Exception {
        // given
        String example =
                "2015-08-19 23:06:48,372 (http--0.0.0.0-28080-297) [CUST:CUS12B1435] /customerInfo.do?action=CUSTOMERINFO in 9999";

        // when
        final RequestInfo requestInfo = this.lineParser.apply(example);

        // then
        assertThat(requestInfo.getResource()).isEqualTo("/customerInfo.do?action=CUSTOMERINFO");
    }
}