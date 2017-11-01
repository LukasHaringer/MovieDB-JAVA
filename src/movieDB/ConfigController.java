package movieDB;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.RadioButton;

public class ConfigController {

    private Stage dialogStage;
    private MainApp mainApp;

    @FXML
    private RadioButton radioButton1;
    @FXML
    private RadioButton radioButton2;
    
    @FXML
    private void initialize() {
    }

    /**
     * Nastavi stage.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage,MainApp mainApp) {
        this.dialogStage = dialogStage;
        this.mainApp=mainApp;
    }
    
    /**
     * Volano po stisku ok.
     */
    @FXML
    private void handleOk() {
        
        if(radioButton1.selectedProperty().get()){
            mainApp.changeSkin("DarkTheme.css");
        }
        
        if(radioButton2.selectedProperty().get()){
            mainApp.changeSkin("BlueTheme.css");
        }
        
        dialogStage.close();
    }

    /**
     * Volano po stisku cancel.
     */
    @FXML
    private void handleCancel() {

        dialogStage.close();
    }

}
