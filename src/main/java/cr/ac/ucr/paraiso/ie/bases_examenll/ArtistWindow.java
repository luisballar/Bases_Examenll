package cr.ac.ucr.paraiso.ie.bases_examenll;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import data.MongoOperations;
import domain.Album;
import domain.Artist;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class ArtistWindow implements Initializable {

    @FXML
    private TableColumn<Artist, String> idColumn;

    @FXML
    private TableColumn<Artist, String> nameColumn;


    @FXML
    private TableColumn<Artist, String> nationalityColumn;

    @FXML
    private TableColumn<Artist, String> genreColumn;

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
    private ComboBox<String> filterBox;


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
    private TableView<Artist> tableView;

    @FXML
    private ChoiceBox<String> nationalityBox;

    @FXML
    private ChoiceBox<String> genreBox;

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
    private String collectionName = "Artist";
    private MongoOperations op = new MongoOperations(collectionName);
    private MainWindow mainWindow;
    private AlbumWindow albumWindow;
    private Alert alertMessage;
    private Album selectedAlbum;

    public ArtistWindow() {
    }

    @FXML
    void addButton_clcked(ActionEvent event) {

        if(nationalityBox.getValue() != null && genreBox.getValue() != null && !nameField.getText().isEmpty()){

            Document document = new Document("_id", op.asignaID())
                    .append("name", nameField.getText().trim())
                    .append("nationality", nationalityBox.getValue().trim())
                    .append("genre", genreBox.getValue().trim())
                    .append("logic_delete", "0");

            op.insertDocument(document);

            nameField.clear();
            op.fetchAndDisplayDataArtist(tableView); // carga el tableView con los datos
            configureTable();
            genreBox.setValue(null);
            nationalityBox.setValue(null);

        }else{

            alertMessage = new Alert(Alert.AlertType.ERROR);
            alertMessage.setTitle("Error al Ingresar");
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Debe ingresar todos los datos");
            alertMessage.show();

        }

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



    public void setNationality(){

        String[] nationalities = {
                "Estadounidense",
                "Canadiense",
                "Británico",
                "Alemán",
                "Francés",
                "Italiano",
                "Chino",
                "Japonés",
                "Brasileño",
                "Argentino",
                "Mexicano",
                "Indio",
                "Australiano",
                "Sudafricano",
                "Nigeriano",
                "Egipcio",
                "Ruso",
                "Turco",
                "Iraní",
                "Saudita",
                "Surcoreano",
                "Indonesio",
                "Neozelandés",
                "Marroquí",
                "Keniano",
                "Chileno",
                "Peruano",
                "Colombiano",
                "Venezolano",
                "Español",
                "Portugués",
                "Griego",
                "Noruego",
                "Sueco",
                "Finlandés",
                "Danés",
                "Suizo",
                "Austríaco",
                "Holandés",
                "Belga",
                "Luxemburgués",
                "Irlandés",
                "Polaco",
                "Húngaro",
                "Checo",
                "Eslovaco",
                "Rumano",
                "Búlgaro",
                "Croata",
                "Serbio",
                "Montenegrino",
                "Bosnio",
                "Macedonio",
                "Albanés",
                "Kosovar",
                "Esloveno",
                "Estonio",
                "Letón",
                "Lituano",
                "Bielorruso",
                "Ucraniano",
                "Georgiano",
                "Armenio",
                "Azerí",
                "Kazajo",
                "Uzbeko",
                "Turcomano",
                "Kirguís",
                "Tayiko",
                "Mongol",
                "Norteamericano",
                "Sudamericano",
                "Europeo",
                "Asiático",
                "Africano",
                "Oceaniano",
                "Caribeño",
                "Centroamericano",
                "Sudamericano",
                "Norteamericano",
                "Antártico"
        };

        nationalityBox.getItems().addAll(nationalities);

    }


    private void configureTable(){
        idColumn.setCellValueFactory((TableColumn.CellDataFeatures<Artist, String> data) -> data.getValue().artistIDProperty());
        nameColumn.setCellValueFactory((TableColumn.CellDataFeatures<Artist, String> data) -> data.getValue().nameProperty());
        nationalityColumn.setCellValueFactory((TableColumn.CellDataFeatures<Artist, String> data) -> data.getValue().nationalityProperty());
        genreColumn.setCellValueFactory((TableColumn.CellDataFeatures<Artist, String> data) -> data.getValue().genreProperty());

    }


    public void setFiltersAlbum(){
        String[] filters = {
                "ID",
                "Nombre",
                "Nacionalidad",
                "Género"
        };

        filterBox.getItems().addAll(filters);
        filterBox.getSelectionModel().selectFirst();

    }














    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MethodsInit.getInstance().setGenres(genreBox); // set genres on genreBox
        MethodsInit.getInstance().disable(viewArtistBut); // disable viewArtistBut
        setNationality(); // set nationalities on genreBox
        setFiltersAlbum(); // set filters on filterBox


    }
}
