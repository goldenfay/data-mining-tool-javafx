package GUI.Controllers;

import animatefx.animation.BounceIn;
import animatefx.animation.BounceInLeft;
import dataminingproject.DataMiningProject;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.preprocessing.bases.DataSet;
import model.preprocessing.bases.Row;
import model.preprocessing.bases.generalDataSet;
import model.preprocessing.bases.generalRow;
import model.preprocessing.preprocessors.Measurement;

/**
 * FXML Controller class
 *
 * @author Goldenfay
 */
public class PreProcessingWindowController implements Initializable {

    @FXML
    private Accordion attrDescAccordion;

    @FXML
    private Text itemSetsTitle;

    @FXML
    private Button dicretizeBtn;

    @FXML
    private Text itemSetsTitle1;
    @FXML
    private Text currentDS_title;
    
    @FXML
    private Text success_text;

    @FXML
    private RadioButton equi_width_rb;

    @FXML
    private RadioButton equi_depth_rb;

    @FXML
    private Slider intervalNumberSlider;

    @FXML
    private Text intervalNumberLabel;

    @FXML
    private RadioButton mean_rb;

    @FXML
    private Accordion resultsAccordion;
    @FXML
    private Button cancelBtn;

    @FXML
    private Button confirmBtn;

    public static DataSet CURRENT_DATASET = null;

    public static DataSet RECOVER = null;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        attrDescAccordion.getPanes().clear();
        resultsAccordion.getPanes().clear();
        this.equi_width_rb.setSelected(true);
        this.success_text.setVisible(false);
        

        intervalNumberSlider.setMax(10);
        intervalNumberSlider.setMin(2);
        intervalNumberSlider.setValue(3);
        intervalNumberSlider.setBlockIncrement(1);
        intervalNumberLabel.setText("3");
        intervalNumberSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                intervalNumberLabel.setText((int) intervalNumberSlider.getValue() + "");
            }
        });

        ToggleGroup toggleGroup = new ToggleGroup();

        equi_depth_rb.setToggleGroup(toggleGroup);
        equi_width_rb.setToggleGroup(toggleGroup);
        mean_rb.setToggleGroup(toggleGroup);

