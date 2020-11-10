
package GUI.Controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import model.mining.algorithms.DBScan;
import model.preprocessing.bases.DataSet;

/**
 * FXML Controller class
 *
 * @author Goldenfay
 */
public class DBScanGUIController implements Initializable {

    public static Stage stage;
    public static DataSet CURRENT_DATASET = AprioriGUIController.CURRENT_DATASET;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        DBScan DbScanAlgo=new DBScan(CURRENT_DATASET, 123, 10);
        ArrayList<Integer> resultVector = DbScanAlgo.scan(3);
        
        HashMap<Integer,HashSet<Integer>> clustersTable=new HashMap<>();
        
        for(int i=0;i<resultVector.size();i++){
            if(!clustersTable.containsKey(resultVector.get(i)))
                clustersTable.put(resultVector.get(i),new HashSet<Integer>());
            
            clustersTable.get(resultVector.get(i)).add(i);
            
        }
        
    }   
    
    
    
    
    @FXML
    void openSettingsWindow(ActionEvent event) {

    }
    
}
