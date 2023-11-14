package data;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import domain.Song;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class MongoOperations {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;


    public MongoOperations(String connectionString, String databaseName, String colectionName) {

        // configura la conexión a tu clúster de MongoDB Atlas
        this.mongoClient = MongoClients.create(MongoClientSettings.builder().applyConnectionString(new ConnectionString(connectionString)).build());

        // selecciona la db y coleccion en la que trabajar
        this.database = mongoClient.getDatabase(databaseName);
        this.collection = database.getCollection(colectionName);

    }

    public void insertDocument(Document document) {
        // Realiza la inserción
        collection.insertOne(document);
        System.out.println("Agregado Correctamente");
        mongoClient.close();
    }

    public void viewData(String dataBase, String collectionName) {

            MongoDatabase database = mongoClient.getDatabase(dataBase);
            MongoCollection<Document> collection = database.getCollection(collectionName);

            // Realizar consulta
            FindIterable<Document> documents = collection.find();


            // Poblar TableView con datos de MongoDB
            populateTableView(documents);

            // Mostrar ventana
            primaryStage.setScene(new Scene(tableView, 600, 400));
            primaryStage.setTitle("MongoDB TableView Example");
            primaryStage.show();


    }

    // poblate data in tableView
    private void populateTableView(FindIterable<Document> documents, TableView tableView) {
        // Poblar TableView con datos de MongoDB
        MongoCursor<Document> cursor = documents.iterator();
        while (cursor.hasNext()) {
            Document document = cursor.next();
            String id = document.getString("_id");
            String title = document.getString("columna1");
            String artist = document.getString("columna2");
            String genre = document.getString("columna3");
            String album = document.getString("columna4");
            String year = document.getString("columna5");
            tableView.getItems().add(new Song(id, ));
        }
    }


    public void insertSong(String nameField, String genreBox, String albumBox, String artisteBox){

        Document document = new Document("_id",new ObjectId())
                .append("title", nameField)
                .append("genre",genreBox)
                .append("album", albumBox)
                .append("artist", artisteBox)
                .append("logic_delete", 0);

        collection.insertOne(document);
        System.out.println("Agregado Correctamente");
        mongoClient.close(); // close connection

    }

}
