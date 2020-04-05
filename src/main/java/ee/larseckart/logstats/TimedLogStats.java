package ee.larseckart.logstats;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

public class TimedLogStats extends LogStatsDecorator {

  private final Console console;
  private final Clock clock;

  public TimedLogStats(LogStats logStats, Console console, Clock clock) {
    super(logStats);
    this.console = console;
    this.clock = clock;
  }

  @Override
  public void run(String[] args) {
    Instant start = Instant.now(clock);
    super.run(args);
    var duration = Duration.between(start, Instant.now(clock));
    console.printProgramExecutionTime(duration);
  }
}
