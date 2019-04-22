package ee.larseckart.logstats;

public class Resource {

    private int invocations;
    private int requestTotalSum;

    public Resource(int requestTotalSum) {
        this.requestTotalSum = requestTotalSum;
        this.invocations = 1;
    }

    public Resource(int requestTotalSum, int invocations) {
        this.requestTotalSum = requestTotalSum;
        this.invocations = invocations;
    }

    public Resource add(Resource resource) {
        return new Resource(this.requestTotalSum + resource.requestTotalSum,
                invocations + resource.invocations);
    }

    public double avg() {
        return this.requestTotalSum / invocations;
    }
}
