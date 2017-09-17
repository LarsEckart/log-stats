package ee.larseckart.logstats;

import javax.inject.Inject;

public class Console {

    @Inject
    public Console() {
    }

    public void printLine(String text) {
        System.out.println(text);
    }
}
