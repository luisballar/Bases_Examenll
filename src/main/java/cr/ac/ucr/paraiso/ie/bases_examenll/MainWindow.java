package cr.ac.ucr.paraiso.ie.bases_examenll;

import data.MongoOperations;
import domain.Album;
import domain.Artist;
import domain.Song;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindow implements Initializable {

    @FXML
    private Button addButton;

    @FXML
    private ChoiceBox<String> albumBox;

    @FXML
    private TableColumn<Song, String> albumColumn;

    @FXML
    private TableColumn<Song, String> artistColumn;

    @FXML
    private ChoiceBox<String> artisteBox;

    @FXML
    private Button deleteButton;

    @FXML
    private Button exitBut;

    @FXML
    private AnchorPane field;

    @FXML
    private ComboBox<String> filterBox;

    @FXML
    private ChoiceBox<String> genreBox;

    @FXML
    private TableColumn<Song, String> genreColumn;

    @FXML
    private TableColumn<Song, String> idColumn;

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
    private TableView<Song> tableView;

    @FXML
    private TableColumn<Song, String> titleColumn;

    @FXML
    private Button updateButton;

    @FXML
    private Button viewAlbumBut;

    @FXML
    private Button viewArtistBut;

    @FXML
    private Button viewSongsBut;
    private FXMLLoader loader;
    private Scene scene;
    private Stage nuevoStage;
    private Stage actual;
    private String collectionName = "Song";
    private MongoOperations op = new MongoOperations(collectionName);
    private MainWindow mainWindow;
    private ArtistWindow artistWindow;
    private AlbumWindow albumWindow;
    private Alert alertMessage;
    private Song selectedSong;


    @FXML
    void addButton_clcked(ActionEvent event) {

        // validar si todos los campos estan lleno
        if(genreBox.getValue() != null && artisteBox.getValue() != null && albumBox.getValue() != null && !nameField.getText().isEmpty()){

            Document document = new Document("_id", op.asignaID())
                    .append("title", nameField.getText().trim())
                    .append("genre", genreBox.getValue().trim())
                    .append("album", albumBox.getValue().trim())
                    .append("artist", artisteBox.getValue().trim())
                    .append("logic_delete", 0);

            op.insertDocument(document);

            nameField.clear();
            genreBox.setValue(null);
            albumBox.setValue(null);
            artisteBox.setValue(null);
            logicDelete.setSelected(false);

            op.fetchAndDisplayDataSong(tableView); // carga el tableView con los datos
            configureTable();

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
        if(selectedSong != null) {
            //valida si se trata de borrado logico o fisico
            if (logicDelete.isSelected()) {
                op.logicDelete(selectedSong.getSongID());
            } else {
                op.deleteDocuments(selectedSong.getSongID());
            }

            nameField.clear();
            genreBox.setValue(null);
            albumBox.setValue(null);
            artisteBox.setValue(null);
            logicDelete.setSelected(false);


            op.fetchAndDisplayDataSong(tableView); // carga el tableView con los datos
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

    @FXML
    void searchButton_clicked(ActionEvent event) {

    }

    @FXML
    void updateButton_clicked(ActionEvent event) {
        if(selectedSong != null) {
            if (op.exists(selectedSong.getSongID()) != null) {

                op.updateSong(selectedSong.getSongID(), nameField.getText(), genreBox.getValue(), albumBox.getValue(), artisteBox.getValue());


            } else {
                alertMessage = new Alert(Alert.AlertType.ERROR);
                alertMessage.setTitle("Error al Eliminar");
                alertMessage.setHeaderText(null);
                alertMessage.setContentText("No existe ese ID");
                alertMessage.show();
            }


            nameField.clear();
            genreBox.setValue(null);
            albumBox.setValue(null);
            artisteBox.setValue(null);
            logicDelete.setSelected(false);

            op.fetchAndDisplayDataSong(tableView);
            configureTable();

        }else{
            alertMessage = new Alert(Alert.AlertType.ERROR);
            alertMessage.setTitle("Error al Actualizar");
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Debe seleccionar una Canción");
            alertMessage.show();
        }
    }

    // abrir stage Album
    @FXML
    void viewAlbumBut_clicked(ActionEvent event) throws IOException, IOException {

        MethodsInit.getInstance().showWindow(loader,
                albumWindow,
                scene,
                nuevoStage,
                actual,
                exitBut,
                "albumWindow.fxml");
    }

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

    // ver que se clickea en el tableView
    @FXML
    void MouseClicked(MouseEvent event) {

        selectedSong = tableView.getSelectionModel().getSelectedItem();
        nameField.setText(selectedSong.getTitle());
        genreBox.getSelectionModel().select(selectedSong.getGenre());
        albumBox.getSelectionModel().select(selectedSong.getAlbum());
        artisteBox.getSelectionModel().select(selectedSong.getArtist());

    }

    private void configureTable(){
        idColumn.setCellValueFactory((TableColumn.CellDataFeatures<Song, String> data) ->
                new SimpleStringProperty(Integer.toString(data.getValue().songIDProperty().get())));
        titleColumn.setCellValueFactory((TableColumn.CellDataFeatures<Song, String> data) -> data.getValue().titleProperty());
        genreColumn.setCellValueFactory((TableColumn.CellDataFeatures<Song, String> data) -> data.getValue().genreProperty());
        albumColumn.setCellValueFactory((TableColumn.CellDataFeatures<Song, String> data) -> data.getValue().albumProperty());
        artistColumn.setCellValueFactory((TableColumn.CellDataFeatures<Song, String> data) -> data.getValue().artistProperty());

    }

    public void setFiltersAlbum(){
        String[] filters = {
                "ID",
                "Titulo",
                "Genero",
                "Album"
                ,"Artista"
        };

        filterBox.getItems().addAll(filters);
        filterBox.getSelectionModel().selectFirst();

    }

    // inicializar los choiceBox
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loader = new FXMLLoader(MainWindow.class.getResource("mainWindow.fxml"));
        scene = loader.getController();
        MethodsInit.getInstance().disable(viewSongsBut); // disable viewSongsBut
        MethodsInit.getInstance().setGenres(genreBox);

        nameField.setTextFormatter(new TextFormatter<>(MethodsInit.getInstance().validateBlankSpaces())); // no permite espacios en blanco
        searchField.setTextFormatter(MethodsInit.getInstance().onlyNumber()); // solo admite numeros


        artisteBox.getItems().addAll(op.loadArtistsIntoChoiceBox()); // cargar artistas
        albumBox.getItems().addAll(op.loadAlbumsIntoChoiceBox()); // cargar albums
        setFiltersAlbum();

        op.fetchAndDisplayDataSong(tableView); // mostrar los datos de la colec en el tb
        configureTable();


    }
}
