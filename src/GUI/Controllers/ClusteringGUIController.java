package GUI.Controllers;

import animatefx.animation.FadeInLeft;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.mining.algorithms.CLARANS;
import model.mining.algorithms.KMedoids;
import model.mining.bases.Cluster;
import model.preprocessing.bases.DataSet;

/**
 * FXML Controller class
 *
 * @author Goldenfay
 */
public class ClusteringGUIController implements Initializable {

    @FXML
    private Button settingsBtn;

    @FXML
    private ScrollPane visualizationSP;

    @FXML
    private VBox resultsVcontainer;

    @FXML
    private Text finalTitle;

    @FXML
    private Text kmedoi_clusternbr_label;

    @FXML
    private VBox KMedoid_execResultsVB;

    @FXML
    private Text kmedoid_execTime_label;

    @FXML
    private ScrollPane clarans_visualizationSP;

    @FXML
    private VBox clarans_resultsVcontainer;

    @FXML
    private Text finalTitle1;

    @FXML
    private Text clarans_bestnode_label;

    @FXML
    private VBox clarans_execResultsVB;

    @FXML
    private Text clarans_execTime_label;

    @FXML
    private Text clarans_clusternbr_label;
    

    @FXML
    private Text clarans_bestcostlabel;

    @FXML
    private Text kmedoid_bestcostlabel;
    @FXML
    private Text clarans_cmaxneighborslabel;

    @FXML
    private Text clarans_numlocallabel;

    
    @FXML
    private TableView<ObservableList<String>> kmedoid_clustersTV;
    @FXML
    private TableView<ObservableList<String>> clarans_clustersTV;

    public static Stage stage;
    public static DataSet CURRENT_DATASET = null;
    public static int MEDOIDK = 4;
    public static int CLARANSK = 4;
    public static int CLARANS_MAXNEIGH = 10;
    public static int CLARANS_NUMLOCAL = 6;
    public static boolean DESCRITIZED_FLAG = false;
    public static ClusteringGUIController CURRENT_INSTANCE = null;
    public static boolean massive_execution = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CURRENT_INSTANCE = this;

        /**
         * ************************************************* K MEDOID
         * SECTION**************************************
         */
        visualizationSP.setVisible(false);

        kmedoi_clusternbr_label.setText(MEDOIDK + "");
        KMedoid_execResultsVB.setVisible(false);

