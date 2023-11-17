package cr.ac.ucr.paraiso.ie.bases_examenll;

import com.mongodb.client.*;
import data.MongoOperations;
import domain.Album;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private ComboBox<?> filterBox;

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

    // insert data
    @FXML
    void addButton_clcked(ActionEvent event) {

        if(nameField.getText().isEmpty()){
            alertMessage = new Alert(Alert.AlertType.ERROR);
            alertMessage.setTitle("Error al Ingresar");
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Debe ingresar un nombre");
            alertMessage.show();

        }else{
            Document document = new Document("_id", op.asignaID())
                    .append("title", nameField.getText())
                    .append("genre", albumBox.getValue())
                    .append("year_release", yearChoiceBox.getValue())
                    .append("logic_delete", "0");

            op.insertDocument(document);

            nameField.clear();
            //albumBox.getItems().clear();
        }

    }

    @FXML
    void deleteButton_clicked(ActionEvent event) {

        if(op.exists(searchField.getText()) != null) {
            //valida si se trata de borrado logico o fisico
            if (logicDelete.isSelected()) {
                op.logicDelete(searchField.getText());
            } else {
                long number = op.countDocuments();
                System.out.println(number);
                op.deleteDocuments("_id", searchField.getText());
            }
        }else
            alertMessage = new Alert(Alert.AlertType.ERROR);
            alertMessage.setTitle("Error al Eliminar");
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("No se encontró es ID solicitado");
            alertMessage.show();
    }

    // close all
    @FXML
    void exitBut_clicked(ActionEvent event) {
        Platform.exit();

    }

    // ver que tiene seleccionado el filtro
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
        System.out.println("chomin" + filterBox_action(event));

        if(!searchField.getText().isEmpty() && option != null && option != null) {

            switch (option) {
                case "ID":
                    if (op.exists(searchField.getText()) != null) {

                        op.fetchAndDisplayDataOne(tableView, op.exists(searchField.getText())); // mostrar el solicitado
                        configureTable(idColumn, nameColumn, genreColumn, yearColumn);

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
                    if (op.existsForTitle(searchField.getText()) != null) {

                        op.fetchAndDisplayDataOne(tableView, op.existsForTitle(searchField.getText())); // mostrar el solicitado
                        configureTable(idColumn, nameColumn, genreColumn, yearColumn);

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

                        op.fetchAndDisplayDataOne(tableView, op.existsForGenre(searchField.getText())); // mostrar el solicitado
                        configureTable(idColumn, nameColumn, genreColumn, yearColumn);


                        break;
                    } else {
                        alertMessage = new Alert(Alert.AlertType.ERROR);
                        alertMessage.setTitle("Error de Búsqueda");
                        alertMessage.setHeaderText(null);
                        alertMessage.setContentText("No se encontró el Genero solicitado");
                        alertMessage.show();
                        break;
                    }

                case "Año":
                    if (op.existsForYear(searchField.getText()) != null) {

                        op.fetchAndDisplayDataOne(tableView, op.existsForYear(searchField.getText())); // mostrar el solicitado
                        configureTable(idColumn, nameColumn, genreColumn, yearColumn);

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
            op.fetchAndDisplayData(tableView); // carga el tableView con los datos
            configureTable(idColumn, nameColumn, genreColumn, yearColumn);
        }
    }

    @FXML
    void mask_button_clicked(ActionEvent event) {

        if(mask_button.getText().equals("Enmascarar")){
            mask_button.setText("Desenmascarar");

            op.maskMethod(tableView); // metodo que enmascara el year
            configureTableMask(idColumn, nameColumn, genreColumn, yearColumn);

        }else if(mask_button.getText().equals("Desenmascarar")){
            mask_button.setText("Enmascarar");
            op.fetchAndDisplayData(tableView); // carga el tableView con los datos
            configureTable(idColumn, nameColumn, genreColumn, yearColumn);

        }

    }


    // saber que fila esta siendo clicked
    @FXML
    void MouseClicked(MouseEvent event) {

        Album selectedAlbum = tableView.getSelectionModel().getSelectedItem();

        System.out.println(selectedAlbum.genreProperty());
        nameField.setText(selectedAlbum.getName());

    }

    @FXML
    void updateButton_clicked(ActionEvent event) {


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
    private void configureTable(TableColumn<Album, String> idColumn, TableColumn<Album, String> title, TableColumn<Album, String> genre, TableColumn<Album, String> year){
        idColumn.setCellValueFactory((TableColumn.CellDataFeatures<Album, String> data) -> data.getValue().albumIDProperty());
        title.setCellValueFactory((TableColumn.CellDataFeatures<Album, String> data) -> data.getValue().nameProperty());
        genre.setCellValueFactory((TableColumn.CellDataFeatures<Album, String> data) -> data.getValue().genreProperty());
        year.setCellValueFactory((TableColumn.CellDataFeatures<Album, String> data) -> data.getValue().yearProperty());

    }

    // enmascara el tableView
    private void configureTableMask(TableColumn<Album, String> idColumn, TableColumn<Album, String> title, TableColumn<Album, String> genre, TableColumn<Album, String> year){
        idColumn.setCellValueFactory((TableColumn.CellDataFeatures<Album, String> data) -> data.getValue().albumIDProperty());
        nameColumn.setCellValueFactory((TableColumn.CellDataFeatures<Album, String> data) -> data.getValue().nameProperty());
        genreColumn.setCellValueFactory((TableColumn.CellDataFeatures<Album, String> data) -> data.getValue().genreProperty());
        yearColumn.setCellValueFactory((TableColumn.CellDataFeatures<Album, String> data) -> data.getValue().yearProperty());

    }

    public void setYears(){

        String[] yearsArray = {
                "1948", "1949", "1950", "1951", "1952", "1953", "1954", "1955", "1956", "1957",
                "1958", "1959", "1960", "1961", "1962", "1963", "1964", "1965", "1966", "1967",
                "1968", "1969", "1970", "1971", "1972", "1973", "1974", "1975", "1976", "1977",
                "1978", "1979", "1980", "1981", "1982", "1983", "1984", "1985", "1986", "1987",
                "1988", "1989", "1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997",
                "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007",
                "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017",
                "2018", "2019", "2020", "2021", "2022", "2023"
        };

        yearChoiceBox.getItems().addAll(yearsArray);

    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setYears();// set years on genreBox
        MethodsInit.getInstance().setGenres(albumBox); // set genres on genreBox
        MethodsInit.getInstance().disable(viewAlbumBut);
        MethodsInit.getInstance().setFiltersAlbum(filterBox);

        //op.fetchAndDisplayData(tableView);
        configureTable(idColumn, nameColumn, genreColumn, yearColumn);
        filterBox.getSelectionModel().selectFirst();
        yearChoiceBox.getSelectionModel().selectFirst();
        albumBox.getSelectionModel().selectFirst();

    }
}
