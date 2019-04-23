package ee.larseckart.logstats;

import java.time.Clock;

public class Main {

    public static void main(String[] args) {
        var console = new Console(System.out);
        new TimedLogStats(new LogStatsImpl(console), console, Clock.systemDefaultZone()).run(args);
    }
}
