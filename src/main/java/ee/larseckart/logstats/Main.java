package ee.larseckart.logstats;

import java.time.Clock;

public class Main {

    public static void main(String[] args) {
        var console = new Console(System.out);
        var logStats = new LogStatsImpl(console);
        var clock = Clock.systemDefaultZone();
        new TimedLogStats(logStats, console, clock).run(args);
    }
}
