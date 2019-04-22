package ee.larseckart.logstats;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class LogStats_should {

    private ByteArrayOutputStream out;
    private LogStats logStats;

    @BeforeEach
    void setUp() throws Exception {
        out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out, true, StandardCharsets.UTF_8.name());
        logStats = new LogStats(new Console(printStream));
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

    @Test
    void prints_message_about_processing_data_when_first_argument_is_valid_file_path_and_second_argument_is_number() throws Exception {
        // given
        File tempFile = File.createTempFile("any", ".log");
        String[] argumentWithFileAndNumberArgument = {tempFile.getAbsolutePath(), "3"};

        // when
        logStats.run(argumentWithFileAndNumberArgument);

        // then
        assertAll(
                () -> assertThat(out.toString()).startsWith("Processing any"),
                () -> assertThat(out.toString()).endsWith(".log for top 3 requests\n")
        );
    }

    @Test
    void prints_resource() throws Exception {
        // given
        File tempFile = File.createTempFile("any", ".log");
        var lines = List.of("2015-08-19 00:00:01,049 (http--0.0.0.0-28080-405) [] /checkSession.do in 100");
        Files.write(tempFile.toPath(), lines);
        String[] argumentWithFileAndNumberArgument = {tempFile.getAbsolutePath(), "3"};

        // when
        logStats.run(argumentWithFileAndNumberArgument);

        // then
        assertThat(out.toString()).endsWith("\n/checkSession.do 100");
    }
}
