package movieDB;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import movieDB.model.Movie;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import javafx.scene.control.Alert;

/**
 * Hlavni trida aplikace.
 */
public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private Scene scene;
    private String style = "DarkTheme.css";
    private static final int PORT = 9999;
    private static ServerSocket socket;

    /**
     * Konstruktor
     */
    public MainApp() {
    }

    /**
     * Metoda volajici se pri startu aplikace. Zkontroluje zda jiz nebezi
     * instance programu, nacte filmy ze souboru a vytvori hlavni okno aplikace.
     *
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        checkIfRunning();
        Movie.load();
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Filmová databáze");

        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            Scene scene = new Scene(rootLayout);
            scene.getStylesheets().clear();
            scene.getStylesheets().add(style);
            this.scene = scene;
            primaryStage.setMinWidth(1200);
            primaryStage.setMinHeight(800);
            primaryStage.setScene(scene);

            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        showMovieOverview();
    }

    /**
     * Getter pro stage.
     *
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Vytvori cast hlavniho okna pro nahled zvoleneho filmu.
     */
    public void showMovieOverview() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("view/MovieOverview.fxml"));
            AnchorPane overviewPage = (AnchorPane) loader.load();
            rootLayout.setCenter(overviewPage);

            MovieOverviewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Otevre dialog pro editaci zvoleneho filmu. Pokud uzivatel klikne na OK
     * metoda vrati true. Pokud uzivatel klikne na cancel metoda vrati false.
     *
     * @param movie zvoleny movie k editaci
     * @return true nebo false.
     */
    public boolean showPersonEditDialog(Movie movie) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("view/MovieEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit movie");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setMinWidth(750);
            dialogStage.setMinHeight(443);
            dialogStage.setMaxWidth(750);
            dialogStage.setMaxHeight(443);
            Scene scene = new Scene(page);
            scene.getStylesheets().clear();
            scene.getStylesheets().add(style);
            dialogStage.setScene(scene);

            MovieEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMovie(movie);

            dialogStage.showAndWait();

            return controller.isOkClicked();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Otevre okno se statistikou poctu filmu.
     */
    public void showCountStatistics() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("view/Statistics.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Počet filmů dle žánrů");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setMinWidth(620);
            dialogStage.setMinHeight(450);
            dialogStage.setScene(scene);

            StatisticsController controller = loader.getController();
            controller.drawCountStatisctic();

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Otevre okno se statistikou hodnoceni filmu.
     */
    public void showRatingStatistics() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("view/Statistics.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Průměrné hodnocení filmů dle žánrů");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.setMinWidth(620);
            dialogStage.setMinHeight(450);

            StatisticsController controller = loader.getController();
            controller.drawRatingStatistic();

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Otevre okno pro nastaveni vzhledu.
     */
    public void showConfig() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("view/Config.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Nastavení");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            scene.getStylesheets().clear();
            scene.getStylesheets().add(style);
            dialogStage.setMinWidth(400);
            dialogStage.setMinHeight(300);
            dialogStage.setScene(scene);

            ConfigController controller = loader.getController();
            controller.setDialogStage(dialogStage, this);
            dialogStage.showAndWait();

            return;

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * Metoda, ktera kontroluje zda aplikace jiz jednou nebezi. Metoda se pokusi
     * nabindovat port 9999 a pokud se to nezdari aplikaci ukonci.
     */
    private static void checkIfRunning() {
        try {
            socket = new ServerSocket(PORT, 0, InetAddress.getByAddress(new byte[]{127, 0, 0, 1}));
        } catch (BindException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Jedna instance programu je již spuštěna!");
            alert.setHeaderText("Program již beží!");
            alert.showAndWait();
            System.exit(1);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Neočekávaná chyba!");
            alert.setHeaderText("Neočekávaná chyba!");
            alert.showAndWait();
            e.printStackTrace();
            System.exit(2);
        }
    }

    /**
     * Metoda pro zmenu skinu
     * @param s nazev skinu
     */
    public void changeSkin(String style) {
        this.style=style;
        scene.getStylesheets().clear();
        scene.getStylesheets().add(style);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
