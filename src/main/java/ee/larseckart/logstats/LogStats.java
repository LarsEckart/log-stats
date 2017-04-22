package ee.larseckart.logstats;

public class LogStats {

    private Console console;

    public LogStats(Console console) {
        this.console = console;
    }

    public void start(String[] args) {
        if (args.length == 0) {
            this.console.printLine("No args provided, run with -h flag for help.");
        } else {
            if (args.length == 1) {

            }
        }
    }
}
