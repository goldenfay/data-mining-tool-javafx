package GUI.Controllers;


import animatefx.animation.BounceInDown;
import animatefx.animation.BounceInLeft;
import animatefx.animation.BounceInRight;
import animatefx.animation.BounceInUp;
import animatefx.animation.FadeIn;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.preprocessing.bases.DataSet;
import model.preprocessing.bases.generalDataSet;
import model.preprocessing.preprocessors.BasicPreProcessor;

public class DataSetWindoController implements Initializable {

    @FXML
    private Button uploadFileBtn;

    @FXML
    private TextField filePathInput;

    @FXML
    private Button loadDatasetBtn;
    @FXML
    private Text startLabel;

    @FXML
    private Button visualizeBtn;

    @FXML
    private Button preprocessBtn;

    @FXML
    private Button miningBtn;

    @FXML
    private Button cluterBtn;

    public static Stage stage;
     public static DataSet CURRENT_DATASET = null;
     
     public static generalDataSet GENERALIZED_DATASET=null;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.startLabel.setVisible(false);
        this.loadDatasetBtn.setVisible(false);
        this.uploadFileBtn.setVisible(false);
        this.filePathInput.setVisible(false);

        this.visualizeBtn.setVisible(false);
        this.preprocessBtn.setVisible(false);
        this.miningBtn.setVisible(false);
        this.cluterBtn.setVisible(false);

        FileChooser fileCh = new FileChooser();
        fileCh.getExtensionFilters().add(new FileChooser.ExtensionFilter("ARFF files (*.arff)", "*.arff"));
        fileCh.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));

        uploadFileBtn.setOnAction((event) -> {
            File file = fileCh.showOpenDialog(stage);
            if (file != null) {
                openFile(file);
            }
        });

    }

    public void reEstablish() {

        this.startLabel.setVisible(true);
        this.loadDatasetBtn.setVisible(true);
        this.uploadFileBtn.setVisible(true);
        this.filePathInput.setVisible(true);

    }

    @FXML
    void loadDataSet(ActionEvent event) throws IOException {

        if (CURRENT_DATASET == null) {
            CURRENT_DATASET = BasicPreProcessor.loadDataSetFrom(dataminingproject.DataMiningProject.DATASETFILEPATH);
        }
        filePathInput.setText(dataminingproject.DataMiningProject.DATASETFILEPATH.substring(dataminingproject.DataMiningProject.DATASETFILEPATH.lastIndexOf("\\")));

        BounceInRight f = new BounceInRight((this.cluterBtn));
        //f.setSpeed(0.2);

        BounceInDown f2 = new BounceInDown((this.miningBtn));
        //f.setSpeed(0.3);

        BounceInUp f3 = new BounceInUp((this.preprocessBtn));
        //f.setSpeed(0.5);
        BounceInLeft f4 = new BounceInLeft((this.visualizeBtn));
        //f.setSpeed(0.3);

        
        /*f.playOnFinished(f2);
        f2.playOnFinished(f3);
        f3.playOnFinished(f4);*/
        
        Platform.runLater(() -> {
            this.cluterBtn.setVisible(true);
            f.play();
            this.miningBtn.setVisible(true);
            f2.play();
            this.preprocessBtn.setVisible(true);
            f3.play();
            this.visualizeBtn.setVisible(true);
            f4.play();
            
            
            
        });

       
        
        
        

    }

    @FXML
    void openClusteringWindow(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/Views/ClusteringGUI.fxml"));
        Parent fxml = (Parent) loader.load();
        ClusteringGUIController controller=loader.getController();
        controller.CURRENT_DATASET=CURRENT_DATASET;
        Scene scene = new Scene(fxml);

        Stage stg = new Stage();
        stg.setScene(scene);
        stg.initStyle(StageStyle.UNDECORATED);
        stg.show();

    }

    @FXML
    void openMiningFPWindow(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/Views/MiningAlgorithmsGUI.fxml"));
        Parent fxml = (Parent) loader.load();
        AprioriGUIController.CURRENT_DATASET=CURRENT_DATASET;
        EclatGUIController.CURRENT_DATASET=CURRENT_DATASET;
        
        Scene scene = new Scene(fxml);

        Stage stg = new Stage();
        stg.setScene(scene);
        stg.initStyle(StageStyle.UNDECORATED);
        stg.show();
    }

    @FXML
    void openPreprocessingWindow(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/Views/PreProcessingWindow.fxml"));
        Parent fxml = (Parent) loader.load();
        PreProcessingWindowController controller=(PreProcessingWindowController) loader.getController();
        PreProcessingWindowController.CURRENT_DATASET=CURRENT_DATASET;
        controller.displayAttributs();

        Scene scene = new Scene(fxml);

        Stage stg = new Stage();
        stg.setScene(scene);
        stg.initStyle(StageStyle.UNDECORATED);
        stg.show();
    }

    @FXML
    void openVisualizeWindow(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/Views/PrevisualizeWindow.fxml"));
        Parent fxml = (Parent) loader.load();

        PrevisualizeWindowController controller = (PrevisualizeWindowController) loader.getController();
        PrevisualizeWindowController.CURRENT_DATASET = CURRENT_DATASET;
        controller.loadDataSet(event);
        Scene scene = new Scene(fxml);

        Stage stg = new Stage();
        stg.setScene(scene);
        stg.initStyle(StageStyle.UNDECORATED);
        stg.show();
    }

    private void openFile(File file) {
        try {
            Desktop.getDesktop().open(file);
            dataminingproject.DataMiningProject.DATASETFILEPATH = file.getAbsolutePath();
        } catch (IOException ex) {

        }
    }

    //Getters and setters
    public Button getUploadFileBtn() {
        return uploadFileBtn;
    }

    public void setUploadFileBtn(Button uploadFileBtn) {
        this.uploadFileBtn = uploadFileBtn;
    }

    public TextField getFilePathInput() {
        return filePathInput;
    }

    public void setFilePathInput(TextField filePathInput) {
        this.filePathInput = filePathInput;
    }

    public Button getLoadDatasetBtn() {
        return loadDatasetBtn;
    }

    public void setLoadDatasetBtn(Button loadDatasetBtn) {
        this.loadDatasetBtn = loadDatasetBtn;
    }

    public Text getStartLabel() {
        return startLabel;
    }

    public void setStartLabel(Text startLabel) {
        this.startLabel = startLabel;
    }

}
