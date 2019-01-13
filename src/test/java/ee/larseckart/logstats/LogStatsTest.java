package ee.larseckart.logstats;

import java.time.Clock;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import ee.larseckart.logstats.model.TimedResource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LogStatsTest {

    private static final String ANY_ARGS = "anyArgs";
    private static final String ANY_FILE_NAME = "any_file_name";
    private static final String NOT_A_NUMBER = "NaN";
    private static final String ANY_POSITIVE_NUMBER = "3";
    private static final String ANY_NEGATIVE_NUMBER = "-42";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private Console console;

    @Mock
    private Function<String, List<TimedResource>> provider;

    @Mock
    private BiConsumer<Integer, List<TimedResource>> consumer;

    private Clock clock = Clock.systemDefaultZone();

    private LogStats logStats;

    @Before
    public void initialize() throws Exception {
        this.logStats = new LogStats(this.clock, this.console, this.provider, this.consumer);
    }

    @Test
    public void should_print_how_to_get_help_when_no_args_provided() throws Exception {
        // given
        final String[] emptyArgs = new String[0];

        // when
        this.logStats.execute(emptyArgs);

        // then
        verify(this.console).printLine("No args provided, run with -h flag for help.");
    }

    @Test
    public void should_print_how_to_use_the_application_when_args_with_help_flag() throws Exception {
        // given
        final String[] args = new String[1];
        args[0] = "-h";

        // when
        this.logStats.execute(args);

        // then
        verify(this.console).printLine("Provide 2 arguments.");
        verify(this.console).printLine("First argument must be String s where s is the name of the log file.");
        verify(this.console).printLine(
                "Second argument must be a number n where n denotes how many resources to print out.");
    }

    @Test
    public void should_print_how_to_use_the_application_when_one_argument_but_not_help_flag() throws Exception {
        // given
        final String[] args = new String[1];
        args[0] = ANY_ARGS;

        // when
        this.logStats.execute(args);

        // then
        verify(this.console).printLine("Unknown argument(s), run with -h flag for help.");
    }

    @Test
    public void should_print_how_to_use_the_application_when_more_than_2_args() throws Exception {
        // given
        final String[] args = new String[3];
        args[0] = ANY_ARGS;
        args[1] = ANY_ARGS;
        args[2] = ANY_ARGS;

        // when
        this.logStats.execute(args);

        // then
        verify(this.console).printLine("Unknown argument(s), run with -h flag for help.");
    }

    @Test
    public void should_print_how_to_use_the_application_when_2_args_but_second_arg_is_no_number() throws Exception {
        // given
        final String[] args = new String[2];
        args[0] = ANY_ARGS;
        args[1] = NOT_A_NUMBER;

        // when
        this.logStats.execute(args);

        // then
        verify(this.console).printLine("Unknown argument(s), run with -h flag for help.");
    }

    @Test
    public void should_print_how_to_use_the_application_when_2_args_but_second_arg_is_negative_number() throws Exception {
        // given
        final String[] args = new String[2];
        args[0] = ANY_ARGS;
        args[1] = ANY_NEGATIVE_NUMBER;

        // when
        this.logStats.execute(args);

        // then
        verify(this.console).printLine("TopN argument must be positive.");
    }

    @Test
    public void should_print_how_to_use_the_application_when_2_args_but_second_arg_is_0() throws Exception {
        // given
        final String[] args = new String[2];
        args[0] = ANY_ARGS;
        args[1] = "0";

        // when
        this.logStats.execute(args);

        // then
        verify(this.console).printLine("TopN argument must be positive.");
    }

    @Test
    public void should_pass_first_argument_to_timed_resource_provider_when_correct_arguments() throws Exception {
        // given
        final String[] args = new String[2];
        args[0] = ANY_FILE_NAME;
        args[1] = ANY_POSITIVE_NUMBER;

        // when
        this.logStats.execute(args);

        // then
        verify(this.provider).apply(ANY_FILE_NAME);
    }

    @Test
    public void should_pass_request_infos_to_top_n_duration_consumer() throws Exception {
        // given
        final String[] args = new String[2];
        args[0] = ANY_FILE_NAME;
        args[1] = ANY_POSITIVE_NUMBER;

        final List<TimedResource> timedResources =
                Collections.singletonList(new AnyTimedResource("any", 42L));

        given(this.provider.apply(ANY_FILE_NAME)).willReturn(timedResources);

        // when
        this.logStats.execute(args);

        // then
        verify(this.consumer).accept(Integer.parseInt(ANY_POSITIVE_NUMBER), timedResources);
    }

    @Test
    public void should_print_file_not_found_if_illegal_argument_exception() throws Exception {
        // given
        final String[] args = new String[2];
        args[0] = ANY_FILE_NAME;
        args[1] = ANY_POSITIVE_NUMBER;

        given(this.provider.apply(ANY_FILE_NAME)).willThrow(new IllegalArgumentException("any_message"));

        // when
        this.logStats.execute(args);

        // then
        verify(this.console).printLine("any_message");
    }

    @Test
    public void should_print_execution_time() throws Exception {
        // given
        final String[] anyArgs = new String[2];
        anyArgs[0] = ANY_FILE_NAME;
        anyArgs[1] = ANY_POSITIVE_NUMBER;

        this.clock = mock(Clock.class);
        given(this.clock.instant()).willReturn(Instant.ofEpochMilli(50L), Instant.ofEpochMilli(75L));

        this.logStats = new LogStats(this.clock, this.console, this.provider, this.consumer);

        // when
        this.logStats.execute(anyArgs);

        // then
        verify(this.console).printLine("Execution took 25 milliseconds.");
    }
}
