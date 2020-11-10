package GUI.Controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
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
import model.mining.algorithms.Apriori;
import model.mining.bases.AssociationRule;
import model.mining.bases.FrequentSet;
import model.preprocessing.bases.DataSet;
import model.preprocessing.preprocessors.BasicPreProcessor;
import static GUI.Controllers.DataSetWindoController.GENERALIZED_DATASET;
import animatefx.animation.Flash;
import com.google.gson.Gson;
import dataminingproject.DataMiningProject;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import model.preprocessing.bases.generalDataSet;
import model.preprocessing.bases.generalRow;

/**
 * FXML Controller class
 *
 * @author SELMANE
 */
public class AprioriGUIController implements Initializable {

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
    private VBox resultsVcontainer;

    @FXML
    private Text itemSetsTitle;

    @FXML
    private Text minSupport_label;

    @FXML
    private Text minConfidence_label;

    @FXML
    private VBox execResultsVB;

    @FXML
    private Text execTime_label;

    @FXML
    private Text max_level_label;

    @FXML
    private Text assoc_rule_label;
    @FXML
    private Text error_label;

    @FXML
    private TableView<ObservableList<String>> singletonsTV;

    public static Stage stage;
    public static DataSet CURRENT_DATASET = null;
    public static int MINIMUM_SUPPORT = 30;
    public static double MINIMUM_CONFIDENCE = 0.4;
    public static AprioriGUIController CURRENT_INSTANCE = null;
    public static boolean massive_execution = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CURRENT_INSTANCE = this;
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

        minSupport_label.setText(60 + "%");
        minConfidence_label.setText(80+ "%");

