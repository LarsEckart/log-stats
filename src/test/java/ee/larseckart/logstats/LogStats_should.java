package ee.larseckart.logstats;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LogStats_should {

    private ByteArrayOutputStream out;
    private LogStats logStats;

    @BeforeEach
    void setUp() throws Exception {
        out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out, true, StandardCharsets.UTF_8.name());
        Console console = new Console(printStream);
        logStats = new LogStatsImpl(console, new AverageRequestTime(console));
    }

    @Test
    void print_message_about_wrong_usage_when_no_args_provided() {
        // given
        String[] noArguments = new String[0];

        // when
        logStats.run(noArguments);

        // then
        assertThat(out.toString()).isEqualTo("No arguments provided. Run program with -h flag for help.\n");
    }

    @Test
    void print_help_message_when_help_flag_args_provided() {
        // given
        String[] helpFlag = {"-h"};

        // when
        logStats.run(helpFlag);

        // then
        assertThat(out.toString()).isEqualTo(
                "Usage: 2 arguments required, filename to parse and number of entries to display.\nFor example: ~/Documents/timing.log 3\n");
    }

    @Test
    void print_error_message_when_2nd_argument_not_a_number() {
        // given
        String[] secondArgumentNotANumber = {"any_file_path", "not_a_number"};

        // when
        logStats.run(secondArgumentNotANumber);

        // then
        assertThat(out.toString()).isEqualTo("Illegal argument, second argument must be a number.\n");
    }

    @Test
    void print_error_message_when_1st_argument_not_a_existing_file() {
        // given
        String[] firstArgumentNotAFile = {"any_file_path", "3"};

        // when
        logStats.run(firstArgumentNotAFile);

        // then
        assertAll(
                () -> assertThat(out.toString()).startsWith("Illegal argument, no file at "),
                () -> assertThat(out.toString()).endsWith("any_file_path.\n")
        );
    }

    @Nested
    class AverageRequestTimes {

        @BeforeEach
        void setUp() throws UnsupportedEncodingException {
            out = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(out, true, StandardCharsets.UTF_8.name());
            Console console = new Console(printStream);
            logStats = new LogStatsImpl(console, new AverageRequestTime(console));
        }

        @Test
        void prints_resource_and_request_time_when_1_resource_entry_in_file(@TempDir Path tempDir) throws Exception {
            // given
            Path tempFile = tempDir.resolve("any_log_file.log");
            List<String> lines = List.of("2015-08-19 00:00:01,049 (http--0.0.0.0-28080-405) [] /checkSession.do in 100");
            Files.write(tempFile, lines);
            String[] argumentWithFileAndNumberArgument = {tempFile.toString(), "1"};

            // when
            logStats.run(argumentWithFileAndNumberArgument);

            // then
            assertThat(out.toString()).endsWith("\n/checkSession.do 100.0");
        }

        @Test
        void prints_resource_and_request_time_when_1_uri_entry_with_action_in_file(@TempDir Path tempDir) throws Exception {
            // given
            var tempFile = tempDir.resolve("any_log_file.log");
            var lines =
                    List.of("2015-08-19 00:06:34,448 (http--0.0.0.0-28080-370) [CUST:CUS99P9988] /mainContent.do?action=CAROUSEL&contentId=main_carousel in 100");
            Files.write(tempFile, lines);
            String[] argumentWithFileAndNumberArgument = {tempFile.toString(), "1"};

            // when
            logStats.run(argumentWithFileAndNumberArgument);

            // then
            assertThat(out.toString()).endsWith("\n/mainContent.do?action=CAROUSEL 100.0");
        }

        @Test
        void prints_resource_and_request_time_when_1_uri_entry_without_action_in_file(@TempDir Path tempDir) throws Exception {
            // given
            var tempFile = tempDir.resolve("any_log_file.log");
            var lines =
                    List.of("2015-12-23 13:17:49,056 (http--0.0.0.0-28080-4) [CUST:1011156780] /downloadPdf.do?messageId=3718793&file=ts_confirmation_1449809063332.pdf in 100");
            Files.write(tempFile, lines);
            String[] argumentWithFileAndNumberArgument = {tempFile.toString(), "1"};

            // when
            logStats.run(argumentWithFileAndNumberArgument);

            // then
            assertThat(out.toString()).endsWith("\n/downloadPdf.do 100.0");
        }

        @Test
        void prints_resource_and_request_time_when_1_uri_entry_with_only_action_query_param_in_file(@TempDir Path tempDir)
                throws Exception {
            // given
            var tempFile = tempDir.resolve("any_log_file.log");
            var lines =
                    List.of("2015-12-21 19:21:13,499 (http--0.0.0.0-28080-28) [ASP CUST:1013233009] /customerCare.do?action=CHANGE_SNI_HEADER in 100");
            Files.write(tempFile, lines);
            String[] argumentWithFileAndNumberArgument = {tempFile.toString(), "1"};

            // when
            logStats.run(argumentWithFileAndNumberArgument);

            // then
            assertThat(out.toString()).endsWith("\n/customerCare.do?action=CHANGE_SNI_HEADER 100.0");
        }

        @Test
        void prints_resource_and_request_time_when_1_uri_entry_with_action_query_param_not_at_beginning_in_file(@TempDir Path tempDir)
                throws Exception {
            // given
            var tempFile = tempDir.resolve("any_log_file.log");
            var lines =
                    List.of("2016-01-08 14:14:45,820 (http--0.0.0.0-28080-330) [ASP CUST:1000825475] /customerCare.do?bpn=1018704151&ccCode=null&page=mainContent.do?action=SAFE_AVENUE_DETAILS in 100");
            Files.write(tempFile, lines);
            String[] argumentWithFileAndNumberArgument = {tempFile.toString(), "1"};

            // when
            logStats.run(argumentWithFileAndNumberArgument);

            // then
            assertThat(out.toString()).endsWith("\n/customerCare.do?action=SAFE_AVENUE_DETAILS 100.0");
        }

        @Test
        void prints_resource_and_avg_request_time_when_2_entries_in_file(@TempDir Path tempDir) throws Exception {
            // given
            var tempFile = tempDir.resolve("any_log_file.log");
            var lines = List.of("2015-08-19 00:00:01,049 (http--0.0.0.0-28080-405) [] /checkSession.do in 100",
                    "2015-08-19 00:00:01,049 (http--0.0.0.0-28080-405) [] /checkSession.do in 200");
            Files.write(tempFile, lines);
            String[] argumentWithFileAndNumberArgument = {tempFile.toString(), "1"};

            // when
            logStats.run(argumentWithFileAndNumberArgument);

            // then
            assertThat(out.toString()).endsWith("\n/checkSession.do 150.0");
        }

        @Test
        void prints_resource_and_avg_request_time_ordered_by_avg_request_time(@TempDir Path tempDir) throws Exception {
            // given
            var tempFile = tempDir.resolve("any_log_file.log");
            var lines = List.of(
                    "2015-08-19 00:00:01,049 (http--0.0.0.0-28080-405) [] /checkSession.do in 100",
                    "2015-08-19 00:00:01,049 (http--0.0.0.0-28080-405) [] getBroadbandSubscriptions in 500",
                    "2015-08-19 00:00:01,049 (http--0.0.0.0-28080-405) [] /checkSession.do in 100",
                    "2015-08-19 00:00:01,049 (http--0.0.0.0-28080-405) [] getBroadbandSubscriptions in 500",
                    "2015-08-19 00:00:01,049 (http--0.0.0.0-28080-405) [] /checkSession.do in 100",
                    "2015-08-19 00:00:01,049 (http--0.0.0.0-28080-405) [] getBroadbandSubscriptions in 500");
            Files.write(tempFile, lines);
            String[] argumentWithFileAndNumberArgument = {tempFile.toString(), "2"};

            // when
            logStats.run(argumentWithFileAndNumberArgument);

            // then
            assertThat(out.toString()).endsWith("\ngetBroadbandSubscriptions 500.0\n/checkSession.do 100.0");
        }

        @Test
        void prints_resource_and_avg_request_time_ordered_by_avg_request_time_and_adheres_to_provided_limit(@TempDir Path tempDir)
                throws Exception {
            // given
            var tempFile = tempDir.resolve("any_log_file.log");
            var lines = List.of(
                    "2015-08-19 00:00:01,049 (http--0.0.0.0-28080-405) [] /checkSession.do in 100",
                    "2015-08-19 00:00:01,049 (http--0.0.0.0-28080-405) [] getBroadbandSubscriptions in 500",
                    "2015-08-19 00:00:01,049 (http--0.0.0.0-28080-405) [] /checkSession.do in 100",
                    "2015-08-19 00:00:01,049 (http--0.0.0.0-28080-405) [] getBroadbandSubscriptions in 500",
                    "2015-08-19 00:00:01,049 (http--0.0.0.0-28080-405) [] /checkSession.do in 100",
                    "2015-08-19 00:00:01,049 (http--0.0.0.0-28080-405) [] getBroadbandSubscriptions in 500");
            Files.write(tempFile, lines);
            String[] argumentWithFileAndNumberArgument = {tempFile.toString(), "1"};

            // when
            logStats.run(argumentWithFileAndNumberArgument);

            // then
            assertThat(out.toString()).endsWith("\ngetBroadbandSubscriptions 500.0");
        }

        @Test
        void ignores_input_lines_that_do_not_have_valid_request_time(@TempDir Path tempDir) throws Exception {
            // given
            var tempFile = tempDir.resolve("any_log_file.log");
            var lines = List.of(
                    "2015-08-19 00:00:01,049 (http--0.0.0.0-28080-405) [] /checkSession.do in 100",
                    "2016-01-19 20:27:08,928 (http--0.0.0.0-28080-16) [USER:358405537695] /mainContent.do?action=CAROUSEL in {}",
                    "2015-08-19 00:00:01,049 (http--0.0.0.0-28080-405) [] /checkSession.do in 100",
                    "2015-08-19 00:00:01,049 (http--0.0.0.0-28080-405) [] /checkSession.do in 100");
            Files.write(tempFile, lines);
            String[] argumentWithFileAndNumberArgument = {tempFile.toString(), "1"};

            // when
            logStats.run(argumentWithFileAndNumberArgument);

            // then
            assertThat(out.toString()).endsWith("\n/checkSession.do 100.0");
        }

        @Test
        void print_info_about_bad_entries(@TempDir Path tempDir) throws Exception {
            // given
            var tempFile = tempDir.resolve("any_log_file.log");
            var lines = List.of(
                    "2016-01-19 20:27:08,928 (http--0.0.0.0-28080-16) [USER:358405537695] /mainContent.do?action=CAROUSEL in {}");
            Files.write(tempFile, lines);
            String[] argumentWithFileAndNumberArgument = {tempFile.toString(), "1"};

            // when
            logStats.run(argumentWithFileAndNumberArgument);

            // then
            assertThat(out.toString()).contains(
                    "Invalid line: 2016-01-19 20:27:08,928 (http--0.0.0.0-28080-16) [USER:358405537695] /mainContent.do?action=CAROUSEL in {}\n");
        }
    }

    @Nested
    class Histogram_should {

        @BeforeEach
        void setUp() throws UnsupportedEncodingException {
            out = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(out, true, StandardCharsets.UTF_8.name());
            Console console = new Console(printStream);
            logStats = new LogStatsImpl(console, new Histogram(console));
        }

        @Test
        void print_histogram_for_one_entry(@TempDir Path tempDir) throws Exception {
            // given
            var tempFile = tempDir.resolve("any_log_file.log");
            var lines = List.of("2015-08-19 18:00:01,049 (http--0.0.0.0-28080-405) [] /checkSession.do in 100");
            Files.write(tempFile, lines);
            String[] argumentWithFileAndNumberArgument = {tempFile.toString(), "1"};

            // when
            logStats.run(argumentWithFileAndNumberArgument);

            // then
            assertThat(out.toString()).isEqualTo("00: \n"
                    + "01: \n"
                    + "02: \n"
                    + "03: \n"
                    + "04: \n"
                    + "05: \n"
                    + "06: \n"
                    + "07: \n"
                    + "08: \n"
                    + "09: \n"
                    + "10: \n"
                    + "11: \n"
                    + "12: \n"
                    + "13: \n"
                    + "14: \n"
                    + "15: \n"
                    + "16: \n"
                    + "17: \n"
                    + "18: #\n"
                    + "19: \n"
                    + "20: \n"
                    + "21: \n"
                    + "22: \n"
                    + "23: \n");
        }

        @Test
        void print_histogram_for_two_entries_in_same_hour(@TempDir Path tempDir) throws Exception {
            // given
            var tempFile = tempDir.resolve("any_log_file.log");
            var lines = List.of("2015-08-19 18:00:01,049 (http--0.0.0.0-28080-405) [] /checkSession.do in 100",
                    "2015-08-19 18:59:01,049 (http--0.0.0.0-28080-405) [] /checkSession.do in 100");
            Files.write(tempFile, lines);
            String[] argumentWithFileAndNumberArgument = {tempFile.toString(), "1"};

            // when
            logStats.run(argumentWithFileAndNumberArgument);

            // then
            assertThat(out.toString()).isEqualTo("00: \n"
                    + "01: \n"
                    + "02: \n"
                    + "03: \n"
                    + "04: \n"
                    + "05: \n"
                    + "06: \n"
                    + "07: \n"
                    + "08: \n"
                    + "09: \n"
                    + "10: \n"
                    + "11: \n"
                    + "12: \n"
                    + "13: \n"
                    + "14: \n"
                    + "15: \n"
                    + "16: \n"
                    + "17: \n"
                    + "18: ##\n"
                    + "19: \n"
                    + "20: \n"
                    + "21: \n"
                    + "22: \n"
                    + "23: \n");
        }
    }

    @Nested
    class Multiple_File_processors_should {

        private FileContentProcessor first;
        private FileContentProcessor second;
        private Console console;

        @BeforeEach
        void setUp() {
            console = mock(Console.class);
            first = mock(FileContentProcessor.class);
            second = mock(FileContentProcessor.class);
            List<FileContentProcessor> processors = List.of(first, second);
            logStats = new LogStatsImpl(console, processors);
        }

        @Test
        void all_be_called(@TempDir Path tempDir) throws Exception {
            // given
            var tempFile = tempDir.resolve("any_log_file.log");
            var lines = List.of("any line");
            Files.write(tempFile, lines);
            String[] argumentWithFileAndNumberArgument = {tempFile.toString(), "1"};

            // when
            logStats.run(argumentWithFileAndNumberArgument);

            // then
            verify(first).process("any line");
            verify(second).process("any line");
        }

        @Test
        void printed_new_line_after_each_content_processors_result(@TempDir Path tempDir) throws Exception {
            // given
            var tempFile = tempDir.resolve("any_log_file.log");
            var lines = List.of("any line");
            Files.write(tempFile, lines);
            String[] argumentWithFileAndNumberArgument = {tempFile.toString(), "1"};

            // when
            logStats.run(argumentWithFileAndNumberArgument);

            // then
            verify(console, times(2)).print(eq("\n"));
        }
    }
}
