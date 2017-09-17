package ee.larseckart.logstats.di;

import dagger.Component;
import ee.larseckart.logstats.LogStats;

@Component(modules = ProviderModule.class)
public interface MainInjector {

    LogStats logStats();
}
