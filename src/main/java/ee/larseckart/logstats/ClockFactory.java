package ee.larseckart.logstats;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;

import java.time.Clock;

import javax.inject.Singleton;

@Factory
public class ClockFactory {

    @Bean
    @Singleton
    Clock clock() {
        return Clock.systemDefaultZone();
    }
}
