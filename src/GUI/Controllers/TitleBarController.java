
package GUI.Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Goldenfay
 */
public class TitleBarController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }   
    
    
    
    @FXML
    void closeWindow(ActionEvent event) {
        
        Node node=(Node) event.getSource();
        Stage stage=(Stage) node.getScene().getWindow();
        
        stage.close();

    }
    
}
