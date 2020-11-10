
package GUI.Controllers;


import java.io.File;
import java.net.URL;
import java.util.HashMap;
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
public class AttributsExplorationController implements Initializable {

    @FXML
    private WebView webView;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        
        loadWebViewContent();
    }    
    
    
    private void loadWebViewContent() {
        
        
        JavaConnector Jconnect = new JavaConnector();
        Jconnect.setDataSet(PrevisualizeWindowController.CURRENT_DATASET);
        File f = new File(new File("").getAbsolutePath()+"\\assets\\histograms2.html");
        HashMap<String, Object> allResults = new HashMap<String, Object>();
        
        Jconnect.setResultsBundle(allResults);
        JavaJsInterface interfaceConnect = new JavaJsInterface(Jconnect, webView, f.toURI().toString());
        interfaceConnect.establishConnexion("");
        

    }
}
