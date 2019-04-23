package ee.larseckart.logstats;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class TimedLogStats_should {

    private ByteArrayOutputStream out;
    private LogStats logStats;
    private Clock clock;

    @BeforeEach
    void setUp() throws Exception {
        out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out, true, StandardCharsets.UTF_8.name());
        clock = mock(Clock.class);
        logStats = new TimedLogStats(mock(LogStats.class), new Console(printStream), clock);
    }

    @Test
    void print_program_execution_time_at_the_end() {
        // given
        given(clock.instant()).willReturn(Instant.ofEpochMilli(0), Instant.ofEpochMilli(42));

        // when
        logStats.run(new String[0]);

        // then
        assertThat(out.toString()).endsWith("Execution time: 42ms");
    }
}
