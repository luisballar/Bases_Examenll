package cr.ac.ucr.paraiso.ie.bases_examenll;

import data.MongoOperations;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindow implements Initializable {

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
    private ChoiceBox<String> genreBox;

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
    private Stage actual;
    private String collectionName = "Song";
    private MongoOperations  op = new MongoOperations(collectionName);

    private ArtistWindow artistWindow;
    private AlbumWindow albumWindow;
    private MainWindow mainWindow;
    MethodsInit methodsInit;

    public MainWindow() {
    }


    @FXML
    void filterBox_action(ActionEvent event) {

    }

    @FXML
    void addButton_clcked(ActionEvent event) {


        Document document = new Document("_id",op.asignaID())
                .append("title", nameField.getText())
                .append("genre",genreBox.getValue())
                .append("album", albumBox.getValue())
                .append("artist", artisteBox.getValue())
                .append("logic_delete", 0);


        op.insertDocument(document);

        nameField.clear();
        genreBox.getItems().clear();



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

        MethodsInit.getInstance().showWindow(loader,
                albumWindow,
                scene,
                nuevoStage,
                actual,
                exitBut,
                "albumWindow.fxml");
    }

    // abrir stage Artist
    @FXML
    void viewArtistBut_clicked(ActionEvent event) throws IOException {

        MethodsInit.getInstance().showWindow(loader,
                artistWindow,
                scene,
                nuevoStage,
                actual,
                exitBut,
                "artistWindow.fxml");
    }


    @FXML
    void viewSongsBut_clicked(ActionEvent event) {

    }

    private boolean isActive(Stage stage){
        if (stage == null) {
            return false;
        } else {
            return true;
        }
    }


    // inicializar los choiceBox
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loader = new FXMLLoader(MainWindow.class.getResource("mainWindow.fxml"));
        scene = loader.getController();

        MethodsInit.getInstance().setGenres(genreBox);

        MethodsInit.getInstance().disable(viewSongsBut); // disable viewSongsBut

    }
}
