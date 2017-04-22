package ee.larseckart.logstats;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.verify;

public class LogStatsTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private Console console;
    private LogStats logStats;

    @Before
    public void initialize() throws Exception {
        this.logStats = new LogStats(this.console);
    }

    @Test
    public void should_print_how_to_get_help_when_no_args_provided() throws Exception {
        // given
        final String[] emptyArgs = new String[0];

        // when
        this.logStats.start(emptyArgs);

        // then
        verify(this.console).printLine("No args provided, run with -h flag for help.");
    }
    
    @Test
    public void should_print_how_to_use_the_application_when_args_with_help_flag() throws Exception {
        // given
        final String[] args = new String[1];
        args[0] = "-h";

        // when
        this.logStats.start(args);

        // then
        verify(this.console).printLine("Provide 2 arguments.\n"
                + "First argument must be String s where s is the name of the log file.\n"
                + "Second argument must be a number n where n denotes how many resources to print out.");
    }

    @Test
    public void should_print_how_to_use_the_application_when_args_but_not_help_flag() throws Exception {
        // given
        final String[] args = new String[1];
        args[0] = "anyArgs";

        // when
        this.logStats.start(args);

        // then
        verify(this.console).printLine("Unknown argument(s), run with -h flag for help.");
    }

    @Test
    public void should_print_how_to_use_the_application_when_more_than_2_args() throws Exception {
        // given
        final String[] args = new String[3];
        args[0] = "anyArgs";
        args[1] = "anyArgs";
        args[2] = "anyArgs";

        // when
        this.logStats.start(args);

        // then
        verify(this.console).printLine("Unknown argument(s), run with -h flag for help.");
    }

    @Test
    public void should_print_how_to_use_the_application_when_2_args_but_second_arg_is_no_number() throws Exception {
        // given
        final String[] args = new String[2];
        args[0] = "anyArgs";
        args[1] = "NaN";

        // when
        this.logStats.start(args);

        // then
        verify(this.console).printLine("Unknown argument(s), run with -h flag for help.");
    }
}