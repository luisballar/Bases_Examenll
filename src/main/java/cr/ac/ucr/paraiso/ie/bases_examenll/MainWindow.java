package cr.ac.ucr.paraiso.ie.bases_examenll;

import data.InsertData;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;

public class MainWindow {
    public MainWindow() {
    }

    @FXML
    private Button addButton;

    @FXML
    private ChoiceBox<?> albumBox;

    @FXML
    private TableColumn<?, ?> artist;

    @FXML
    private ChoiceBox<?> artisteBox;

    @FXML
    private Button deleteButton;

    @FXML
    private Button exitBut;

    @FXML
    private AnchorPane field;

    @FXML
    private ComboBox<?> filterBox;

    @FXML
    private TextField genreField;

    @FXML
    private CheckBox logicDelete;

    @FXML
    private Button mask_button;

    @FXML
    private TextField nameField;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<?> tableView;

    @FXML
    private Button updateButton;

    @FXML
    private Button viewAlbumBut;

    @FXML
    private Button viewArtistBut;

    @FXML
    private Button viewColectBut;

    @FXML
    private Button viewSongsBut;

    private FXMLLoader loader;
    private Scene scene;
    private Stage nuevoStage;
    private InsertData insert;
    private String stringConnection = "mongodb+srv://luisballar:C20937@if4100.kles8ol.mongodb.net/?retryWrites=true&w=majority";
    private String dataBase = "C20937";
    private String collectionName = "Song";

    @FXML
    void filterBox_action(ActionEvent event) {

    }

    @FXML
    void addButton_clcked(ActionEvent event) {
        Document document = new Document("_id",new ObjectId())
                .append("title", nameField.getText())
                .append("genre",genreField.getText())
                .append("album", albumBox.getValue())
                .append("artist", artisteBox.getValue());



        insert = new InsertData(stringConnection, dataBase, collectionName);
        insert.insertDocument(document);


    }

    @FXML
    void deleteButton_clicked(ActionEvent event) {

    }

    // close all stages
    @FXML
    void exitBut_clicked(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void logicDelete_clicked(ActionEvent event) {

    }

    @FXML
    void mask_button_clicked(ActionEvent event) throws IOException {

    }

    @FXML
    void searchButton_clicked(ActionEvent event) {

    }

    @FXML
    void updateButton_clicked(ActionEvent event) {

    }

    // abrir stage Album
    @FXML
    void viewAlbumBut_clicked(ActionEvent event) throws IOException {
        loader = new FXMLLoader(getClass().getResource("albumWindow.fxml"));

        scene = new Scene(loader.load());
        nuevoStage = new Stage();
        nuevoStage.setScene(scene);
        nuevoStage.show();
        nuevoStage.setResizable(false);

    }

    // abrir stage Artist
    @FXML
    void viewArtistBut_clicked(ActionEvent event) throws IOException {
        loader = new FXMLLoader(getClass().getResource("artistWindow.fxml"));

        scene = new Scene(loader.load());
        nuevoStage = new Stage();
        nuevoStage.setScene(scene);
        nuevoStage.show();
        nuevoStage.setResizable(false);
    }

    @FXML
    void viewColectBut_clicked(ActionEvent event) {

    }

    @FXML
    void viewSongsBut_clicked(ActionEvent event) {

    }

    // setear imagenes en los botones
    public void setImages(Scene actual){
        actual.getStylesheets().add(getClass().getResource("/butStyle.css").toExternalForm());
    }
    private boolean isActive(Stage stage){
        if (stage == null) {
            return false;
        } else {
            return true;
        }
    }


}
