package movieDB;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import movieDB.model.Movie;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

/**
 * Kontroler pro okno pro editaci a zakladani filmu.
 *
 */
public class MovieEditDialogController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField originalNameField;
    @FXML
    private TextField ratingField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField directorField;
    @FXML
    private TextField countryField;
    @FXML
    private TextField genreField;
    @FXML
    private TextArea actorsField;
    @FXML
    private TextArea aboutField;

    private Stage dialogStage;
    private Movie movie;
    private boolean okClicked = false;

    /**
     * Metoda automaticky volana po nacteni fxml.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Nastaveni stage.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Nastavi hodnoty daneho filmu pro editaci do policek okna.
     *
     * @param movie
     */
    public void setMovie(Movie movie) {
        this.movie = movie;
        
        actorsField.getStyleClass().clear();
        actorsField.getStyleClass().add("text-field");       
        aboutField.getStyleClass().clear();
        aboutField.getStyleClass().add("text-field");

        nameField.setText(movie.getName());
        originalNameField.setText(movie.getOriginalName());
        ratingField.setText(Integer.toString(movie.getRating()));
        directorField.setText(movie.getDirector());
        actorsField.setText(movie.getActors());
        yearField.setText(Integer.toString(movie.getYear()));
        genreField.setText(movie.getZanr());
        aboutField.setText(movie.getAbout());
        countryField.setText(movie.getCountry());
    }

    /**
     * Vraci true pokud uzivatel stisknul OK. Pri stisknuti cancel vraci false. 
     *
     * @return true nebo false
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Metoda volana pri stisku tlacitka ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            movie.setName(nameField.getText());
            movie.setOriginalName(originalNameField.getText());
            movie.setRating(Integer.parseInt(ratingField.getText()));
            movie.setDirector(directorField.getText());
            movie.setActors(actorsField.getText());
            movie.setYear(Integer.parseInt(yearField.getText()));
            movie.setZanr(genreField.getText().toLowerCase());
            movie.setAbout(aboutField.getText());
            movie.setCountry(countryField.getText().toLowerCase());

            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Metoda volana pri stisku tlacitka Cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Metoda pro validaci vstupu od uzivatele
     *
     * @return Pokud je vstup validni vraci true
     */
    private boolean isInputValid() {
        String errorMessage = "";
        
        if (nameField.getText() == null || regexValidNameText(nameField.getText())) {
            errorMessage += "Špatně zadané jméno filmu!\n";
        }
        if (originalNameField.getText() == null || regexValidNameText(nameField.getText())) {
            errorMessage += "Špatně zadaný originální název!\n";
        }
        if (ratingField.getText() == null || regexValidRating(ratingField.getText())) {
            errorMessage += "Špatně zadané hodnocení. Musí být číslo 0-10!\n";
        }  
        if (yearField.getText() == null || regexValidYear(yearField.getText())) {
            errorMessage += "Špatně zadaný rok!\n";
        } 
        if (directorField.getText() == null || regexValidNameText(directorField.getText())) {
            errorMessage += "Špatně zadané jméno režiséra!\n";
        } 
        if (actorsField.getText() == null || regexValidNameText(actorsField.getText())) {
            errorMessage += "Špatně zadaná jména herců!\n";
        }        
        if (genreField.getText() == null || regexValidNameText(genreField.getText())) {
            errorMessage += "Špatně zadaný žánr!\n";
        }
        if (aboutField.getText() == null || regexValidNameText(aboutField.getText())) {
            errorMessage += "Špatně zadané info o filmu!\n";
        }
        if (countryField.getText() == null || regexValidNameText(countryField.getText())) {
            errorMessage += "Špatně zadaná země původu!\n";
        }
        
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText(errorMessage);
            alert.setTitle("Opravte prosím chybná pole");
            alert.setHeaderText("Špatně vyplněná pole!");
            alert.showAndWait();
            return false;
        }
    }

    /**
     * Metoda pro validaci nazvu za pomoci regularniho vyrazu
     * @param text retezec pro validaci
     * @return Pokud je vstup validni vraci true
     */
    private boolean regexValidNameText(String text) {
        Pattern p = Pattern.compile("[a-zA-Z0-9 ]+");
        Matcher m = p.matcher(text);
        if (m.find()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Metoda pro validaci hodnoceni za pomoci regularniho vyrazu
     * @param text retezec pro validaci
     * @return Pokud je vstup validni vraci true
     */
    private boolean regexValidRating(String text) {
        Pattern p = Pattern.compile("(10|\\d)");
        Matcher m = p.matcher(text);
        if (m.find()) {
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * Metoda pro validaci roku za pomoci regularniho vyrazu
     * @param text retezec pro validaci
     * @return Pokud je vstup validni vraci true
     */
    private boolean regexValidYear(String text) {
        Pattern p = Pattern.compile("^[12][0-9]{3}$");
        Matcher m = p.matcher(text);
        if (m.find()) {
            return false;
        } else {
            return true;
        }
    }
    
}
