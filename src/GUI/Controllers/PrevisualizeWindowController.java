package GUI.Controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import model.preprocessing.bases.DataSet;
import model.preprocessing.bases.generalDataSet;
import model.preprocessing.preprocessors.BasicPreProcessor;


/**
 * FXML Controller class
 *
 * @author Goldenfay
 */
public class PrevisualizeWindowController implements Initializable {

    @FXML
    private Button settingsBtn;
    
    @FXML
    private TextArea DSDescription;

    @FXML
    private ScrollPane visualizationSP;

    @FXML
    private Button uploadFileBtn;
    
    @FXML
    private TextField filePathInput;

    @FXML
    private TableView<ObservableList<String>> dataSetTV;
    
    @FXML
    private TableView<ObservableList<String>> attrTableDesc;

    @FXML
    private Button showPlotsButton;
    
    @FXML
    private Button showAttributsAnalysisBtn;

    
     @FXML
    private Text selectedColsText;

    public static Stage stage;
    private HashSet<Integer> selectedCols = new HashSet();
    public static BasicPreProcessor PRE_PROCESSOR = new BasicPreProcessor();
    public static DataSet CURRENT_DATASET = null;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        visualizationSP.setVisible(false);
        dataSetTV.setVisible(false);
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

    @FXML
    void openSettingsWindow(ActionEvent event) {

    }

