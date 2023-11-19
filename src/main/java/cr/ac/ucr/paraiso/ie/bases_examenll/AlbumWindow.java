package cr.ac.ucr.paraiso.ie.bases_examenll;

import com.mongodb.client.*;
import data.MongoOperations;
import domain.Album;
import domain.Artist;
import domain.Song;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.ResourceBundle;

public class AlbumWindow implements Initializable {

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button exitBut;

    @FXML
    private AnchorPane field;

    @FXML
    private ComboBox<String> filterBox;

    @FXML
    private TableColumn<Album, String> genreColumn;

    @FXML
    private ChoiceBox<String> albumBox;

    @FXML
    private ChoiceBox<String> yearChoiceBox;

    @FXML
    private TableColumn<Album, String> idColumn;

    @FXML
    private CheckBox logicDelete;

    @FXML
    private Button mask_button;

    @FXML
    private TableColumn<Album, String> nameColumn;

    @FXML
    private TableColumn<Album, String> artistColumn;

    @FXML
    private ChoiceBox<String> artistBox;

    @FXML
    private TextField nameField;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Album>tableView;

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

    @FXML
    private TableColumn<Album, String> yearColumn;
    @FXML
    private TextField yearField;
    private FXMLLoader loader;
    private Scene scene;
    private Stage nuevoStage;
    private Stage actual;
    private String collectionName = "Album";
    private MongoOperations op = new MongoOperations(collectionName);
    private MainWindow mainWindow;
    private ArtistWindow artistWindow;
    private Alert alertMessage;
    private Album selectedAlbum;
    private boolean clickeado;


    // insert data
    @FXML
    void addButton_clcked(ActionEvent event) {

        if(albumBox.getValue() != null && yearChoiceBox.getValue() != null && artistBox.getValue() != null && !nameField.getText().isEmpty()){

            Document document = new Document("_id", op.asignaID())
                    .append("title", nameField.getText().trim())
                    .append("genre", albumBox.getValue().trim())
                    .append("year_release", yearChoiceBox.getValue().trim())
                    .append("artist", artistBox.getValue().trim())
                    .append("logic_delete", 0);

            op.insertDocument(document);

            nameField.clear();
            albumBox.setValue(null);
            yearChoiceBox.setValue(null);
            artistBox.setValue(null);
            logicDelete.setSelected(false);

            op.fetchAndDisplayDataAlbum(tableView); // carga el tableView con los datos
            configureTable();


        }else{
            showAlert("Error al Ingresar","Debe ingresar todos los datos");
        }

    }


    @FXML
    void MouseClickeFilterBox(MouseEvent event) {

    }

    @FXML
    void deleteButton_clicked(ActionEvent event) {

        if(selectedAlbum != null) {
            //valida si se trata de borrado logico o fisico
            if (logicDelete.isSelected()) {
                op.logicDelete(selectedAlbum.getAlbumID());
            } else {
                op.deleteDocuments(selectedAlbum.getAlbumID());
            }


            nameField.clear();
            albumBox.setValue(null);
            yearChoiceBox.setValue(null);
            artistBox.setValue(null);
            logicDelete.setSelected(false);
            artistBox.setDisable(true);
            addButton.setDisable(false);


            op.fetchAndDisplayDataAlbum(tableView); // carga el tableView con los datos
            configureTable();

        }else {
            showAlert("Error al Eliminar","Seleccion un Album a eliminar");
        }

    }


    // close all
    @FXML
    void exitBut_clicked(ActionEvent event) {
        Platform.exit();

    }

    // ver que tiene seleccionado el filterBox
    @FXML
    public Object filterBox_action(ActionEvent event) {
        return filterBox.getValue();
    }

    @FXML
    void logicDelete_clicked(ActionEvent event) {
    }

    @FXML
    void searchButton_clicked(ActionEvent event) {
        String option = (String) filterBox_action(event);
        System.out.println(option);

        if(!searchField.getText().isEmpty() && option != null) {

            switch (option) {
                case "ID":
                    if (op.exists(Integer.parseInt(searchField.getText().trim())) != null) {

                        op.fetchAndDisplayDataOneAlbum(tableView, op.exists(Integer.parseInt(searchField.getText().trim()))); // mostrar el solicitado
                        configureTable();

                        break;
                    } else {
                        showAlert("Error de Búsqueda","No se encontró el ID solicitado");
                        break;
                    }
                case "Nombre":
                    if (op.existsAlbumForTitle(searchField.getText().trim()) != null) {

                        op.showAlbumsName(tableView, searchField.getText().trim()); // mostrar el solicitado
                        configureTable();

                        break;
                    } else {
                        showAlert("Error de Búsqueda","No se encontró el Nombre solicitado");
                        break;
                    }
                case "Genero":
                    if (op.existsForGenre(searchField.getText().trim()) != null) {

                        op.showAlbumsGenre(tableView, searchField.getText().trim()); // mostrar el solicitado
                        configureTable();

                        break;
                    } else {
                        showAlert("Error de Búsqueda","No se encontró el Género solicitado");
                        break;
                    }

                case "Año":
                    if (op.existsForYear(searchField.getText().trim()) != null) {

                        op.showAlbumsYear(tableView, searchField.getText().trim()); // mostrar el solicitado
                        configureTable();

                        break;
                    } else {
                        showAlert("Error de Búsqueda","No se encontró el Año solicitado");
                        break;
                    }
                case "Artista":
                    if (op.existsForArtistNameInSong(searchField.getText().trim()) != null) {

                        op.showArtistsOnAlbum(tableView, searchField.getText().trim()); // mostrar el solicitado
                        configureTable();

                        break;
                    } else {
                        showAlert("Error de Búsqueda","No se encontró el Artista solicitado");
                        break;
                    }


            }
        }else {
            op.fetchAndDisplayDataAlbum(tableView); // carga el tableView con los datos
            configureTable();
        }
    }

