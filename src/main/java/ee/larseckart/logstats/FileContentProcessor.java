package ee.larseckart.logstats;

public interface FileContentProcessor {

  void process(String line);

  void print(int limit);
}
