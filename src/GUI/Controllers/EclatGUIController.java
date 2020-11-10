package GUI.Controllers;

import static GUI.Controllers.DataSetWindoController.GENERALIZED_DATASET;
import animatefx.animation.Flash;
import dataminingproject.DataMiningProject;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.mining.algorithms.Eclat;
import model.mining.bases.AssociationRule;
import model.mining.bases.FrequentSet;
import model.preprocessing.bases.DataSet;
import model.preprocessing.bases.generalDataSet;
import model.preprocessing.preprocessors.BasicPreProcessor;

/**
 * FXML Controller class
 *
 * @author Goldenfay
 */
public class EclatGUIController implements Initializable {

    @FXML
    private Button settingsBtn;

    @FXML
    private Button uploadFileBtn;

    @FXML
    private TextField filePathInput;

    @FXML
    private Button loadDatasetBtn;

    @FXML
    private ScrollPane visualizationSP;

    @FXML
    private Text itemSetsTitle;


    @FXML
    private VBox execResultsVB;

    @FXML
    private Text execTime_label;

    @FXML
    private Text max_level_label;

    @FXML
    private Text assoc_rule_label;

    @FXML
    private Text minSupport_label;

    @FXML
    private Text minConfidence_label;
    
    @FXML
    private Text error_label;


    @FXML
    private TableView<ObservableList<String>> inverseItemsTableTV;

    

