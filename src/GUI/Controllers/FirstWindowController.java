
package GUI.Controllers;

import animatefx.animation.BounceInLeft;
import animatefx.animation.BounceInRight;
import animatefx.animation.BounceInUp;
import animatefx.animation.FadeIn;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Goldenfay
 */
public class FirstWindowController implements Initializable {

    @FXML
    private MediaView intromediaview;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Media media = new Media(new File("assets\\introv2.mp4").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnEndOfMedia(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/Views/DataSetWindo.fxml"));
                Parent fxml = (Parent) loader.load();

                Scene scene = new Scene(fxml);

                Stage stg = new Stage();
                stg.setScene(scene);
                stg.initStyle(StageStyle.UNDECORATED);
                stg.show();
                
                DataSetWindoController controller=(DataSetWindoController)loader.getController();
                
                        // Animate
                FadeIn fiAnim=new FadeIn(controller.getStartLabel());
                fiAnim.setSpeed(0.2);
                BounceInLeft bil=new BounceInLeft(controller.getFilePathInput());
                BounceInUp biu=new BounceInUp(controller.getUploadFileBtn());
                BounceInRight bir=new BounceInRight(controller.getLoadDatasetBtn());
                controller.reEstablish();
                Platform.runLater(() -> {
                    fiAnim.play();
                });
                Platform.runLater(() -> {
                    bil.play();
                });
                Platform.runLater(() -> {
                    biu.play();
                });
                Platform.runLater(() -> {
                    bir.play();
                });
                
                
                DataSetWindoController.stage=stg;
                
                Stage stage = (Stage) intromediaview.getScene().getWindow();
   
                stage.close();
            } catch (Exception e) {

            }
        });
        intromediaview.setMediaPlayer(mediaPlayer);

        mediaPlayer.setAutoPlay(true);

    }

}
