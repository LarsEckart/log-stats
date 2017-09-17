package ee.larseckart.logstats;

import ee.larseckart.logstats.di.DaggerMainInjector;
import ee.larseckart.logstats.di.MainInjector;

public class Main {

    public static void main(String[] args) {
        MainInjector injector = DaggerMainInjector.create();
        injector.logStats().execute(args);
    }
}
