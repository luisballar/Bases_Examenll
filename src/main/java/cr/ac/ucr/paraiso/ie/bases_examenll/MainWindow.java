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
            artisteBox.setDisable(false);
            addButton.setDisable(false);


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

            op.maskSong(tableView); // metodo que enmascara el album
            configureTable(); // enmascara

        }else if(mask_button.getText().equals("Desenmascarar")){
            mask_button.setText("Enmascarar");
            op.fetchAndDisplayDataSong(tableView); // carga el tableView con los datos
            configureTable();

        }
    }

    @FXML
    void setMouseClicked(MouseEvent event) {
        nameField.clear();
        genreBox.setValue(null);
        albumBox.setValue(null);
        artisteBox.setValue(null);
        artisteBox.setDisable(false);
        addButton.setDisable(false);
        tableView.getSelectionModel().clearSelection();

    }

    @FXML
    void onMouseClicked(MouseEvent event) {

        filterBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                op.fetchAndDisplayDataSong(tableView);
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
        String option = (String) filterBox_action(event);
        System.out.println(option);

        if(!searchField.getText().isEmpty() && option != null) {

            switch (option) {
                case "ID":
                    if (op.exists(Integer.parseInt(searchField.getText().trim())) != null) {

                        op.fetchAndDisplayDataOneSong(tableView, op.exists(Integer.parseInt(searchField.getText().trim()))); // mostrar el solicitado
                        configureTable();

                        break;
                    } else {
                        showAlert("Error de Búsqueda","No se encontró el ID solicitado");
                        break;
                    }
                case "Titulo":
                    if (op.existsAlbumForTitle(searchField.getText().trim()) != null) {

                        op.showSongsName(tableView, searchField.getText().trim()); // mostrar el solicitado
                        configureTable();

                        break;
                    } else {
                        showAlert("Error de Búsqueda","No se encontró el Nombre solicitado");
                        break;
                    }
                case "Genero":
                    if (op.existsForGenre(searchField.getText().trim()) != null) {

                        op.showSongsGenre(tableView, searchField.getText().trim()); // mostrar el solicitado
                        configureTable();

                        break;
                    } else {
                        showAlert("Error de Búsqueda","No se encontró el Género solicitado");
                        break;
                    }

                case "Album":
                    if (op.existsAlbumForTitle(searchField.getText().trim()) != null) {

                        op.showSongAlbum(tableView, searchField.getText().trim()); // mostrar el solicitado
                        configureTable();

                        break;
                    } else {
                        showAlert("Error de Búsqueda","No se encontró el Año solicitado");
                        break;
                    }
                case "Artista":
                    if (op.existsForArtistNameInSong(searchField.getText().trim()) != null) {

                        op.showSongArtist(tableView, searchField.getText().trim()); // mostrar el solicitado
                        configureTable();

                        break;
                    } else {
                        showAlert("Error de Búsqueda","No se encontró el Artista solicitado");
                        break;
                    }


            }
        }else {
            op.fetchAndDisplayDataSong(tableView); // carga el tableView con los datos
            configureTable();
        }
    }

    @FXML
    void updateButton_clicked(ActionEvent event) {
        if(selectedSong != null) {
            if (op.exists(selectedSong.getSongID()) != null) {

                op.updateSong(selectedSong.getSongID(), nameField.getText(), genreBox.getValue(), albumBox.getValue(), artisteBox.getValue());


            } else {
                showAlert("Error al Eliminar","No existe ese ID");
            }

            nameField.clear();
            genreBox.setValue(null);
            albumBox.setValue(null);
            artisteBox.setValue(null);
            logicDelete.setSelected(false);
            artisteBox.setDisable(false);
            addButton.setDisable(false);


            op.fetchAndDisplayDataSong(tableView);
            configureTable();

        }else{
            showAlert("Error al Actualizar","Debe seleccionar una Canción");
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
        artisteBox.setDisable(true);
        addButton.setDisable(true);


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

    private void showAlert(String title, String contentText) {
        alertMessage = new Alert(Alert.AlertType.ERROR);
        alertMessage.setTitle(title);
        alertMessage.setHeaderText(null);
        alertMessage.setContentText(contentText);
        alertMessage.show();
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
