package model.exchangeInterface;

import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

/**
 * Java-Javascript connection class
 * @author Goldenfay
 */
public class JavaJsInterface {

    static JavaConnector JConnector;
    static JSObject JSconnector;
    private WebView webView;
    private WebEngine engine;
    private String url;
    boolean firstTime=true;

    public JavaJsInterface(JavaConnector Jconnector, WebView webView, String url) {
        this.JConnector = Jconnector;
        this.webView = webView;
        this.engine = this.webView.getEngine();
        this.url = url;
    }

    public void establishConnexion(String methodeCalling) {

        try {
            engine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
                    
                if (Worker.State.SUCCEEDED == newValue) {
                    if(firstTime) firstTime=false;
                    // set an interface object named 'javaConnector' in the web engine's page
                    JSObject window = (JSObject) engine.executeScript("window");
                    window.setMember("javaConnector", JConnector);

                    // get the Javascript connector object. 
                    JSconnector = (JSObject) engine.executeScript("getJsConnector()");
                    engine.executeScript("setVariables()");
                    JSconnector.call(methodeCalling);
                    
                }
                
            });
            engine.load(url);

            
            
        } catch (Exception e) {

        }

    }
    
    public void callMethode(String methodeCalling){
        
        
    }

}
