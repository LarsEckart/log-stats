package ee.larseckart.logstats;

import io.micronaut.context.ApplicationContext;

public class Main {

    public static void main(String[] args) {
        try (ApplicationContext context = ApplicationContext.run()) {
            LogStats myBean = context.getBean(LogStats.class);
            myBean.execute(args);
        }
    }
}
