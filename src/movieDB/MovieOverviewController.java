package movieDB;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import movieDB.model.Movie;
import java.util.ArrayList;
import java.util.Collections;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 * Kontroler pro spodni cast hlavniho okna zobrazujici nahled filmu.
 */
public class MovieOverviewController {

    @FXML
    private TableView<Movie> movieTable;
    @FXML
    private TreeView treeView;
    @FXML
    private Label nameLabel;
    @FXML
    private TableColumn<Movie, String> firstNameColumn;
    @FXML
    private TableColumn<Movie, String> lastNameColumn;
    @FXML
    private Label originalNameLabel;
    @FXML
    private Label ratingLabel;
    @FXML
    private Label directorLabel;
    @FXML
    private TextArea actorsLabel;
    @FXML
    private Label yearLabel;
    @FXML
    private Label genreLabel;
    @FXML
    private TextArea aboutLabel;
    @FXML
    private Label countryLabel;
    @FXML
    private TextField searchField;

    private MainApp mainApp;

    /**
     * Konstruktor
     */
    public MovieOverviewController() {
    }

    /**
     * Metoda automaticky volana po nacteni fxml.
     */
    @FXML
    private void initialize() {

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Movie, String>("name"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Movie, String>("rating"));
        movieTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        showMovieDetails(null);

        //Nastaveni listeneru pro movieTable
        movieTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Movie>() {
            @Override
            public void changed(ObservableValue<? extends Movie> observable,
                    Movie oldValue, Movie newValue) {
                showMovieDetails(newValue);
            }
        });

        //Nastaveni listeneru pro treeView
        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue,
                    Object newValue) {
                TreeItem<String> selectedItem = (TreeItem<String>) newValue;
                treeviewAction(selectedItem);
            }
        });
    }

    /**
     * Metoda volana mainApp pro poskytnuti refence na sebe.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        movieTable.setItems(FXCollections.observableArrayList(Movie.getMovieData()));
        setTreeview();
    }

    /**
     * Metoda pro nacteni informaci o vybranem filmu do danych labelu
     *
     * @param movie vybrany film nebo null
     */
    private void showMovieDetails(Movie movie) {
        if (movie != null) {
            nameLabel.setText(movie.getName());
            originalNameLabel.setText(movie.getOriginalName());
            ratingLabel.setText(Integer.toString(movie.getRating()));
            directorLabel.setText(movie.getDirector());
            actorsLabel.setText(movie.getActors());
            yearLabel.setText(Integer.toString(movie.getYear()));
            genreLabel.setText(movie.getZanr());
            aboutLabel.setText(movie.getAbout());
            countryLabel.setText(movie.getCountry());

        } else {
            nameLabel.setText("Název filmu");
            originalNameLabel.setText("");
            ratingLabel.setText("");
            directorLabel.setText("");
            actorsLabel.setText("");
            yearLabel.setText("");
            genreLabel.setText("");
            countryLabel.setText("");
            aboutLabel.setText("");
        }
    }

    /**
     * Metoda volana pri stisku tlacitka vyhledat
     */
    @FXML
    private void handleSearch() {
        movieTable.setItems(Movie.search(searchField.getText()));
    }

    /**
     * Metoda volana pri stisku tlacitka smazat.
     */
    @FXML
    private void handleDeleteMovie() {
        int selectedIndex = movieTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Alert alert = new Alert(AlertType.CONFIRMATION, "Opravdu chcete tento film smazat ?", ButtonType.YES, ButtonType.NO);
            alert.setHeaderText("Mazání filmu");
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                Movie.delMovie(movieTable.getItems().get(selectedIndex));
                refresh();
                setTreeview();
                Movie.save();
            }
        } else {
            //Nic nevybrano
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Nejdříve vyberte film, který chcete smazat!");
            alert.setTitle("Chybný výběr");
            alert.setHeaderText("Nic není vybráno!");
            alert.showAndWait();
        }
    }

    /**
     * Metoda volana pri stisknuti tlacitka novy.
     */
    @FXML
    private void handleNewMovie() {
        Movie tempPerson = new Movie();
        boolean okClicked = mainApp.showPersonEditDialog(tempPerson);
        if (okClicked) {
            Movie.addMovie(tempPerson);
            refresh();
            Movie.save();
            setTreeview();
        }
    }

    /**
     * Metoda volana pri stisknuti tlacitka edit
     */
    @FXML
    private void handleEditMovie() {
        Movie selectedPerson = movieTable.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            boolean okClicked = mainApp.showPersonEditDialog(selectedPerson);
            if (okClicked) {
                refresh();
                setTreeview();
                Movie.save();
                showMovieDetails(selectedPerson);
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Nejdříve vyberte film, který chcete upravit!");
            alert.setTitle("Chybný výběr");
            alert.setHeaderText("Nic není vybráno!");
            alert.showAndWait();
        }
    }

    /**
     * Metoda pro vytvoreni TreeView
     */
    private void setTreeview() {
        TreeItem<String> rootItem = new TreeItem<String>("Všechny filmy");
        rootItem.setExpanded(true);

        TreeItem<String> link1 = new TreeItem<String>("Dle žánru");
        link1.setExpanded(true);

        TreeItem<String> link2 = new TreeItem<String>("Dle země původu");
        link2.setExpanded(true);

        TreeItem<String> link3 = new TreeItem<String>("Dle hodnocení");
        link3.setExpanded(true);

        ArrayList<String> list = new ArrayList<String>();
        ArrayList<String> list2 = new ArrayList<String>();
        ArrayList<Integer> listInt = new ArrayList<Integer>();

        for (Movie movie : Movie.getMovieData()) {
            if (!(list.contains(movie.getZanr()))) {
                list.add(movie.getZanr());
            }

            if (!(list2.contains(movie.getCountry()))) {
                list2.add(movie.getCountry());
            }

            if (!(listInt.contains(movie.getRating()))) {
                listInt.add(movie.getRating());
            }
        }

        Collections.sort(list);
        Collections.sort(list2);
        Collections.sort(listInt);

        for (String string : list) {
            link1.getChildren().add(new TreeItem<String>(string));
        }

        for (String string : list2) {
            link2.getChildren().add(new TreeItem<String>(string));
        }

        for (Integer digit : listInt) {
            link3.getChildren().add(new TreeItem<String>(Integer.toString(digit)));
        }

        treeView.setRoot(rootItem);
        rootItem.getChildren().add(link1);
        rootItem.getChildren().add(link2);
        rootItem.getChildren().add(link3);
    }

    /**
     * Metoda volana pri vyberu polozky v TreeView
     *
     * @param value
     */
    private void treeviewAction(TreeItem value) {
        if (!(value.getValue().equals("Dle žánru") || value.getValue().equals("Dle země původu") || value.getValue().equals("Dle hodnocení"))) {
            movieTable.setItems(Movie.treeAction(value));
        }
    }

    /**
     * Metoda pro aktuatualizaci dat v tabulce a treeview, pote co dojde ke
     * zmene dat v modelu.
     */
    private void refresh() {
        int selectedIndexTree = treeView.getSelectionModel().getSelectedIndex();
        int selectedIndexTable = movieTable.getSelectionModel().getSelectedIndex();
        movieTable.setItems(null);
        movieTable.layout();
        movieTable.setItems(FXCollections.observableArrayList(Movie.getMovieData()));
        movieTable.getSelectionModel().select(selectedIndexTable);
        treeView.getSelectionModel().select(selectedIndexTree);
    }
}
