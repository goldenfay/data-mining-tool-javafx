package dataminingproject;

import GUI.Controllers.PrevisualizeWindowController;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Goldenfay
 */
public class DataMiningProject extends Application {

    public static String DATASETFILEPATH = "C:\\Users\\PC\\Documents\\NetBeansProjects\\DataMiningProject\\assets\\HEART_Stat.txt";

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("/GUI/Views/FirstWindow.fxml"));

        Scene scene = new Scene(fxml);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.setHeight(scene.getHeight());
        PrevisualizeWindowController.stage = primaryStage;
        primaryStage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public static void writeObject(String jsonString, String algoname) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(new File("assets/outputs/" + algoname + ".txt"));
            os.write(jsonString.getBytes(), 0, jsonString.length());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveObject(Object obj, String filename) {
        try {
            FileOutputStream f = new FileOutputStream(new File("assets/outputs/" + filename +".obj"));
            ObjectOutputStream o = new ObjectOutputStream(f);

            o.writeObject(obj);

            o.close();
            f.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }
    }

    public static Object loadObject(String filename) throws FileNotFoundException, IOException, ClassNotFoundException {

        FileInputStream fi = new FileInputStream(new File("assets/outputs/" + filename +".obj"));
        ObjectInputStream oi = new ObjectInputStream(fi);

        Object obj=oi.readObject();

        oi.close();
        fi.close();
        return obj;

    }

    public static void writeCsv(String filename, HashMap<String, HashMap<String, Object>> results, ArrayList<String> Headers) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(new File("assets/outputs/" + filename + ".csv"));
        StringBuilder sBuilder = new StringBuilder();

        sBuilder.append(String.join(",", Headers) + "\n");
        //sBuilder.append("Minimum Support,Average Execution Time,Maximum Frequent ItemSets Level,Average Confidence \n");
        writer.append(sBuilder.toString() + "\n");
        writer.flush();

        results.forEach((t, u) -> {
            StringBuilder row = new StringBuilder();
            for (int i = 0; i < Headers.size(); i++) {
                row.append(u.get(Headers.get(i)) + (i == Headers.size() - 1 ? "" : ","));

            }
            row.append("\n");
            writer.append(row.toString());
            writer.flush();

        });

    }

}
