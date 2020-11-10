package GUI.Controllers;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;
import model.exchangeInterface.JavaConnector;
import model.exchangeInterface.JavaJsInterface;

/**
 * FXML Controller class
 *
 * @author Goldenfay
 */
public class ShowAnalysisController implements Initializable {

    @FXML
    private WebView webview;
    
    
    
    
    public static ArrayList<Integer> attributsToSummerize;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        loadWebViewContent();
    }

    private void loadWebViewContent() {
        
        
        JavaConnector Jconnect = new JavaConnector();
        File f = new File(new File("").getAbsolutePath()+"\\assets\\boxplots.html");
        HashMap<String, Object> allResults = new HashMap<String, Object>();
        for (int j = 0; j < attributsToSummerize.size(); j++) {

            HashMap<String, Object> result = PrevisualizeWindowController.PRE_PROCESSOR.simpleDataAnalysis(PrevisualizeWindowController.CURRENT_DATASET, attributsToSummerize.get(j));
            /*try {
                //Jconnect.toJSonObject(result);

            } catch (JSONException ex) {
                Logger.getLogger(PrevisualizeWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }*/
            allResults.put("Attribute " + (PrevisualizeWindowController.CURRENT_DATASET.getAttributsName().get( attributsToSummerize.get(j)) ), result);

        }
        Jconnect.setResultsBundle(allResults);
        JavaJsInterface interfaceConnect = new JavaJsInterface(Jconnect, webview, f.toURI().toString());
        interfaceConnect.establishConnexion("showBoxplot");
        
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
                //Getters & Setters

    
    

}
