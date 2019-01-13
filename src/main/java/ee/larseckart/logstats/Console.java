package ee.larseckart.logstats;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Console {

    @Inject
    public Console() {
    }

    public void printLine(String text) {
        System.out.println(text);
    }
}
