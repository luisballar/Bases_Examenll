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

        Document document = new Document("_id", op.asignaID())
                .append("title", nameField.getText())
                .append("genre", albumBox.getValue())
                .append("year_release", yearField.getText())
                .append("logic_delete", "0");

        op.insertDocument(document);

        nameField.clear();
        //albumBox.getItems().clear();
        yearField.clear();
    }

    @FXML
    void deleteButton_clicked(ActionEvent event) {

        if(op.exists(searchField.getText()) == true) {
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
    public String filterBox_action(ActionEvent event) {
        return (String) filterBox.getValue();
    }

    @FXML
    void logicDelete_clicked(ActionEvent event) {
    }

    @FXML
    void searchButton_clicked(ActionEvent event) {
        String option = filterBox_action(event);
        System.out.println(option);
        switch(option){
            case "ID":
                if(op.exists(searchField.getText()) == true){
                    System.out.println("si existe"); // valida si existe el id
                    break;
                }else{
                    alertMessage = new Alert(Alert.AlertType.ERROR);
                    alertMessage.setTitle("Error de Búsqueda");
                    alertMessage.setHeaderText(null);
                    alertMessage.setContentText("No se encontró el ID solicitado");
                    alertMessage.show();
                    break;
                }
            case "Nombre":
                if(op.existsForTitle(searchField.getText()) == true){
                    System.out.println("si existe"); // valida si existe el nombre
                    break;
                }else{
                    alertMessage = new Alert(Alert.AlertType.ERROR);
                    alertMessage.setTitle("Error de Búsqueda");
                    alertMessage.setHeaderText(null);
                    alertMessage.setContentText("No se encontró el Nombre solicitado");
                    alertMessage.show();
                    break;
                }
            case "Genero":
                if(op.existsForGenre(searchField.getText()) == true){
                    System.out.println("si existe"); // valida si existe el genero
                    break;
                }else{
                    alertMessage = new Alert(Alert.AlertType.ERROR);
                    alertMessage.setTitle("Error de Búsqueda");
                    alertMessage.setHeaderText(null);
                    alertMessage.setContentText("No se encontró el Genero solicitado");
                    alertMessage.show();
                    break;
                }

            case "Año":
                if(op.existsForYear(searchField.getText()) == true){
                    System.out.println("si existe"); // valida si existe el id
                    break;
                }else{
                    alertMessage = new Alert(Alert.AlertType.ERROR);
                    alertMessage.setTitle("Error de Búsqueda");
                    alertMessage.setHeaderText(null);
                    alertMessage.setContentText("No se encontró el Año solicitado");
                    alertMessage.show();
                    break;
                }


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
        yearField.setText(selectedAlbum.getYear());

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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MethodsInit.getInstance().setGenres(albumBox); // set genres on genreBox
        MethodsInit.getInstance().disable(viewAlbumBut);
        MethodsInit.getInstance().setFiltersAlbum(filterBox);


        //op.fetchAndDisplayData(tableView);
        configureTable(idColumn, nameColumn, genreColumn, yearColumn);
    }
}
