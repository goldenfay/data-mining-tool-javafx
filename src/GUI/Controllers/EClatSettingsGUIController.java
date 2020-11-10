
package GUI.Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Goldenfay
 */
public class EClatSettingsGUIController implements Initializable {

    @FXML
    private Slider minSupportSlider;

    @FXML
    private Label minSupportSliderVal;

    @FXML
    private Slider minConfSlider;

    @FXML
    private Label minConfidenceSliderVal;

    @FXML
    private Button confirmBtn;

    @FXML
    private Button cancelBtn;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      minSupportSlider.setValue(EclatGUIController.MINIMUM_SUPPORT);
        minSupportSlider.setBlockIncrement(1);
        minSupportSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                minSupportSliderVal.setText((int)minSupportSlider.getValue()+" %");
            }
        });
        
        minConfSlider.setValue(EclatGUIController.MINIMUM_CONFIDENCE*100);
        minConfSlider.setBlockIncrement(1);
         minConfSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                minConfidenceSliderVal.setText((int)minConfSlider.getValue()+"");
            }
        });
        
    }   
    
    
    

    @FXML
    void dismissWindow(ActionEvent event) {
        
        Node node=(Node) event.getSource();
        Stage stage=(Stage) node.getScene().getWindow();
        
        stage.close();

    }

    @FXML
    void saveSettings(ActionEvent event) {
        
        //EclatGUIController.MINIMUM_CONFIDENCE=minConfSlider.getValue()/100;
        //EclatGUIController.MINIMUM_SUPPORT=(int) (minSupportSlider.getValue());
        
        EclatGUIController.CURRENT_INSTANCE.getMinConfidence_label().setText(minConfSlider.getValue()+"%");
        EclatGUIController.CURRENT_INSTANCE.getMinSupport_label().setText(minSupportSlider.getValue()+"%");
        Node node=(Node) event.getSource();
        Stage stage=(Stage) node.getScene().getWindow();
        
        stage.close();

    }
    
}