    @FXML
    void mask_button_clicked(ActionEvent event) {

        if(mask_button.getText().equals("Enmascarar")){
            mask_button.setText("Desenmascarar");

            op.maskAlbum(tableView); // metodo que enmascara el year
            configureTable(); // enmascara

        }else if(mask_button.getText().equals("Desenmascarar")){
            mask_button.setText("Enmascarar");
            op.fetchAndDisplayDataAlbum(tableView); // carga el tableView con los datos
            configureTable();

        }

    }


    // saber que fila esta siendo clicked
    @FXML
    void MouseClicked(MouseEvent event) {
        selectedAlbum = tableView.getSelectionModel().getSelectedItem();

        if(selectedAlbum != null){
            nameField.setText(selectedAlbum.getName());
            albumBox.getSelectionModel().select(selectedAlbum.getGenre());
            yearChoiceBox.getSelectionModel().select(selectedAlbum.getYear());
            artistBox.getSelectionModel().select(selectedAlbum.getArtist());
            artistBox.setDisable(true);
            addButton.setDisable(true);
        }


    }


    // cuando clickeo afuera del tableView
    @FXML
    void setMouseClicked(MouseEvent event) {
        selectedAlbum = tableView.getSelectionModel().getSelectedItem();

        nameField.clear();
        albumBox.setValue(null);
        yearChoiceBox.setValue(null);
        artistBox.setValue(null);
        artistBox.setDisable(false);
        addButton.setDisable(false);
        tableView.getSelectionModel().clearSelection();
    }

    @FXML
    void updateButton_clicked(ActionEvent event) {

        if(selectedAlbum == null) {

            showAlert("Error al Actualizar","Debe seleccionar un Album");


        }else if(nameField.getText().isEmpty()) {

            showAlert("Error al Actualizar","No deben haber espacios en blanco");

        }else if(selectedAlbum != null){
            if (op.exists(selectedAlbum.getAlbumID()) != null) {

                op.updateAlbum(selectedAlbum.getAlbumID(), nameField.getText(), albumBox.getValue(), yearChoiceBox.getValue());

            } else {

                showAlert("Error al Actualizar","No existe ese ID");

            }

            nameField.clear();
            albumBox.setValue(null);
            yearChoiceBox.setValue(null);
            artistBox.setDisable(false);
            addButton.setDisable(false);
            artistBox.setValue(null);


            op.fetchAndDisplayDataAlbum(tableView);
            configureTable();
        }
    }

    // observar que esta seleccionando el filterBox
    @FXML
    void onMouseClicked(MouseEvent event) {
        filterBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                op.fetchAndDisplayDataAlbum(tableView);
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
    void viewAlbumBut_clicked(ActionEvent event) {
    }


    // configure tableview
    private void configureTable(){
        idColumn.setCellValueFactory((TableColumn.CellDataFeatures<Album, String> data) ->
                new SimpleStringProperty(Integer.toString(data.getValue().albumIDProperty().get())));
        nameColumn.setCellValueFactory((TableColumn.CellDataFeatures<Album, String> data) -> data.getValue().nameProperty());
        genreColumn.setCellValueFactory((TableColumn.CellDataFeatures<Album, String> data) -> data.getValue().genreProperty());
        yearColumn.setCellValueFactory((TableColumn.CellDataFeatures<Album, String> data) -> data.getValue().yearProperty());
        artistColumn.setCellValueFactory((TableColumn.CellDataFeatures<Album, String> data) -> data.getValue().artistProperty());
    }

    public void setYears(){

        String[] yearsArray = {
                "2023", "2022", "2021", "2020", "2019", "2018", "2017", "2016", "2015", "2014",
                "2013", "2012", "2011", "2010", "2009", "2008", "2007", "2006", "2005", "2004",
                "2003", "2002", "2001", "2000", "1999", "1998", "1997", "1996", "1995", "1994",
                "1993", "1992", "1991", "1990", "1989", "1988", "1987", "1986", "1985", "1984",
                "1983", "1982", "1981", "1980", "1979", "1978", "1977", "1976", "1975", "1974",
                "1973", "1972", "1971", "1970", "1969", "1968", "1967", "1966", "1965", "1964",
                "1963", "1962", "1961", "1960", "1959", "1958", "1957", "1956", "1955", "1954",
                "1953", "1952", "1951", "1950", "1949", "1948"
        };

        yearChoiceBox.getItems().addAll(yearsArray);


    }


    public void setFiltersAlbum(){
        String[] filters = {
                "ID",
                "Nombre",
                "Genero",
                "Año",
                "Artista"
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setYears();// set years on genreBox
        artistBox.getItems().addAll(op.loadArtistsIntoChoiceBox());
        MethodsInit.getInstance().setGenres(albumBox); // set genres on genreBox
        MethodsInit.getInstance().disable(viewAlbumBut);

        searchField.setTextFormatter((new TextFormatter<>(MethodsInit.getInstance().validateSpacesAndNumbers()))); // solo admite numeros
        nameField.setTextFormatter(new TextFormatter<>(MethodsInit.getInstance().validateBlankSpaces())); // no permite espacios en blanco

        setFiltersAlbum();

        tableView.setOnMouseClicked(this::MouseClicked);


        op.fetchAndDisplayDataAlbum(tableView); // mostrar los datos de la colec en el tb
        configureTable();


        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }
}
