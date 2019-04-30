package ee.larseckart.logstats;

import java.time.Clock;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        var console = new Console(System.out);
        var averageRequestTime = new AverageRequestTime(console);
        var histogram = new Histogram(console);
        var logStats = new LogStatsImpl(console, List.of(averageRequestTime, histogram));
        var clock = Clock.systemDefaultZone();
        new TimedLogStats(logStats, console, clock).run(args);
    }
}
