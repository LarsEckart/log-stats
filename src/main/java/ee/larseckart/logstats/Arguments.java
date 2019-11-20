package ee.larseckart.logstats;

import java.nio.file.Path;
import java.nio.file.Paths;

class Arguments {

    private final String[] parameters;

    Arguments(String[] parameters) {
        this.parameters = parameters;
    }

    boolean isEmpty() {
        return parameters.length == 0;
    }

    boolean isHelpFlag() {
        return "-h".equals(parameters[0]);
    }

    boolean isTopNArgumentProvided() {
        String parameter = parameters[1];
        return parameter.chars().allMatch(Character::isDigit);
    }

    boolean isFileArgumentProvided() {
        var path = Paths.get(parameters[0]);
        var file = path.toFile();

        return file.exists() && file.isFile() && file.canRead();
    }

    int topN() {
        return Integer.parseInt(parameters[1]);
    }

    Path file() {
        var path = Paths.get(parameters[0]);

        return path;
    }
}
