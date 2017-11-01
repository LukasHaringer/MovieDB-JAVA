package movieDB;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import movieDB.model.Movie;
import java.util.HashMap;

/**
 * Kontroler pro zobrazovaní statistik
 */
public class StatisticsController {

    @FXML
    private BarChart<String, Integer> barChart;

    @FXML
    private CategoryAxis xAxis;

    private ObservableList<String> categoriesNames;

    /**
     * Metoda automaticky volana po nacteni fxml
     */
    @FXML
    private void initialize() {

        categoriesNames = Movie.getCategories();
        xAxis.setCategories(categoriesNames);
    }

    /**
     * Metoda pro vykresleni statistik dle poctu filmu danych zanru
     */
    public void drawCountStatisctic() {
        barChart.setTitle("Počet filmů dle žánrů");
        XYChart.Series<String, Integer> series = createDataSeries(Movie.getCountStatistics());
        barChart.getData().add(series);
    }

    /**
     * Metoda pro vykresleni statistik dle hodnoceni filmu danych zanru
     */
    public void drawRatingStatistic() {
        barChart.setTitle("Průměrné hodnocení filmů dle žánrů");
        XYChart.Series<String, Integer> series = createDataSeries(Movie.getRatingStatistics());
        barChart.getData().add(series);
    }

    /**
     * Metoda pro vytvoreni dat pro graf
     * @param data data pro graf
     * @return XYChart.Series
     */
     
     private XYChart.Series<String, Integer> createDataSeries(HashMap<String, Integer> data) {
        XYChart.Series<String, Integer> series = new XYChart.Series<String, Integer>();

        for (String s : categoriesNames) {
            XYChart.Data<String, Integer> monthData = new XYChart.Data<String, Integer>(s, data.get(s));
            series.getData().add(monthData);
        }

        return series;
    }
}
