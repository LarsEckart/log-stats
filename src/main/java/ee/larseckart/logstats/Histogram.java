package ee.larseckart.logstats;

public class Histogram implements FileContentProcessor {

    private final Console console;

    public Histogram(Console console) {
        this.console = console;
    }

    @Override
    public void process(String line) {

    }

    @Override
    public void print(int limit) {
        this.console.print("00: #\n"
                + "01: \n"
                + "02: \n"
                + "03: \n"
                + "04: \n"
                + "05: \n"
                + "06: \n"
                + "07: \n"
                + "08: \n"
                + "09: \n"
                + "10: \n"
                + "11: \n"
                + "12: \n"
                + "13: \n"
                + "14: \n"
                + "15: \n"
                + "16: \n"
                + "17: \n"
                + "18: \n"
                + "19: \n"
                + "20: \n"
                + "21: \n"
                + "22: \n"
                + "23: \n"
                + "24: \n");
    }
}
