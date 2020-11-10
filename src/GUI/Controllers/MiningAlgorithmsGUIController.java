package GUI.Controllers;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Goldenfay
 */
public class MiningAlgorithmsGUIController implements Initializable {

    @FXML
    private Button aprioriButton;

    @FXML
    private Button eclatButton;
    
    public static Stage stage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        setupEffects();

    }

    @FXML
    void openAprioriGUI(ActionEvent event) throws IOException {
        
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/GUI/Views/AprioriGUI.fxml"));
        Parent fxml=(Parent) loader.load();
        
        
        Scene scene=new Scene(fxml);
        
        Stage stg=new Stage();
        stg.initOwner(stage);
        stg.setScene(scene);
        stg.initStyle(StageStyle.UNDECORATED);
           
        stg.show();

    }

    @FXML
    void openEclatGUI(ActionEvent event) throws IOException {

        
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/GUI/Views/EclatGUI.fxml"));
        Parent fxml=(Parent) loader.load();
        
        
        Scene scene=new Scene(fxml);
        
        Stage stg=new Stage();
        stg.initOwner(stage);
        stg.setScene(scene);
        stg.initStyle(StageStyle.UNDECORATED);
           
        stg.show();
    }

    @FXML
    void startBackgroundAnimation(MouseEvent event) {

    }

    @FXML
    void stopBackgroundAnimation(MouseEvent event) {

    }
    
    @FXML
    void openSettingsWindow(ActionEvent event) {

    }

    private void setupEffects() {
        DropShadow rollOverColor = new DropShadow();
        rollOverColor.setColor(Color.ORANGERED);
        rollOverColor.setSpread(0.3);
        rollOverColor.setRadius(20);
        DropShadow clickColor = new DropShadow();
        clickColor.setColor(Color.DARKBLUE);

        aprioriButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
                (event) -> aprioriButton.setEffect(rollOverColor));

        aprioriButton.addEventHandler(MouseEvent.MOUSE_EXITED, (event) -> aprioriButton.setEffect(null));

        aprioriButton.addEventHandler(MouseEvent.MOUSE_PRESSED,
                (event) -> aprioriButton.setEffect(clickColor));

        aprioriButton.addEventHandler(MouseEvent.MOUSE_RELEASED,
                (event) -> aprioriButton.setEffect(rollOverColor));

        //
        eclatButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
                (event) -> eclatButton.setEffect(rollOverColor));

        eclatButton.addEventHandler(MouseEvent.MOUSE_EXITED, (event) -> eclatButton.setEffect(null));

        eclatButton.addEventHandler(MouseEvent.MOUSE_PRESSED,
                (event) -> eclatButton.setEffect(clickColor));

        eclatButton.addEventHandler(MouseEvent.MOUSE_RELEASED,
                (event) -> eclatButton.setEffect(rollOverColor));
    }
}
