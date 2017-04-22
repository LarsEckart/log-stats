package ee.larseckart.logstats;

import ee.larseckart.logstats.model.TimedResource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

import static org.mockito.Mockito.verify;

public class AverageDurationCalculatorTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private Console console;

    private BiConsumer<Integer, List<TimedResource>> averageDurationCalculator;

    @Before
    public void initialize() throws Exception {
        this.averageDurationCalculator = new AverageDurationCalculator(console);
    }

    @Test
    public void should_print_n_resources_who_have_highest_avg_duration() throws Exception {
        // given
        final List<TimedResource> timedResources = Arrays.asList(
                new AnyTimedResource("A", 2L), new AnyTimedResource("B", 4L),
                new AnyTimedResource("A", 8L), new AnyTimedResource("B", 16L),
                new AnyTimedResource("C", 3L), new AnyTimedResource("C", 9L),
                new AnyTimedResource("D", 1L), new AnyTimedResource("D", 1L));
        // A: 5 ; B: 10 ; C: 6 ; D: 1

        // when
        this.averageDurationCalculator.accept(2, timedResources);

        // then
        verify(this.console).printLine("B: 10.0");
        verify(this.console).printLine("C: 6.0");
    }
}