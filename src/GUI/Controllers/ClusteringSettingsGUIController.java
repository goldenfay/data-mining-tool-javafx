
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Goldenfay
 */
public class ClusteringSettingsGUIController implements Initializable {

    
    
     @FXML
    private Slider clusterNumberSlider;

    @FXML
    private Label clusterNumberSliderVal;

    @FXML
    private Slider maxNeighborsSlider;

    @FXML
    private Label maxNeighborsSliderVal;

    @FXML
    private CheckBox descritized_checkbox;

    @FXML
    private Button confirmBtn;

    @FXML
    private Button cancelBtn;

        /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
         maxNeighborsSlider.setValue(ClusteringGUIController.CLARANS_MAXNEIGH);
        maxNeighborsSlider.setBlockIncrement(1);
        maxNeighborsSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                maxNeighborsSliderVal.setText((int)maxNeighborsSlider.getValue()+" ");
            }
        });
        
        
         clusterNumberSlider.setValue(ClusteringGUIController.CLARANSK);
        clusterNumberSlider.setBlockIncrement(1);
        clusterNumberSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                clusterNumberSliderVal.setText((int)clusterNumberSlider.getValue()+" ");
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

        
        if (descritized_checkbox.isSelected())
            ClusteringGUIController.DESCRITIZED_FLAG=true;
        else 
            ClusteringGUIController.DESCRITIZED_FLAG=false;
        
        ClusteringGUIController.MEDOIDK=(int) clusterNumberSlider.getValue();
        ClusteringGUIController.CLARANSK=(int) clusterNumberSlider.getValue();
        
        ClusteringGUIController.CLARANS_MAXNEIGH=(int) maxNeighborsSlider.getValue();
        ClusteringGUIController.CLARANSK=(int) clusterNumberSlider.getValue();
        
        
        ClusteringGUIController.CURRENT_INSTANCE.getKmedoi_clusternbr_label().setText(ClusteringGUIController.MEDOIDK+"");
        ClusteringGUIController.CURRENT_INSTANCE.getClarans_clusternbr_label().setText(ClusteringGUIController.CLARANSK+"");
        ClusteringGUIController.CURRENT_INSTANCE.getClarans_cmaxneighborslabel().setText(maxNeighborsSlider.getValue()+"");
        //ClusteringGUIController.CURRENT_INSTANCE.getClarans_clusternbr_label().setText(ClusteringGUIController.CLARANSK+"");
        
        
            
        
        Node node=(Node) event.getSource();
        Stage stage=(Stage) node.getScene().getWindow();
        
        stage.close();
    }

    
}
