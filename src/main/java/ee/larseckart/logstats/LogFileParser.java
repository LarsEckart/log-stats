package ee.larseckart.logstats;

import ee.larseckart.logstats.model.RequestInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class LogFileParser {

    public List<RequestInfo> parse(String fileContent) {
        List<RequestInfo> result = new ArrayList<>();

        final long count = Pattern.compile("\n").splitAsStream(fileContent).count();

        for (int i = 0; i < count; i++) {
            final RequestInfo.Builder builder = new RequestInfo.Builder();
            result.add(builder.build());
        }
        return result;
    }
}
