package ee.larseckart.logstats;

public class Resource {

    private int invocations;
    private int requestTotalSum;

    public Resource(int requestTotalSum) {
        this.requestTotalSum = requestTotalSum;
        this.invocations = 1;
    }

    public Resource add(Resource resource) {
        this.invocations += resource.invocations;
        this.requestTotalSum += resource.requestTotalSum;
        return this;
    }

    public double avg() {
        return this.requestTotalSum / invocations;
    }
}
