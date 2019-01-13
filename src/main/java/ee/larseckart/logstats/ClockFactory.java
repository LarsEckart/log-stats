package ee.larseckart.logstats;

import java.time.Clock;
import javax.inject.Singleton;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;

@Factory
public class ClockFactory {

    @Bean
    @Singleton
    Clock clock() {
        return Clock.systemDefaultZone();
    }
}
