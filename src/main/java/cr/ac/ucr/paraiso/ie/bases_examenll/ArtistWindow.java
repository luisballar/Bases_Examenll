package cr.ac.ucr.paraiso.ie.bases_examenll;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import data.MongoOperations;
import domain.Album;
import domain.Artist;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
                    .append("logic_delete", 0);

            op.insertDocument(document);

            nameField.clear();
            op.fetchAndDisplayDataArtist(tableView); // carga el tableView con los datos
            configureTable();
            genreBox.setValue(null);
            nationalityBox.setValue(null);
            addButton.setDisable(false);


        }else{
            showAlert("Error al Ingresar", "Debe ingresar todos los datos");
        }
    }

    @FXML
    void deleteButton_clicked(ActionEvent event) {
        if(selecteArtist != null) {
            //valida si se trata de borrado logico o fisico
            if (logicDelete.isSelected()) {
                op.logicDelete(selecteArtist.getArtistID());
            } else {
                op.deleteDocuments(selecteArtist.getArtistID());
            }

            nameField.clear();
            nationalityBox.setValue(null);
            genreBox.setValue(null);
            logicDelete.setSelected(false);
            addButton.setDisable(false);


            op.fetchAndDisplayDataArtist(tableView); // carga el tableView con los datos
            configureTable();

        }else {

            showAlert("Error al Eliminar", "Seleccione un Artista a eliminar");

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
            op.fetchAndDisplayDataArtist(tableView); // carga el tableView con los datos
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
                    if (op.exists(Integer.parseInt(searchField.getText().trim())) != null) {

                        op.fetchAndDisplayDataOneArtist(tableView, op.exists(Integer.parseInt(searchField.getText().trim()))); // mostrar el solicitado
                        configureTable();

                        break;
                    } else {
                        showAlert("Error de Búsqueda","No se encontró el ID solicitado" );
                        break;
                    }
                case "Nombre":
                    if (op.existsForArtistName(searchField.getText().trim()) != null) {

                        op.showArtistName(tableView, searchField.getText().trim()); // mostrar el solicitado
                        configureTable();

                        break;
                    } else {
                        showAlert("Error de Búsqueda","No se encontró el Nombre solicitado" );
                        break;
                    }
                case "Genero":
                    if (op.existsForGenre(searchField.getText().trim()) != null) {

                        op.showArtistsGenre(tableView, searchField.getText().trim()); // mostrar el solicitado
                        configureTable();

                        break;
                    } else {
                        showAlert("Error de Búsqueda","No se encontró el Género solicitado" );
                        break;
                    }

                case "Nacionalidad":
                    if (op.existsForNationality(searchField.getText().trim()) != null) {

                        op.showArtistNationality(tableView, searchField.getText().trim()); // mostrar el solicitado
                        configureTable();

                        break;
                    } else {
                        showAlert("Error de Búsqueda","No se encontró el Año solicitado" );
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

        if(selecteArtist == null) {

            showAlert("Error al Actualizar","Debe seleccionar un Artista");


        }else if(nameField.getText().isEmpty()) {

            showAlert("Error al Actualizar","No deben haber espacios en blanco");

        }else if(selecteArtist != null){
            if (op.exists(selecteArtist.getArtistID()) != null) {

                op.updateArtist(selecteArtist.getArtistID(), nameField.getText(), nationalityBox.getValue(), genreBox.getValue());


            } else {

                showAlert("Error al Actualizar","No existe ese ID");

            }

            nameField.clear();
            nationalityBox.setValue(null);
            genreBox.setValue(null);
            addButton.setDisable(false);


            op.fetchAndDisplayDataArtist(tableView);
            configureTable();
        }
    }

    @FXML
    void setMouseClicked(MouseEvent event) {
        nameField.clear();
        genreBox.setValue(null);
        nationalityBox.setValue(null);
        tableView.getSelectionModel().clearSelection();
        addButton.setDisable(false);

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

    // filterChoiceBox
    @FXML
    void onMouseClicked(MouseEvent event) {
        filterBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    op.fetchAndDisplayDataArtist(tableView);
                    configureTable();

                    // Acción a realizar cuando cambia la selección
                    if (filterBox.getValue().equals("ID")) {
                        searchField.setTextFormatter(MethodsInit.getInstance().onlyNumber()); // solo admite numeros
                    } else {
                        searchField.setTextFormatter(new TextFormatter<>(MethodsInit.getInstance().validateBlankSpaces())); // solo admite numeros
                    }
                    searchField.clear();
                }
            });

    }

    // ver que se clickea en el tableView
    @FXML
    void MouseClicked(MouseEvent event) {

        selecteArtist = tableView.getSelectionModel().getSelectedItem();
        if(selecteArtist != null){
            nameField.setText(selecteArtist.getName());
            nationalityBox.getSelectionModel().select(selecteArtist.getNationality());
            genreBox.getSelectionModel().select(selecteArtist.getGenre());
            addButton.setDisable(true);
        }


    }

    // cofigurar las columnas del tableView
    private void configureTable(){
        idColumn.setCellValueFactory((TableColumn.CellDataFeatures<Artist, String> data) ->
                new SimpleStringProperty(Integer.toString(data.getValue().artistIDProperty().get())));
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


    public void setNationality(){
        String[] nationalities = {
                "Africano",
                "Albanés",
                "Alemán",
                "Antártico",
                "Armenio",
                "Asiático",
                "Australiano",
                "Austríaco",
                "Azerí",
                "Belga",
                "Bielorruso",
                "Bosnio",
                "Brasileño",
                "Británico",
                "Búlgaro",
                "Canadiense",
                "Caribeño",
                "Centroamericano",
                "Checo",
                "Chileno",
                "Chino",
                "Colombiano",
                "Costarricense",
                "Croata",
                "Danés",
                "Egipcio",
                "Eslovaco",
                "Esloveno",
                "Español",
                "Estadounidense",
                "Estonio",
                "Europeo",
                "Finlandés",
                "Francés",
                "Georgiano",
                "Griego",
                "Holandés",
                "Húngaro",
                "Indio",
                "Indonesio",
                "Iraní",
                "Irlandés",
                "Italiano",
                "Japonés",
                "Kazajo",
                "Keniano",
                "Kirguís",
                "Kosovar",
                "Letón",
                "Lituano",
                "Luxemburgués",
                "Macedonio",
                "Marroquí",
                "Mexicano",
                "Mongol",
                "Montenegrino",
                "Neozelandés",
                "Nigeriano",
                "Noruego",
                "Oceaniano",
                "Peruano",
                "Polaco",
                "Portugués",
                "Rumano",
                "Ruso",
                "Saudita",
                "Serbio",
                "Sudafricano",
                "Sueco",
                "Sudcoreano",
                "Suizo",
                "Surcoreano",
                "Tayiko",
                "Turco",
                "Turcomano",
                "Ucraniano",
                "Uzbeko",
                "Venezolano"
        };

        nationalityBox.getItems().addAll(nationalities);

    }

    // Método para mostrar una alerta
    private void showAlert(String title, String contentText) {
        alertMessage = new Alert(Alert.AlertType.ERROR);
        alertMessage.setTitle(title);
        alertMessage.setHeaderText(null);
        alertMessage.setContentText(contentText);
        alertMessage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MethodsInit.getInstance().setGenres(genreBox); // set genres on genreBox
        MethodsInit.getInstance().disable(viewArtistBut); // disable viewArtistBut
        setNationality(); // set nationalities on genreBox
        setFiltersAlbum(); // set filters on filterBox

        nameField.setTextFormatter(new TextFormatter<>(MethodsInit.getInstance().validateBlankSpaces())); // no permite espacios en blanco
        searchField.setTextFormatter(MethodsInit.getInstance().onlyNumber()); // solo admite numeros

        op.fetchAndDisplayDataArtist(tableView);
        configureTable();
    }
}