    public static Stage stage;
    public static DataSet CURRENT_DATASET = null;
    public static int MINIMUM_SUPPORT=20;
    public static double MINIMUM_CONFIDENCE=0.4;
    public static EclatGUIController CURRENT_INSTANCE=null;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CURRENT_INSTANCE=this;
        visualizationSP.setVisible(false);
        FileChooser fileCh = new FileChooser();
        fileCh.getExtensionFilters().add(new FileChooser.ExtensionFilter("ARFF files (*.arff)", "*.arff"));
        fileCh.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));

        uploadFileBtn.setOnAction((event) -> {
            File file = fileCh.showOpenDialog(stage);
            if (file != null) {
                openFile(file);
            }
        });
        minSupport_label.setText(50+"");
        minConfidence_label.setText(80+"");

    }

    @FXML
    void visualizeDataSet(ActionEvent event) throws IOException {
        if (true) return;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/Views/MainWindow.fxml"));
        Parent fxml = (Parent) loader.load();

        PrevisualizeWindowController.CURRENT_DATASET = CURRENT_DATASET.descriticize();
        Scene scene = new Scene(fxml);

        Stage stg = new Stage();
        stg.initOwner(stage);
        stg.setScene(scene);
        stg.initStyle(StageStyle.UNDECORATED);

        stg.show();
        ((PrevisualizeWindowController) loader.getController()).loadDataSet(event);
    }

    @FXML
    void loadDataSet(ActionEvent event) throws IOException {

        CURRENT_DATASET = BasicPreProcessor.loadDataSetFrom(dataminingproject.DataMiningProject.DATASETFILEPATH);
        filePathInput.setText(dataminingproject.DataMiningProject.DATASETFILEPATH);

    }

    @FXML
    void openSettingsWindow(ActionEvent event) throws IOException {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/Views/EClatSettingsGUI.fxml"));
        Parent fxml = (Parent) loader.load();

        
        Scene scene = new Scene(fxml);

        Stage stg = new Stage();
        stg.initOwner(stage);
        stg.setScene(scene);
        stg.initStyle(StageStyle.UNDECORATED);

        stg.show();

    }

    @FXML
    void startMining(ActionEvent event) {
        
        error_label.getParent().setVisible(false);
        if(GENERALIZED_DATASET==null){
            
            try {
                GENERALIZED_DATASET=(generalDataSet) DataMiningProject.loadObject("descritizedDataset");
            } catch (Exception e) {
                e.printStackTrace();
                error_label.setText("No Discritized DataSet found ! Please discritize DataSet first.");
                error_label.getParent().setVisible(true);
                Platform.runLater(() -> {
                    new Flash(error_label.getParent()).play();
                });
                return;
            }
        }
        visualizationSP.setVisible(true);
        TableColumn<ObservableList<String>, String> column = new TableColumn("ItemSet");
        column.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(param.getValue().get(0));
        });

        inverseItemsTableTV.getColumns().set(0, column);

        TableColumn<ObservableList<String>, String> column2 = new TableColumn("Instances");
        column2.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(param.getValue().get(1));
        });

        inverseItemsTableTV.getColumns().set(1, column2);

        startEclatMining();
        System.out.println(inverseItemsTableTV.getColumns().size());
    }

    public void startEclatMining() {
        System.gc();
      
        long start = System.currentTimeMillis();
        Eclat eclatAlgo = new Eclat(DataSetWindoController.GENERALIZED_DATASET, (int) MINIMUM_SUPPORT * GENERALIZED_DATASET.getInstances().size() / 100, MINIMUM_CONFIDENCE);
        HashMap<FrequentSet, HashSet<Integer>> singletonsTable = eclatAlgo.constructReversedTable();
        if (singletonsTable.isEmpty()) {
            System.out.println("1-Itemsets is empty");
            return;
        }
        // Display results
        for (Map.Entry<FrequentSet, HashSet<Integer>> entry : singletonsTable.entrySet()) {
            inverseItemsTableTV.getItems().add(FXCollections.<String>observableArrayList(
                    (entry.getKey().toString() + "/" + entry.getValue().toString()).split("/")
            ));
        }

        // Go mining from 2-ItemSets ...
        HashMap<FrequentSet, HashSet<Integer>> finalTable = eclatAlgo.forwardExtraction(singletonsTable);

        if (finalTable.isEmpty()) {

            System.out.println("I-FrequentItemSets (i>1) is empty . Search endedn on 1-Frequent ItemSets.");

            return;
        }

        TableView<ObservableList<String>> table = constructTableView();
        HBox container = new HBox();
        container.setStyle(itemSetsTitle.getParent().getStyle());
        Text title = new Text("Final ItsemSets Vertical Table");
        title.setStyle(itemSetsTitle.getStyle());
        title.getStyleClass().add("itemSetTitle_txt");
        HBox.setMargin(title, new Insets(0, 0, 0, 10));

        container.getChildren().add(title);

        ((VBox) (inverseItemsTableTV.getParent())).getChildren().add(container);

        for (Map.Entry<FrequentSet, HashSet<Integer>> entry : finalTable.entrySet()) {
            System.out.println("FROM RESULTS : "+entry.getKey().toString()+"  "+entry.getValue().toString());
            table.getItems().add(FXCollections.<String>observableArrayList(
                    
                    (entry.getKey().toString() + "/" + entry.getValue().toString()).split("/")
            ));
        }

        ((VBox) (inverseItemsTableTV.getParent())).getChildren().add(table);

        // Association Rules
        TableView<ObservableList<String>> rulesTV = constructTableView();
        rulesTV.getColumns().get(0).setText("Rule");
        rulesTV.getColumns().get(1).setText("Confidence");

        HBox container2 = new HBox();
        container.setStyle(itemSetsTitle.getParent().getStyle());
        Text title2 = new Text("Association Rules :");
        title2.setStyle(itemSetsTitle.getStyle());
        title2.getStyleClass().add("itemSetTitle_txt");
        HBox.setMargin(title2, new Insets(0, 0, 0, 10));

        container2.getChildren().add(title2);

        ((VBox) (inverseItemsTableTV.getParent())).getChildren().add(container2);

        
        // Extract association rules
        int maxLevel=0;
        for(FrequentSet setKey:finalTable.keySet()){
            if(setKey.size()>maxLevel)
                maxLevel=setKey.size();
                        
        }
        
        long end = System.currentTimeMillis();
        execTime_label.setText(((double)(end-start)/(double)1000)+" seconds.");
        max_level_label.setText(maxLevel+"");
        int cptRules=0;
        for(FrequentSet setKey:finalTable.keySet()){
            if(setKey.size()!=maxLevel) continue;
                
            ArrayList<AssociationRule> rules = AssociationRule.extractAssociationRules(setKey,DataSetWindoController.GENERALIZED_DATASET, MINIMUM_CONFIDENCE);
            System.gc();
            
             // Show results
            
            
            

            if (rules.isEmpty()) {
                System.out.println("No rule with confidence higher then threshold.");
                return;
            }
            cptRules+=rules.size();
            for (AssociationRule rule : rules) {
                rulesTV.getItems().add(FXCollections.<String>observableArrayList(
                        (rule.toString() + "/" + rule.calculateConfidence(DataSetWindoController.GENERALIZED_DATASET)).split("/")
                ));
                System.out.println(rule);
            }
        }
        assoc_rule_label.setText(cptRules+"");
        

        ((VBox) (inverseItemsTableTV.getParent())).getChildren().add(rulesTV);

    }

    public TableView<ObservableList<String>> constructTableView() {
        TableView<ObservableList<String>> table = new TableView<ObservableList<String>>();
        table.getStyleClass().add("itemSet_tv");
        TableColumn<ObservableList<String>, String> column = new TableColumn("ItemSet");
        column.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(param.getValue().get(0));
        });

        table.getColumns().add(0, column);

        TableColumn<ObservableList<String>, String> column2 = new TableColumn("Instances ID");
        column2.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(param.getValue().get(1));
        });

        table.getColumns().add(1, column2);

        table.setStyle(inverseItemsTableTV.getStyle());
        return table;

    }

    private void openFile(File file) {
        try {
            Desktop.getDesktop().open(file);
            dataminingproject.DataMiningProject.DATASETFILEPATH = file.getAbsolutePath();
        } catch (IOException ex) {

        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
                // Getters and Setters

    public TextField getFilePathInput() {
        return filePathInput;
    }

    public void setFilePathInput(TextField filePathInput) {
        this.filePathInput = filePathInput;
    }

    public ScrollPane getVisualizationSP() {
        return visualizationSP;
    }

    public void setVisualizationSP(ScrollPane visualizationSP) {
        this.visualizationSP = visualizationSP;
    }

    public Text getItemSetsTitle() {
        return itemSetsTitle;
    }

    public void setItemSetsTitle(Text itemSetsTitle) {
        this.itemSetsTitle = itemSetsTitle;
    }

    public VBox getExecResultsVB() {
        return execResultsVB;
    }

    public void setExecResultsVB(VBox execResultsVB) {
        this.execResultsVB = execResultsVB;
    }

    public Text getExecTime_label() {
        return execTime_label;
    }

    public void setExecTime_label(Text execTime_label) {
        this.execTime_label = execTime_label;
    }

    public Text getMax_level_label() {
        return max_level_label;
    }

    public void setMax_level_label(Text max_level_label) {
        this.max_level_label = max_level_label;
    }

    public Text getAssoc_rule_label() {
        return assoc_rule_label;
    }

    public void setAssoc_rule_label(Text assoc_rule_label) {
        this.assoc_rule_label = assoc_rule_label;
    }

    public Text getMinSupport_label() {
        return minSupport_label;
    }

    public void setMinSupport_label(Text minSupport_label) {
        this.minSupport_label = minSupport_label;
    }

    public Text getMinConfidence_label() {
        return minConfidence_label;
    }

    public void setMinConfidence_label(Text minConfidence_label) {
        this.minConfidence_label = minConfidence_label;
    }

    public TableView<ObservableList<String>> getInverseItemsTableTV() {
        return inverseItemsTableTV;
    }

    public void setInverseItemsTableTV(TableView<ObservableList<String>> inverseItemsTableTV) {
        this.inverseItemsTableTV = inverseItemsTableTV;
    }
    
    

}
