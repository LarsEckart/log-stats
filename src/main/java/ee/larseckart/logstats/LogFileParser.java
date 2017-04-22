package ee.larseckart.logstats;

import ee.larseckart.logstats.model.RequestInfo;

import java.util.ArrayList;
import java.util.List;

public class LogFileParser {

    public List<RequestInfo> parse(String fileContent) {
        List<RequestInfo> result = new ArrayList<>();
        final RequestInfo.Builder builder = new RequestInfo.Builder();
        result.add(builder.build());
        return result;
    }
}
