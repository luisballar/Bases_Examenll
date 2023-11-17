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
import javafx.scene.input.MouseEvent;
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
    private Artist selecteArtist;

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
        if(selecteArtist != null) {
            //valida si se trata de borrado logico o fisico
            if (logicDelete.isSelected()) {
                op.logicDelete(searchField.getText());
            } else {
                op.deleteDocuments("_id", selecteArtist.getArtistID());
            }

            op.fetchAndDisplayDataArtist(tableView); // carga el tableView con los datos
            configureTable();

        }else {
            alertMessage = new Alert(Alert.AlertType.ERROR);
            alertMessage.setTitle("Error al Eliminar");
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Seleccione un Artista a eliminar");
            alertMessage.show();
        }
    }

    @FXML
    void exitBut_clicked(ActionEvent event) {
        Platform.exit();

    }

    @FXML
    public Object filterBox_action(ActionEvent event) {
        return filterBox.getValue();
    }

    @FXML
    void logicDelete_clicked(ActionEvent event) {

    }

    @FXML
    void mask_button_clicked(ActionEvent event) {
        if(mask_button.getText().equals("Enmascarar")){
            mask_button.setText("Desenmascarar");

            op.maskArtist(tableView); // metodo que enmascara el year
            configureTable(); // enmascara

        }else if(mask_button.getText().equals("Desenmascarar")){
            mask_button.setText("Enmascarar");
            op.fetchAndDisplayDataAlbum(tableView); // carga el tableView con los datos
            configureTable();

        }
    }

    @FXML
    void searchButton_clicked(ActionEvent event) {
        String option = (String) filterBox_action(event);
        System.out.println(option);

        if(!searchField.getText().isEmpty() && option != null) {

            switch (option) {
                case "ID":
                    if (op.exists(searchField.getText()) != null) {

                        op.fetchAndDisplayDataOneArtist(tableView, op.exists(searchField.getText())); // mostrar el solicitado
                        configureTable();

                        break;
                    } else {
                        alertMessage = new Alert(Alert.AlertType.ERROR);
                        alertMessage.setTitle("Error de Búsqueda");
                        alertMessage.setHeaderText(null);
                        alertMessage.setContentText("No se encontró el ID solicitado");
                        alertMessage.show();
                        break;
                    }
                case "Nombre":
                    if (op.existsForArtistName(searchField.getText()) != null) {

                        op.showArtistName(tableView, searchField.getText()); // mostrar el solicitado
                        configureTable();

                        break;
                    } else {
                        alertMessage = new Alert(Alert.AlertType.ERROR);
                        alertMessage.setTitle("Error de Búsqueda");
                        alertMessage.setHeaderText(null);
                        alertMessage.setContentText("No se encontró el Nombre solicitado");
                        alertMessage.show();
                        break;
                    }
                case "Genero":
                    if (op.existsForGenre(searchField.getText()) != null) {

                        op.showArtistsGenre(tableView, searchField.getText()); // mostrar el solicitado
                        configureTable();

                        break;
                    } else {
                        alertMessage = new Alert(Alert.AlertType.ERROR);
                        alertMessage.setTitle("Error de Búsqueda");
                        alertMessage.setHeaderText(null);
                        alertMessage.setContentText("No se encontró el Genero solicitado");
                        alertMessage.show();
                        break;
                    }

                case "Nacionalidad":
                    if (op.existsForNationality(searchField.getText()) != null) {

                        op.showArtistNationality(tableView, searchField.getText()); // mostrar el solicitado
                        configureTable();

                        break;
                    } else {
                        alertMessage = new Alert(Alert.AlertType.ERROR);
                        alertMessage.setTitle("Error de Búsqueda");
                        alertMessage.setHeaderText(null);
                        alertMessage.setContentText("No se encontró el Año solicitado");
                        alertMessage.show();
                        break;
                    }


            }
        }else {
            op.fetchAndDisplayDataArtist(tableView); // carga el tableView con los datos de la colect
            configureTable();
        }
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


    @FXML
    void MouseClicked(MouseEvent event) {

        selecteArtist = tableView.getSelectionModel().getSelectedItem();
        nameField.setText(selecteArtist.getName());

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
                "Genero"
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

        nameField.setTextFormatter(new TextFormatter<>(MethodsInit.getInstance().validateBlankSpaces())); // no permite espacios en blanco
        searchField.setTextFormatter(new TextFormatter<>(MethodsInit.getInstance().validateBlankSpaces())); // no permite espacios en blanco


        op.fetchAndDisplayDataArtist(tableView);
        configureTable();
    }
}
