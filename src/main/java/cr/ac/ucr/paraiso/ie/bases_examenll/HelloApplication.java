package cr.ac.ucr.paraiso.ie.bases_examenll;

import data.InsertData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("mainWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Disquera");
        stage.setScene(scene);
        MainWindow mainWindow = fxmlLoader.getController();
        //mainWindow.setImages(scene);
        stage.setResizable(false);
        stage.show();



    }

    public static void main(String[] args) {
        launch();
    }
}