    @FXML
    void loadDataSet(ActionEvent event) throws IOException {
        dataSetTV.getColumns().clear();
        dataSetTV.getItems().clear();
        attrTableDesc.getColumns().clear();
        attrTableDesc.getItems().clear();
        if(CURRENT_DATASET==null)
            CURRENT_DATASET = BasicPreProcessor.loadDataSetFrom(dataminingproject.DataMiningProject.DATASETFILEPATH);
        filePathInput.setText(dataminingproject.DataMiningProject.DATASETFILEPATH);
        
        DSDescription.setText(CURRENT_DATASET.getDescription().toString());
        showAttributsAnalysisBtn.setVisible(true);
        
        ScrollBar sb=new ScrollBar();
        
        constructAttrTable();

            // Affichage du tableau de Dataset
        dataSetTV.getColumns().clear();
        constructTableColumns();
        constructTableInstances();
        dataSetTV.getSelectionModel().setCellSelectionEnabled(true);
        final ObservableList<TablePosition> selectedCells = dataSetTV.getSelectionModel().getSelectedCells();

        selectedCells.addListener(new ListChangeListener<TablePosition>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends TablePosition> c) {

                for (TablePosition pos : selectedCells) {
                    selectedCols.add(pos.getColumn());
                    updateSelectedColsText();
                    System.out.println("POS : "+pos.getColumn());
                    if(showPlotsButton.isDisabled()){
                        showPlotsButton.setVisible(true);
                        showPlotsButton.setDisable(false);
                    }
                    
                    
                    dataSetTV.getSelectionModel().clearSelection(pos.getRow(),dataSetTV.getVisibleLeafColumn(0));
                }
            }
        });
        showAttributsAnalysisBtn.setDisable(false);
        visualizationSP.setVisible(true);
        dataSetTV.setVisible(true);

    }

    @FXML
    void openPlotsWindow(ActionEvent event) throws IOException {
        
        
        ShowAnalysisController.attributsToSummerize=new ArrayList<Integer>(selectedCols);
        // Then load the GUI
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/GUI/Views/showAnalysis.fxml"));
        Parent fxml=(Parent) loader.load();
        
        
        Scene scene=new Scene(fxml);
        
        Stage stg=new Stage();
        stg.initOwner(stage);
        stg.setScene(scene);
        stg.initStyle(StageStyle.UNDECORATED);
           
        stg.show();

    }
    
    
    @FXML
    void openAttrbitsAnalysisGUI(ActionEvent event) throws IOException {
        
        ShowAnalysisController.attributsToSummerize=new ArrayList<Integer>(selectedCols);
        // Then load the GUI
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/GUI/Views/attributsExploration.fxml"));
        Parent fxml=(Parent) loader.load();
        
        
        Scene scene=new Scene(fxml);
        
        Stage stg=new Stage();
        stg.initOwner(stage);
        stg.setScene(scene);
        stg.initStyle(StageStyle.UNDECORATED);
           
        stg.show();

    }
    
    
    
    public void loadDiscritizedDataSet(generalDataSet data){
        if(data==null) System.out.println("GUI.Controllers.PrevisualizeWindowController.loadDiscritizedDataSet()");
        dataSetTV.getColumns().clear();
        dataSetTV.getItems().clear();
        attrTableDesc.getColumns().clear();
        attrTableDesc.getItems().clear();
        filePathInput.setText(dataminingproject.DataMiningProject.DATASETFILEPATH);
        
        DSDescription.setText(data.getDescription().toString());
        showAttributsAnalysisBtn.setVisible(true);
        
     
        
        constructAttrTable(data);

            // Affichage du tableau de Dataset
        dataSetTV.getColumns().clear();
        constructTableColumns(data);
        constructTableInstances(data);
        dataSetTV.getSelectionModel().setCellSelectionEnabled(true);
        final ObservableList<TablePosition> selectedCells = dataSetTV.getSelectionModel().getSelectedCells();

        selectedCells.addListener(new ListChangeListener<TablePosition>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends TablePosition> c) {

                for (TablePosition pos : selectedCells) {
                    selectedCols.add(pos.getColumn());
                    updateSelectedColsText();
                    System.out.println("POS : "+pos.getColumn());
                    if(showPlotsButton.isDisabled()){
                        showPlotsButton.setVisible(true);
                        showPlotsButton.setDisable(false);
                    }
                    
                    
                    dataSetTV.getSelectionModel().clearSelection(pos.getRow(),dataSetTV.getVisibleLeafColumn(0));
                }
            }
        });
        showAttributsAnalysisBtn.setDisable(false);
        visualizationSP.setVisible(true);
        dataSetTV.setVisible(true);
        
    }
    

    
    private void constructAttrTable(){
        TableColumn<ObservableList<String>, String> column1 = new TableColumn("Attribute Name");
            column1.setCellValueFactory((param) -> {
                return new ReadOnlyObjectWrapper<>(param.getValue().get(0));

            });
            attrTableDesc.getColumns().add(column1);
            
            TableColumn<ObservableList<String>, String> column2 = new TableColumn("Attribute Type");
            column2.setCellValueFactory((param) -> {
                return new ReadOnlyObjectWrapper<>(param.getValue().get(1));

            });
            attrTableDesc.getColumns().add(column2);
            TableColumn<ObservableList<String>, String> column3 = new TableColumn("Attribute encoding type");
            column3.setCellValueFactory((param) -> {
                return new ReadOnlyObjectWrapper<>(param.getValue().get(2));

            });
            attrTableDesc.getColumns().add(column3);
      
        
        
        ObservableList columnsNameList = FXCollections.observableArrayList();
        
        for (int i = 0; i < CURRENT_DATASET.getAttributsName().size(); i++) {
            ArrayList<String> attributeRow=new ArrayList<>();
            
            attributeRow.add(CURRENT_DATASET.getAttributsName().get(i));
            attributeRow.add(CURRENT_DATASET.getAttributsType().get(i));
            attributeRow.add(CURRENT_DATASET.getAttributsCategory().get(i));
            
            columnsNameList.add(CURRENT_DATASET.getAttributsName().get(i));
            columnsNameList.add(CURRENT_DATASET.getAttributsType().get(i));
            columnsNameList.add(CURRENT_DATASET.getAttributsCategory().get(i));
            
            attrTableDesc.getItems().add(FXCollections.<String>observableArrayList(
                    attributeRow
            ));
        }
    }
    
    
    /**
     * Construction de la liste des column(arttributs) du DataSet pour la forme de tableView
     * @return 
     */

    private ArrayList<TableColumn> constructTableColumns() {
        ArrayList<TableColumn> listColumns = new ArrayList<TableColumn>();
        int nbrColumns = CURRENT_DATASET.getInstances().get(0).getValues().size();
        for (int i = 0; i < nbrColumns; i++) {
            final int index = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn(CURRENT_DATASET.getAttributsName().get(i));
            column.setCellValueFactory((param) -> {
                return new ReadOnlyObjectWrapper<>(param.getValue().get(index));

            });
            listColumns.add(new TableColumn("Attribute " + i));
            dataSetTV.getColumns().add(column);
        }

        return listColumns;

    }
    
    private void constructAttrTable(generalDataSet data){
        TableColumn<ObservableList<String>, String> column1 = new TableColumn("Attribute Name");
            column1.setCellValueFactory((param) -> {
                return new ReadOnlyObjectWrapper<>(param.getValue().get(0));

            });
            attrTableDesc.getColumns().add(column1);
            
            TableColumn<ObservableList<String>, String> column2 = new TableColumn("Attribute Type");
            column2.setCellValueFactory((param) -> {
                return new ReadOnlyObjectWrapper<>(param.getValue().get(1));

            });
            attrTableDesc.getColumns().add(column2);
            TableColumn<ObservableList<String>, String> column3 = new TableColumn("Attribute encoding type");
            column3.setCellValueFactory((param) -> {
                return new ReadOnlyObjectWrapper<>(param.getValue().get(2));

            });
            attrTableDesc.getColumns().add(column3);
      
        
        
        ObservableList columnsNameList = FXCollections.observableArrayList();
        
        for (int i = 0; i < data.getAttributsName().size(); i++) {
            ArrayList<String> attributeRow=new ArrayList<>();
            
            attributeRow.add(data.getAttributsName().get(i));
            attributeRow.add(data.getAttributsType().get(i));
            attributeRow.add(data.getAttributsCategory().get(i));
            
            columnsNameList.add(data.getAttributsName().get(i));
            columnsNameList.add(data.getAttributsType().get(i));
            columnsNameList.add(data.getAttributsCategory().get(i));
            
            attrTableDesc.getItems().add(FXCollections.<String>observableArrayList(
                    attributeRow
            ));
        }
    }
    
    
    /**
     * Construction de la liste des column(arttributs) du DataSet pour la forme de tableView
     * @return 
     */

    private ArrayList<TableColumn> constructTableColumns(generalDataSet data) {
        ArrayList<TableColumn> listColumns = new ArrayList<TableColumn>();
        int nbrColumns = data.getInstances().get(0).getValues().size();
        for (int i = 0; i < nbrColumns; i++) {
            final int index = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn(data.getAttributsName().get(i));
            column.setCellValueFactory((param) -> {
                return new ReadOnlyObjectWrapper<>(param.getValue().get(index));

            });
            listColumns.add(new TableColumn("Attribute " + i));
            dataSetTV.getColumns().add(column);
        }

        return listColumns;

    }

    /**
     * Construction des lignes(instances) du Dataset en forme du TableView
     * @return 
     */
    private ObservableList constructTableInstances() {

        ObservableList listeInstances = FXCollections.observableArrayList();

        for (int i = 0; i < CURRENT_DATASET.getInstances().size(); i++) {
            listeInstances.add(CURRENT_DATASET.getInstances().get(i).getValues());
            dataSetTV.getItems().add(FXCollections.<String>observableArrayList(
                    CURRENT_DATASET.getInstances().get(i).getValuesAsStringArray()
            ));
        }

        return listeInstances;

    }
    
     private ObservableList constructTableInstances(generalDataSet data) {

        ObservableList listeInstances = FXCollections.observableArrayList();

        for (int i = 0; i < data.getInstances().size(); i++) {
            listeInstances.add(data.getInstances().get(i).getValues());
            dataSetTV.getItems().add(FXCollections.<String>observableArrayList(
                    data.getInstances().get(i).getValuesAsStringArray()
            ));
        }

        return listeInstances;

    }
    
    
    private void updateSelectedColsText(){
        
        String text="";
        Object[] list= selectedCols.toArray();
        for(int i=0;i<list.length;i++)
            text+=(CURRENT_DATASET.getAttributsName().get((int)list[i]))+(i==(list.length-1)?"":",");
        
        selectedColsText.setText(text);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private void openFile(File file) {
        try {
            Desktop.getDesktop().open(file);
            dataminingproject.DataMiningProject.DATASETFILEPATH = file.getAbsolutePath();
        } catch (IOException ex) {

        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
                // Getters and Setters

    public ScrollPane getVisualizationSP() {
        return visualizationSP;
    }

    public void setVisualizationSP(ScrollPane visualizationSP) {
        this.visualizationSP = visualizationSP;
    }

    public Button getUploadFileBtn() {
        return uploadFileBtn;
    }

    public void setUploadFileBtn(Button uploadFileBtn) {
        this.uploadFileBtn = uploadFileBtn;
    }

    public TableView<ObservableList<String>> getDataSetTV() {
        return dataSetTV;
    }

    public void setDataSetTV(TableView<ObservableList<String>> dataSetTV) {
        this.dataSetTV = dataSetTV;
    }

    public TableView<ObservableList<String>> getAttrTableDesc() {
        return attrTableDesc;
    }

    public void setAttrTableDesc(TableView<ObservableList<String>> attrTableDesc) {
        this.attrTableDesc = attrTableDesc;
    }

    public Button getShowPlotsButton() {
        return showPlotsButton;
    }

    public void setShowPlotsButton(Button showPlotsButton) {
        this.showPlotsButton = showPlotsButton;
    }

    public Button getShowAttributsAnalysisBtn() {
        return showAttributsAnalysisBtn;
    }

    public void setShowAttributsAnalysisBtn(Button showAttributsAnalysisBtn) {
        this.showAttributsAnalysisBtn = showAttributsAnalysisBtn;
    }

    public Text getSelectedColsText() {
        return selectedColsText;
    }

    public void setSelectedColsText(Text selectedColsText) {
        this.selectedColsText = selectedColsText;
    }

    public HashSet<Integer> getSelectedCols() {
        return selectedCols;
    }

    public void setSelectedCols(HashSet<Integer> selectedCols) {
        this.selectedCols = selectedCols;
    }
    
    
    
    
    
    
    

}


//C:\Users\PC\Downloads\Compressed\M2-SII-S3 (2019-20)-20191011T190626Z-001\M2-SII-S3 (2019-20)\Data Mining\Projets\Data Mining Projet 19-20
