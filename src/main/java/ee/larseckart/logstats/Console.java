package ee.larseckart.logstats;

import javax.inject.Singleton;

@Singleton
public class Console {

    public Console() {
    }

    public void printLine(String text) {
        System.out.println(text);
    }
}
