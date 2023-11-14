package cr.ac.ucr.paraiso.ie.bases_examenll;

import data.MongoOperations;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ArtistWindow implements Initializable {

    @FXML
    private Button addButton;

    @FXML
    private TableColumn<?, ?> artist;

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
    private TextField lastNameField;

    @FXML
    private CheckBox logicDelete;

    @FXML
    private Button mask_button;

    @FXML
    private TextField nameField;

    @FXML
    private TextField nationalField;

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
    private MongoOperations insert;
    private String stringConnection = "mongodb+srv://luisballar:C20937@if4100.kles8ol.mongodb.net/?retryWrites=true&w=majority";
    private String dataBase = "luisballar";
    private String collectionName = "Artist";
    private MainWindow mainWindow;
    private AlbumWindow albumWindow;

    public ArtistWindow() {
    }

    @FXML
    void addButton_clcked(ActionEvent event) {
        insert = new MongoOperations(stringConnection, dataBase, collectionName);


        Document document = new Document("_id",new ObjectId())
                .append("artist_name", nameField.getText())
                .append("artist_lastName",lastNameField.getText())
                .append("nationality", nationalField.getText())
                .append("gender", genreBox.getValue())
                .append("logic_delete", 0);

        insert.insertDocument(document);

        nameField.clear();
        lastNameField.clear();
        nationalField.clear();
        genreBox.getItems().clear();

    }

    @FXML
    void deleteButton_clicked(ActionEvent event) {

    }

    @FXML
    void exitBut_clicked(ActionEvent event) {
        Platform.exit();

    }

    @FXML
    void filterBox_action(ActionEvent event) {

    }

    @FXML
    void logicDelete_clicked(ActionEvent event) {

    }

    @FXML
    void mask_button_clicked(ActionEvent event) {

    }

    @FXML
    void searchButton_clicked(ActionEvent event) {

    }

    @FXML
    void updateButton_clicked(ActionEvent event) {

    }

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

    @FXML
    void viewArtistBut_clicked(ActionEvent event) {

    }

    @FXML
    void viewSongsBut_clicked(ActionEvent event) throws IOException {

        MethodsInit.getInstance().showWindow(loader,
                mainWindow,
                scene,
                nuevoStage,
                actual,
                exitBut,
                "mainWindow.fxml");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MethodsInit.getInstance().setSex(genreBox); // set genres on genreBox
        MethodsInit.getInstance().disable(viewArtistBut); // disable viewArtistBut

    }
}