        TableColumn<ObservableList<String>, String> column = new TableColumn("ItemSet");
        column.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(param.getValue().get(0));
        });

        singletonsTV.getColumns().set(0, column);

        TableColumn<ObservableList<String>, String> column2 = new TableColumn("Support");
        column2.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(param.getValue().get(1));
        });

        singletonsTV.getColumns().set(1, column2);

    }

    @FXML
    void visualizeDataSet(ActionEvent event) throws IOException {
         if (GENERALIZED_DATASET == null) {
            try {
                GENERALIZED_DATASET=(generalDataSet) DataMiningProject.loadObject("descritizedDataset");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("ERROR");
            }
         }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/Views/PrevisualizeWindow.fxml"));
        Parent fxml = (Parent) loader.load();

        PrevisualizeWindowController cntr = (PrevisualizeWindowController) loader.getController();
        cntr.loadDiscritizedDataSet(GENERALIZED_DATASET);
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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/Views/AprioriSettingsGUI.fxml"));
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
        if (GENERALIZED_DATASET == null) {
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
        System.out.println("Min support " + MINIMUM_SUPPORT + "  Min confidence  " + MINIMUM_CONFIDENCE);
        visualizationSP.setVisible(true);

        // Cleaning ...
        singletonsTV.getItems().clear();
        ArrayList<Integer> toremove = new ArrayList<>();
        ObservableList<Node> children = resultsVcontainer.getChildren();
        for (Node child : children) {
            if (!child.getStyleClass().contains("default_element")) {
                toremove.add(children.indexOf(child));
            }

        }
        toremove.forEach((t) -> {
            resultsVcontainer.getChildren().remove(t);
        });

        if (massive_execution) {
            massive_execution();
            return;
        }
        startAprioriMining();

    }

    public void startAprioriMining() {

        //System.gc();
        long start = System.currentTimeMillis();
        Apriori aprioriAlgo = new Apriori(GENERALIZED_DATASET, (int) MINIMUM_SUPPORT * GENERALIZED_DATASET.getInstances().size() / 100, MINIMUM_CONFIDENCE);
        HashSet<FrequentSet> singletons = aprioriAlgo.extractSingletonItemSets();
        if (singletons.isEmpty()) {
            return;
        }
        // Display results
        Iterator<FrequentSet> it = singletons.iterator();

        while (it.hasNext()) {

            FrequentSet val = it.next();
            singletonsTV.getItems().add(FXCollections.<String>observableArrayList(
                    (val.toString() + "/" + val.getSupport()).split("/")
            ));
        }

        // Go mining from 2-ItemSets ...
        HashMap<Integer, HashSet<FrequentSet>> all_I_itemSets = aprioriAlgo.forwardExtraction(singletons);

        if (all_I_itemSets.isEmpty()) {

            System.out.println("I-FrequentItemSets (i>1) is empty . Search endedn on 1-Frequent ItemSets.");

            return;
        }

        int maxLevel = (int) Collections.max(all_I_itemSets.keySet());

        for (int l = 2; l < maxLevel; l++) {
            int level = l;
            TableView<ObservableList<String>> table = constructTableView();

            HBox container = new HBox();
            container.setStyle(itemSetsTitle.getParent().getStyle());
            Text title = new Text(level + "-ItemSets");
            title.setStyle(itemSetsTitle.getStyle());
            title.getStyleClass().add("itemSetTitle_txt");
            HBox.setMargin(title, new Insets(0, 0, 0, 10));

            container.getChildren().add(title);

            ((VBox) (singletonsTV.getParent())).getChildren().add(container);

            HashSet<FrequentSet> elements = all_I_itemSets.get(level);
            for (FrequentSet element : elements) {
                table.getItems().add(FXCollections.<String>observableArrayList(
                        (element.toString() + "/" + element.getSupport()).split("/")
                ));
            }

            ((VBox) (singletonsTV.getParent())).getChildren().add(table);
        }

        TableView<ObservableList<String>> rulesTV = constructTableView();
        rulesTV.getColumns().get(0).setText("Rule");
        rulesTV.getColumns().get(1).setText("Confidence");

        HBox container = new HBox();
        container.setStyle(itemSetsTitle.getParent().getStyle());
        container.getStyleClass().remove("default_element");
        Text title = new Text("Association Rules :");
        title.setStyle(itemSetsTitle.getStyle());
        title.getStyleClass().add("itemSetTitle_txt");
        HBox.setMargin(title, new Insets(0, 0, 0, 10));

        container.getChildren().add(title);

        ((VBox) (singletonsTV.getParent())).getChildren().add(container);

        // Extract association rules
        if (all_I_itemSets.get(maxLevel).isEmpty()) {
            maxLevel--;
        }
        final int max = maxLevel;
        int cpt_rules = 0;
        for (FrequentSet t : all_I_itemSets.get(maxLevel)) {

            ArrayList<AssociationRule> rules = AssociationRule.extractAssociationRules(t, GENERALIZED_DATASET, MINIMUM_CONFIDENCE);
            System.gc();
            cpt_rules += rules.size();

            if (rules.isEmpty()) {
                System.out.println("No rule with confidence higher then threshold.");
                return;
            }
            for (AssociationRule rule : rules) {
                rulesTV.getItems().add(FXCollections.<String>observableArrayList(
                        (rule.toString() + "/" + rule.calculateConfidence(GENERALIZED_DATASET)).split("/")
                ));
                System.out.println(rule);
            }

        }
        // Show results
        long end = System.currentTimeMillis();
        execTime_label.setText(((double) (end - start) / (double) 1000) + " seconds.");
        max_level_label.setText(max + "");
        assoc_rule_label.setText(cpt_rules + "");

        ((VBox) (singletonsTV.getParent())).getChildren().add(rulesTV);

    }

    public void massive_execution() {
        HashMap<String, HashMap<String, Object>> allresults = new HashMap<>();
        HashMap<String, Double> execSummary = new HashMap<>();
        String[] headers = {"Minimum Support", "Average Execution Time", "Maximum Frequent ItemSets Level", "Average Confidence"};
        for (float support = 0.05f; support <= 1f; support += 0.05f) {
            System.out.println("*************************EXECUTION: Minimum support=" + support);
            long start = System.currentTimeMillis();
            Apriori aprioriAlgo = new Apriori(GENERALIZED_DATASET, (int) support * GENERALIZED_DATASET.getInstances().size() / 100, MINIMUM_CONFIDENCE);
            HashSet<FrequentSet> singletons = aprioriAlgo.extractSingletonItemSets();
            if (singletons.isEmpty()) {
                return;
            }

            // Go mining from 2-ItemSets ...
            HashMap<Integer, HashSet<FrequentSet>> all_I_itemSets = aprioriAlgo.forwardExtraction(singletons);

            if (all_I_itemSets.isEmpty()) {

                System.out.println("I-FrequentItemSets (i>1) is empty . Search endedn on 1-Frequent ItemSets.");

                return;
            }

            int maxLevel = (int) Collections.max(all_I_itemSets.keySet());

            // Extract association rules
            if (all_I_itemSets.get(maxLevel).isEmpty()) {
                maxLevel--;
            }
            double average_confidence = 0;
            int cpt_rules = 0;
            for (FrequentSet t : all_I_itemSets.get(maxLevel)) {

                ArrayList<AssociationRule> rules = AssociationRule.extractAssociationRules(t, GENERALIZED_DATASET, MINIMUM_CONFIDENCE);
                System.gc();
                cpt_rules += rules.size();
                // Show results

                if (rules.isEmpty()) {
                    System.out.println("No rule with confidence higher then threshold.");
                    return;
                }

                for (AssociationRule rule : rules) {
                    average_confidence += rule.calculateConfidence(GENERALIZED_DATASET);

                }

            }
            long end = System.currentTimeMillis();
            average_confidence = average_confidence / cpt_rules;

            execSummary.put((support + "").substring(0, 3), (double) (end - start) / (double) 1000);
            HashMap<String, Object> entry = new HashMap<String, Object>();
            entry.put("Minimum Support", (support + "").substring(0, 3));
            entry.put("Average Execution Time", (double) (end - start) / (double) 1000);
            entry.put("Average Confidence", (double) (end - start) / (double) 1000);
            entry.put("Maximum Frequent ItemSets Level", maxLevel);

            allresults.put(("Min_supp" + support + "").substring(0, 3), entry);

            try {
                DataMiningProject.writeCsv("Apriori_results", allresults, (ArrayList<String>) (Arrays.asList(headers)));
            } catch (FileNotFoundException ex) {
                System.err.println("Couldn't write on csv File");
            }

            dataminingproject.DataMiningProject.writeObject(new Gson().toJson(execSummary), "Apriori_summary");
            dataminingproject.DataMiningProject.writeObject(new Gson().toJson(execSummary), "Apriori");
        }

    }

    public TableView<ObservableList<String>> constructTableView() {
        TableView<ObservableList<String>> table = new TableView<ObservableList<String>>();
        table.getStyleClass().add("itemSet_tv");
        TableColumn<ObservableList<String>, String> column = new TableColumn("ItemSet");
        column.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(param.getValue().get(0));
        });

        table.getColumns().add(0, column);

        TableColumn<ObservableList<String>, String> column2 = new TableColumn("Support");
        column2.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(param.getValue().get(1));
        });

        table.getColumns().add(1, column2);

        table.setStyle(singletonsTV.getStyle());
        return table;

    }

    private void openFile(File file) {
        try {
            Desktop.getDesktop().open(file);
            dataminingproject.DataMiningProject.DATASETFILEPATH = file.getAbsolutePath();
        } catch (IOException ex) {

        }
    }

    // Getters & Setters 
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

    public Button getLoadDatasetBtn() {
        return loadDatasetBtn;
    }

    public void setLoadDatasetBtn(Button loadDatasetBtn) {
        this.loadDatasetBtn = loadDatasetBtn;
    }

    public Text getItemSetsTitle() {
        return itemSetsTitle;
    }

    public void setItemSetsTitle(Text itemSetsTitle) {
        this.itemSetsTitle = itemSetsTitle;
    }

    public Text getError_label() {
        return error_label;
    }

    public void setError_label(Text error_label) {
        this.error_label = error_label;
    }

    public static boolean isMassive_execution() {
        return massive_execution;
    }

    public static void setMassive_execution(boolean massive_execution) {
        AprioriGUIController.massive_execution = massive_execution;
    }

    
    
    
}
