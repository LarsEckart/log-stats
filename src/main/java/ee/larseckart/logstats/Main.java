package ee.larseckart.logstats;

public class Main {

    public static void main(String[] args) {
        new LogStats(new Console(), new LogFileReader(), new LogFileParser(new LogFileLineParser())).start(args);
    }
}
