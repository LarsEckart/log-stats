package ee.larseckart.logstats;

public class Main {

    public static void main(String[] args) {
        var console = new Console(System.out);
        new LogStats(console).run(args);
    }
}
