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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private TableView<Document>tableView;

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
            System.out.println("No existe el ID");

    }

    // close all
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
    void searchButton_clicked(ActionEvent event) {

    }
    @FXML
    void mask_button_clicked(ActionEvent event) {

        if(mask_button.getText().equals("Enmascarar")){
            mask_button.setText("Desenmascarar");

            op.maskMethod(tableView); // metodo que enmascara el year

            idColumn.setCellValueFactory((TableColumn.CellDataFeatures<Album, String> data) -> data.getValue().albumIDProperty());
            nameColumn.setCellValueFactory((TableColumn.CellDataFeatures<Album, String> data) -> data.getValue().nameProperty());
            genreColumn.setCellValueFactory((TableColumn.CellDataFeatures<Album, String> data) -> data.getValue().genreProperty());
            yearColumn.setCellValueFactory((TableColumn.CellDataFeatures<Album, String> data) -> data.getValue().yearProperty());

        }else if(mask_button.getText().equals("Desenmascarar")){
            mask_button.setText("Enmascarar");
            op.fetchAndDisplayData(tableView); // carga el tableView con los datos
            configureTable(idColumn, nameColumn, genreColumn, yearColumn);

        }


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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MethodsInit.getInstance().setGenres(albumBox); // set genres on genreBox
        MethodsInit.getInstance().disable(viewAlbumBut);
        MethodsInit.getInstance().setFiltersAlbum(filterBox);


        op.fetchAndDisplayData(tableView);
        configureTable(idColumn, nameColumn, genreColumn, yearColumn);
    }

    // configure tableview
    private void configureTable(TableColumn<Album, String> idColumn, TableColumn<Album, String> title, TableColumn<Album, String> genre, TableColumn<Album, String> year){
        idColumn.setCellValueFactory((TableColumn.CellDataFeatures<Album, String> data) -> data.getValue().albumIDProperty());
        title.setCellValueFactory((TableColumn.CellDataFeatures<Album, String> data) -> data.getValue().nameProperty());
        genre.setCellValueFactory((TableColumn.CellDataFeatures<Album, String> data) -> data.getValue().genreProperty());
        year.setCellValueFactory((TableColumn.CellDataFeatures<Album, String> data) -> data.getValue().yearProperty());

    }


}
