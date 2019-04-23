package ee.larseckart.logstats;

public abstract class LogStatsDecorator implements LogStats {

    private final LogStats logStats;

    public LogStatsDecorator(LogStats logStats) {
        this.logStats = logStats;
    }

    @Override
    public void run(String[] args) {
        this.logStats.run(args);
    }
}
