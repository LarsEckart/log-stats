package ee.larseckart.logstats.ui;

import ee.larseckart.logstats.input.LogFileInput;
import ee.larseckart.logstats.input.LogFileLineParser;
import ee.larseckart.logstats.model.TimedResource;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.List;
import java.util.function.Function;

public class HourlyCountHistogram extends Application {

    private static final int[][] CHART_DATA = new int[24][2];

    static {
        for (int hour = 0; hour < 24; hour++) {
            CHART_DATA[hour][0] = hour;
            CHART_DATA[hour][1] = 0;
        }
    }

    public static void main(String[] args) {
        final List<TimedResource> timedResources = readDataFromLogFile(args[0]);

        aggregate(timedResources);

        launch(args); // calls start(args)
    }

    private static List<TimedResource> readDataFromLogFile(String arg) {
        final Function<String, TimedResource> logFileLineParser = new LogFileLineParser();
        final Function<String, List<TimedResource>> provider = new LogFileInput(logFileLineParser);
        return provider.apply(arg);
    }

    private static void aggregate(List<TimedResource> timedResources) {
        for (TimedResource timedResource : timedResources) {
            final int hour = timedResource.getTimestamp().getHour();
            CHART_DATA[hour][1] = CHART_DATA[hour][1] + 1;
        }
    }

    @Override
    public void start(Stage stage) {
        final BarChart<String, Number> barChart = setupBarChart();

        XYChart.Series<String, Number> hourlyCountSeries = setupChartData();

        barChart.getData().addAll(hourlyCountSeries);

        Scene scene = new Scene(barChart, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Log Stats");
        stage.show();
    }

    private BarChart<String, Number> setupBarChart() {
        final CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Hour");
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Count");
        final BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Hourly Request Count");
        return barChart;
    }

    private XYChart.Series<String, Number> setupChartData() {
        XYChart.Series<String, Number> hourlyCountSeries = new XYChart.Series<>();
        hourlyCountSeries.setName("Requests");
        for (int i = 0; i < 24; i++) {
            final String hour = String.valueOf(CHART_DATA[i][0]);
            final int count = CHART_DATA[i][1];
            final XYChart.Data<String, Number> item = new XYChart.Data<>(hour, count);
            hourlyCountSeries.getData().add(item);
        }
        return hourlyCountSeries;
    }
}