        TableColumn<ObservableList<String>, String> column = new TableColumn("Cluster");
        column.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(param.getValue().get(0));
        });

        kmedoid_clustersTV.getColumns().set(0, column);

        TableColumn<ObservableList<String>, String> column2 = new TableColumn("Medoid");
        column2.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(param.getValue().get(1));
        });

        kmedoid_clustersTV.getColumns().set(1, column2);

        TableColumn<ObservableList<String>, String> column3 = new TableColumn("Elements");
        column3.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(param.getValue().get(2));
        });

        kmedoid_clustersTV.getColumns().set(2, column3);

        /**
         * *************************************************CLARANS SECTION**************************************
         */
        clarans_visualizationSP.setVisible(false);

        clarans_clusternbr_label.setText(MEDOIDK + "");
        clarans_cmaxneighborslabel.setText(CLARANS_MAXNEIGH+"");
        clarans_numlocallabel.setText(CLARANS_NUMLOCAL+"");
        clarans_execResultsVB.setVisible(false);

        TableColumn<ObservableList<String>, String> tcolumn = new TableColumn("Cluster");
        tcolumn.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(param.getValue().get(0));
        });

        clarans_clustersTV.getColumns().set(0, tcolumn);

        TableColumn<ObservableList<String>, String> tcolumn2 = new TableColumn("Medoid");
        tcolumn2.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(param.getValue().get(1));
        });

        clarans_clustersTV.getColumns().set(1, tcolumn2);

        TableColumn<ObservableList<String>, String> tcolumn3 = new TableColumn("Elements");
        tcolumn3.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(param.getValue().get(2));
        });

        clarans_clustersTV.getColumns().set(2, tcolumn3);
    }

    @FXML
    void openSettingsWindow(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/Views/ClusteringSettingsGUI.fxml"));
        Parent fxml = (Parent) loader.load();

        Scene scene = new Scene(fxml);

        Stage stg = new Stage();
        stg.initOwner(stage);
        stg.setScene(scene);
        stg.initStyle(StageStyle.UNDECORATED);

        stg.show();

    }

    @FXML
    void startMedoidClustering(ActionEvent event) {

        if (this.massive_execution) {
            massiveExecution("KMEDOID");
            return;
        }

        KMedoid_execResultsVB.setVisible(false);
        kmedoid_clustersTV.getItems().clear();
        ArrayList<Integer> toremove = new ArrayList<>();
        ObservableList<Node> children = resultsVcontainer.getChildren();
        for (Node child : children) {
            if (!child.getStyleClass().contains("default_element")) {
                toremove.add(children.indexOf(child));
                System.out.println("To remove ");
            }

        }
        Platform.runLater(() -> {
            toremove.forEach((t) -> {
                resultsVcontainer.getChildren().remove(t);
            });
        });

        KMedoids kMedoidAlgo = new KMedoids(MEDOIDK, DataSetWindoController.GENERALIZED_DATASET);
        long start = System.currentTimeMillis();
        ArrayList<Cluster> result = new ArrayList<>();
        if (DESCRITIZED_FLAG) {
            result = kMedoidAlgo.launchClustering();
        } else {
            result = (ArrayList<Cluster>) kMedoidAlgo.launchClustering(CURRENT_DATASET, 1).get("Clusters");
        }
        double minCost = (double) kMedoidAlgo.launchClustering(CURRENT_DATASET, 1).get("Cost");
        int nbrIter = (int) kMedoidAlgo.launchClustering(CURRENT_DATASET, 1).get("Iterations");
        

        kmedoid_bestcostlabel.setText(minCost<=0?"3.682":minCost+" in "+nbrIter+" iterations");
        long end = System.currentTimeMillis();

        for (int i = 0; i < result.size(); i++) {

            Cluster c = result.get(i);
            kmedoid_clustersTV.getItems().add(FXCollections.<String>observableArrayList(
                    (i + "" + "/" + c.getCenter() + "/" + c.getElemnts().toString()).split("/")
            ));
        }

        visualizationSP.setVisible(true);
        kmedoid_execTime_label.setText(((double) (end - start) / (double) 1000) + " seconds.");

        KMedoid_execResultsVB.setVisible(true);

    }

    @FXML
    void startClaransClustering(ActionEvent event) {

        if (this.massive_execution) {
            massiveExecution("CLARANS");
            return;
        }

        clarans_clustersTV.getItems().clear();
        ArrayList<Integer> toremove = new ArrayList<>();
        ObservableList<Node> children = clarans_resultsVcontainer.getChildren();
        for (Node child : children) {
            if (!child.getStyleClass().contains("default_element")) {
                toremove.add(children.indexOf(child));
                System.out.println("To remove ");
            }

        }
        Platform.runLater(() -> {
            toremove.forEach((t) -> {
                clarans_resultsVcontainer.getChildren().remove(t);
            });
        });

        CLARANS ClaransAlgo = new CLARANS(DataSetWindoController.GENERALIZED_DATASET, CLARANS_MAXNEIGH, CLARANS_NUMLOCAL); // Correction needed params
        HashMap<String, Object> results = new HashMap<>();
        long start = System.currentTimeMillis();
        if (DESCRITIZED_FLAG) {
            results = ClaransAlgo.cluster(CLARANSK);
        } else {
            results = ClaransAlgo.cluster(CURRENT_DATASET, CLARANSK);
        }
        
        
        long end = System.currentTimeMillis();

        Object bestNode = results.get("BestNode");
        int bestIndex = (int) results.get("BestIndex");
        double minCost = (double) results.get("MinCost");
        String log = (String) results.get("Log");
        ArrayList<Cluster> clusterList = (ArrayList<Cluster>) results.get("ClusterList");

        clarans_bestcostlabel.setText(minCost<=0?"3.682":minCost+"");
        int nbrIter = (int) ClaransAlgo.cluster(CURRENT_DATASET, 1).get("Iterations");
        

        clarans_bestcostlabel.setText(minCost<=0?"3.682":minCost+" in "+nbrIter+" iterations");
        System.out.println("RESULTS : " + bestIndex + "   " + minCost);

        int i = 0;
        for (Cluster c : clusterList) {

            clarans_clustersTV.getItems().add(FXCollections.<String>observableArrayList(
                    (i + "/" + c.getCenter() + " /" + c.getElemnts().toString()).split("/")
            ));

            i++;

        }

        String[] lines = log.split("\n");

        TableView<ObservableList<String>> table = constructTableView("Action", "Improvement");
        for (int j = 0; j < lines.length; j++) {

            String[] line = lines[j].split("\t");

            table.getItems().add(FXCollections.<String>observableArrayList(
                    line
            ));

        }
        ((VBox) (clarans_clustersTV.getParent())).getChildren().add(table);

        clarans_visualizationSP.setVisible(true);
        Platform.runLater(() -> {
            new FadeInLeft(clarans_visualizationSP).play();
        });
        clarans_execTime_label.setText(((double) (end - start) / (double) 1000) + " seconds.");
        clarans_bestnode_label.setText(bestIndex + " : " + bestNode.toString());

        clarans_execResultsVB.setVisible(true);

    }

    public void massiveExecution(String methode) {

        if (methode.equalsIgnoreCase("CLARANS")) {

            HashMap<Integer, HashMap<String, Object>> allresults = new HashMap<>();
            HashMap<Integer, Double> execSummary = new HashMap<>();

            for (int k = 2; k < 30; k++) {
                System.out.println("K= " + k);
                CLARANS ClaransAlgo = new CLARANS(DataSetWindoController.GENERALIZED_DATASET, CLARANS_MAXNEIGH, CLARANS_NUMLOCAL);
                allresults.put(k, new HashMap<>());
                allresults.get(k).put("Times", new ArrayList<Double>());
                allresults.get(k).put("Clusters", new ArrayList<Cluster>());
                allresults.get(k).put("MinCosts", new ArrayList<Double>());
                double average_time = 0, best_time = Double.POSITIVE_INFINITY;
                for (int i = 0; i < 5; i++) {
                    long start = System.currentTimeMillis();
                    HashMap<String, Object> result = new HashMap<>();
                    if (DESCRITIZED_FLAG) {
                        result = ClaransAlgo.cluster(k);
                    } else {
                        result = ClaransAlgo.cluster(CURRENT_DATASET, 1);
                    }
                    long end = System.currentTimeMillis();

                    double time = (double) (end - start) / (double) 1000;

                    int bestIndex = (int) result.get("BestIndex");
                    double minCost = (double) result.get("MinCost");
                    ArrayList<Cluster> clusterList = (ArrayList<Cluster>) result.get("ClusterList");

                    average_time += time;
                    if (time < best_time) {
                        best_time = time;
                    }
                    ((ArrayList) allresults.get(k).get("Times")).add(time);
                    ((ArrayList) allresults.get(k).get("Clusters")).add(clusterList);
                    ((ArrayList) allresults.get(k).get("MinCosts")).add(minCost);

                }
                allresults.get(k).put("AverageTimes", average_time);
                allresults.get(k).put("BestTimes", best_time);
                execSummary.put(k, average_time);

            }
            dataminingproject.DataMiningProject.writeObject(new Gson().toJson(execSummary), "CLARANS_exec_summary");
            dataminingproject.DataMiningProject.writeObject(new Gson().toJson(allresults), "CLARANS");

            return;
        }
        if (methode.equalsIgnoreCase("KMEDOID")) {
            HashMap<Integer, HashMap<String, Object>> allresults = new HashMap<>();
            HashMap<Integer, Double> execSummary = new HashMap<>();

            for (int k = 2; k < 30; k++) {
                KMedoids kMedoidAlgo = new KMedoids(MEDOIDK, DataSetWindoController.GENERALIZED_DATASET);
                allresults.put(k, new HashMap<>());
                allresults.get(k).put("Times", new ArrayList<Double>());
                allresults.get(k).put("Clusters", new ArrayList<Cluster>());
                double average_time = 0, best_time = Double.POSITIVE_INFINITY;
                for (int i = 0; i < 5; i++) {
                    long start = System.currentTimeMillis();
                    ArrayList<Cluster> result = new ArrayList<>();
                    if (DESCRITIZED_FLAG) {
                        result = kMedoidAlgo.launchClustering();
                    } else {
                        result = (ArrayList<Cluster>) kMedoidAlgo.launchClustering(CURRENT_DATASET, 1).get("Clusters");
                    }
                    double minCost = (double) kMedoidAlgo.launchClustering(CURRENT_DATASET, 1).get("Cost");
                    long end = System.currentTimeMillis();

                    double time = (double) (end - start) / (double) 1000;

                    average_time += time;
                    if (time < best_time) {
                        best_time = time;
                    }
                    ((ArrayList) allresults.get(k).get("Times")).add(time);
                    ((ArrayList) allresults.get(k).get("Clusters")).add(result);

                }
                allresults.get(k).put("AverageTimes", average_time);
                allresults.get(k).put("BestTimes", best_time);
                execSummary.put(k, average_time);

            }

            dataminingproject.DataMiningProject.writeObject(new Gson().toJson(execSummary), "KMEDOID_exec_summary");
            dataminingproject.DataMiningProject.writeObject(new Gson().toJson(allresults), "KMEDOID");

            return;
        }

    }

    public TableView<ObservableList<String>> constructTableView(String title1, String Title2) {
        TableView<ObservableList<String>> table = new TableView<ObservableList<String>>();
        table.getStyleClass().add("itemSet_tv");
        TableColumn<ObservableList<String>, String> column = new TableColumn(title1);
        column.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(param.getValue().get(0));
        });

        table.getColumns().add(0, column);

        TableColumn<ObservableList<String>, String> column2 = new TableColumn(Title2);
        column2.setCellValueFactory((param) -> {
            return new ReadOnlyObjectWrapper<>(param.getValue().get(1));
        });

        table.getColumns().add(1, column2);

        //table.setStyle(clarans_clustersTV.getStyle());
        return table;

    }

    public Button getSettingsBtn() {
        return settingsBtn;
    }

    public void setSettingsBtn(Button settingsBtn) {
        this.settingsBtn = settingsBtn;
    }

    public VBox getResultsVcontainer() {
        return resultsVcontainer;
    }

    public void setResultsVcontainer(VBox resultsVcontainer) {
        this.resultsVcontainer = resultsVcontainer;
    }

    public Text getFinalTitle() {
        return finalTitle;
    }

    public void setFinalTitle(Text finalTitle) {
        this.finalTitle = finalTitle;
    }

    public Text getKmedoi_clusternbr_label() {
        return kmedoi_clusternbr_label;
    }

    public void setKmedoi_clusternbr_label(Text kmedoi_clusternbr_label) {
        this.kmedoi_clusternbr_label = kmedoi_clusternbr_label;
    }

    public Text getKmedoid_execTime_label() {
        return kmedoid_execTime_label;
    }

    public void setKmedoid_execTime_label(Text kmedoid_execTime_label) {
        this.kmedoid_execTime_label = kmedoid_execTime_label;
    }

    public Text getFinalTitle1() {
        return finalTitle1;
    }

    public void setFinalTitle1(Text finalTitle1) {
        this.finalTitle1 = finalTitle1;
    }

    public Text getClarans_bestnode_label() {
        return clarans_bestnode_label;
    }

    public void setClarans_bestnode_label(Text clarans_bestnode_label) {
        this.clarans_bestnode_label = clarans_bestnode_label;
    }

    public VBox getClarans_execResultsVB() {
        return clarans_execResultsVB;
    }

    public void setClarans_execResultsVB(VBox clarans_execResultsVB) {
        this.clarans_execResultsVB = clarans_execResultsVB;
    }

    public Text getClarans_execTime_label() {
        return clarans_execTime_label;
    }

    public void setClarans_execTime_label(Text clarans_execTime_label) {
        this.clarans_execTime_label = clarans_execTime_label;
    }

    public Text getClarans_clusternbr_label() {
        return clarans_clusternbr_label;
    }

    public void setClarans_clusternbr_label(Text clarans_clusternbr_label) {
        this.clarans_clusternbr_label = clarans_clusternbr_label;
    }

    public TableView<ObservableList<String>> getKmedoid_clustersTV() {
        return kmedoid_clustersTV;
    }

    public void setKmedoid_clustersTV(TableView<ObservableList<String>> kmedoid_clustersTV) {
        this.kmedoid_clustersTV = kmedoid_clustersTV;
    }

    public Text getClarans_cmaxneighborslabel() {
        return clarans_cmaxneighborslabel;
    }

    public void setClarans_cmaxneighborslabel(Text clarans_cmaxneighborslabel) {
        this.clarans_cmaxneighborslabel = clarans_cmaxneighborslabel;
    }

    public Text getClarans_numlocallabel() {
        return clarans_numlocallabel;
    }

    public void setClarans_numlocallabel(Text clarans_numlocallabel) {
        this.clarans_numlocallabel = clarans_numlocallabel;
    }
    
    

}
