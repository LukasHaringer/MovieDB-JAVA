package movieDB;

import movieDB.model.Movie;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialogs;

/**
 * Kontroler pro horni cast hlavniho okna (Ovladaci bar).
 */
public class RootLayoutController {

	private MainApp mainApp;

	/**
         * Metoda volana mainApp pro poskytnuti refence na sebe.
         * @param mainApp 
         */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	
	/**
         * Metoda volada pri stisku tlacitka filmy dle hodniceni
         */
	@FXML
	private void handleShowRatingStatistics() {
		mainApp.showRatingStatistics();
	}
        
        /**
         * Metoda volada pri stisku tlacitka pocet filmu dle zanru
         */
        @FXML
	private void handleShowCountStatistics() {
		mainApp.showCountStatistics();
	}
	
	/**
         * Metoda volana pri stisknuti tlacitka o programu.
         */
	@FXML
	private void handleAbout() {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Semestrální práce pro předmět KIV/UUR 2015/2016 \nAutor: Lukáš Haringer A13B0306P");
            alert.setTitle("O programu");
            alert.setHeaderText("Filmová databáze 1.2");
            alert.showAndWait();
	}
	
        /**
         * Metoda volana pri stisku tlacitka nastaveni
         */
        @FXML
	private void handleConfig() {
            mainApp.showConfig();
	}
        
	/**
         * Metoda volana pri stisknuti tlacitka exit.
         */
	@FXML
	private void handleExit() {
            Movie.save();
		System.exit(0);
	}
	
}