// listen to changes in selected toggle
        toggleGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> System.out.println(newVal + " was selected"));

    }

    public void displayAttributs() {

        this.currentDS_title.setText(this.currentDS_title.getText()+" "+CURRENT_DATASET.getName());
        // Init components with attributs infos
        HashMap<String, Object> attrinfos = new HashMap<String, Object>();

        for (int i = 0; i < CURRENT_DATASET.getAttributsName().size(); i++) {
            if (!CURRENT_DATASET.isNumeric(i) || (!CURRENT_DATASET.getAttributsName().get(i).equalsIgnoreCase("resting_blood_pressure")
                    && !CURRENT_DATASET.getAttributsName().get(i).equalsIgnoreCase("serum_cholestoral")
                    && !CURRENT_DATASET.getAttributsName().get(i).equalsIgnoreCase("maximum_heart_rate_achieved")
                    && !CURRENT_DATASET.getAttributsName().get(i).equalsIgnoreCase("age"))) {
                continue;
            }
            HashMap<String, Double> FivenumberSummary = Measurement.fiveNumberSummary(CURRENT_DATASET, i);
            attrinfos.put(CURRENT_DATASET.getAttributsName().get(i), FivenumberSummary);
        }

        int nbr_numericAttr = attrinfos.keySet().size();

        if (nbr_numericAttr == 0) {

            return;
        }

        TitledPane[] panes = new TitledPane[nbr_numericAttr];
        int i = 0;
        for (String attr : attrinfos.keySet()) {
            HashMap<String, Double> summary = (HashMap<String, Double>) attrinfos.get(attr);
            panes[i] = new TitledPane();
            panes[i].setText(attr);
            panes[i].setId(attr);
            panes[i].setStyle("-fx-text-fill: #4b7ff8;");
            panes[i].applyCss();
            panes[i].getStyleClass().add(".titled-pane");

            VBox vb = new VBox();
            String[] heads = {"Min", "Q1", "Median", "Q3", "Max"};
            for (int j = 0; j < heads.length; j++) {

                HBox hb = new HBox(15), hb2 = new HBox(15);
                hb.setPadding(new Insets(0, 0, 10, 15));

                Label label = new Label(heads[j]);
                Label val_label = new Label("" + summary.get(heads[j]));

                hb.getChildren().add(label);
                hb.getChildren().add(val_label);
                vb.getChildren().add(hb);

            }

            panes[i].setContent(vb);

            attrDescAccordion.getPanes().add(panes[i]);

        }

    }

    @FXML
    void discretizeAttribute(ActionEvent event) {
        TitledPane target = this.attrDescAccordion.getExpandedPane();
        if (RECOVER == null) {
            RECOVER = CURRENT_DATASET;
        }
        int index = CURRENT_DATASET.getAttributsName().indexOf(target.getId());
        int methode = 1;
        if (this.equi_width_rb.isSelected()) {
            methode = 1;
        }
        if (this.equi_depth_rb.isSelected()) {
            methode = 2;
        }
        if (this.mean_rb.isSelected()) {
            methode = 3;
        }
        
        CURRENT_DATASET = CURRENT_DATASET.descriticize(index, (int) Math.ceil(this.intervalNumberSlider.getValue()), methode);

        TitledPane pane = new TitledPane();
        pane.setText(target.getId());
        pane.setId(target.getId());
        pane.setStyle("-fx-text-fill: #4b7ff8;");
        pane.applyCss();
        pane.getStyleClass().add(".titled-pane");
        VBox vb = new VBox();

        ArrayList<HashMap<String, String>> rangeslist = CURRENT_DATASET.getDiscritizeTable().get(index);
        rangeslist.forEach((t) -> {
            HBox hb = new HBox(15), hb2 = new HBox(15);
            hb.setPadding(new Insets(0, 0, 10, 15));
            Label label = new Label(t.get("Range"));
            label.setWrapText(true);
            Label val_label = new Label(t.get("Name"));
            val_label.setWrapText(true);

            hb.getChildren().add(label);
            hb.getChildren().add(val_label);
            vb.getChildren().add(hb);
        });

        pane.setContent(vb);
        resultsAccordion.getPanes().add(pane);

    }

    @FXML
    void reduceDataSet(ActionEvent event) {
        this.success_text.setVisible(false);
        if(DataSetWindoController.GENERALIZED_DATASET==null)
            DataSetWindoController.GENERALIZED_DATASET = CURRENT_DATASET.transform();

        ArrayList<Integer> attrIndexes = new ArrayList<>();
        attrIndexes.add(CURRENT_DATASET.getAttributsName().indexOf("serum_cholestoral"));
        attrIndexes.add(CURRENT_DATASET.getAttributsName().indexOf("maximum_heart_rate_achieved"));
        attrIndexes.add(CURRENT_DATASET.getAttributsName().indexOf("age"));
        attrIndexes.add(CURRENT_DATASET.getAttributsName().indexOf("resting_blood_pressure"));

        generalDataSet reducedDataSet = new generalDataSet();
        reducedDataSet.setDescription(DataSetWindoController.GENERALIZED_DATASET.getDescription());
        for (int i = 0; i < attrIndexes.size(); i++) {
            reducedDataSet.getAttributsCategory().add(i, CURRENT_DATASET.getAttributsCategory().get(attrIndexes.get(i)));
            reducedDataSet.getAttributsName().add(i, CURRENT_DATASET.getAttributsName().get(attrIndexes.get(i)));
            reducedDataSet.getAttributsType().add(i, CURRENT_DATASET.getAttributsType().get(attrIndexes.get(i)));
        }

        for (generalRow row : DataSetWindoController.GENERALIZED_DATASET.getInstances()) {
            generalRow newrow = new generalRow();
            for (int i = 0; i < attrIndexes.size(); i++) {
                newrow.getValues().add(i, row.getValues().get(attrIndexes.get(i)));

            }
            reducedDataSet.getInstances().add(newrow);

        }
        this.success_text.setVisible(true);
        Platform.runLater(() -> {
            new BounceInLeft(this.success_text).play();
        });

        System.out.println("REDUCTION : New Instances number :"+reducedDataSet.getInstances().size()+"  New Columns number :"+reducedDataSet.getAttributsName().size());
        
        DataSetWindoController.GENERALIZED_DATASET=reducedDataSet;
    }

    @FXML
    void confirmchanges(ActionEvent event) {
        

        DataSetWindoController.CURRENT_DATASET = CURRENT_DATASET;
        if(DataSetWindoController.GENERALIZED_DATASET.getAttributsName().size()==CURRENT_DATASET.getAttributsName().size())
            DataSetWindoController.GENERALIZED_DATASET = CURRENT_DATASET.transform();
        
        DataMiningProject.saveObject(DataSetWindoController.GENERALIZED_DATASET,"descritizedDataset");
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        stage.close();
    }

    @FXML
    void reset(ActionEvent event) {
        CURRENT_DATASET = RECOVER;

        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        stage.close();

    }

}
