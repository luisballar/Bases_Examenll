package data;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import domain.Album;
import domain.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;

public class MongoOperations {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;
    private ClientSession session;
    private String stringConn = "mongodb+srv://luisballar:C20937@if4100.kles8ol.mongodb.net/?retryWrites=true&w=majority";
    private String db = "luisballar";

    String title, genre, year;


    public MongoOperations(String colectionName) {

        // configura la conexión a tu clúster de MongoDB Atlas
        this.mongoClient = MongoClients.create(MongoClientSettings.builder().applyConnectionString(new ConnectionString(this.stringConn)).build());

        // selecciona la db y coleccion en la que trabajar
        this.database = mongoClient.getDatabase(this.db);
        this.collection = database.getCollection(colectionName);
        session = mongoClient.startSession();
        session.startTransaction();

    }

    public void fetchAndDisplayData(TableView tableView) {
        FindIterable<Document> findIterable = collection.find();
        ObservableList<Album> data = FXCollections.observableArrayList();

        for (Document document : findIterable) {
            data.add(new Album(document));
        }

        tableView.setItems(data);
    }

    public void configureTable(TableColumn<Album, String> idColumn, TableColumn<Album, String> title, TableColumn<Album, String> genre, TableColumn<Album, String> year){
        idColumn.setCellValueFactory((TableColumn.CellDataFeatures<Album, String> data) -> data.getValue().albumIDProperty());
    }

    public void insertDocument(Document document) {
        // Realiza la inserción
        collection.insertOne(document);
        System.out.println("Agregado Correctamente");
        //mongoClient.close();
    }

    // delete docs
    public void deleteDocuments(String idField, String value) {
        // crear un filtro para especificar los documentos que deseas borrar
        Document filter = new Document(idField, value);

        // borrar múltiples documentos que coinciden con el filtro
        DeleteResult deleteResult = collection.deleteOne(filter);

        // imprimir la cantidad de documentos eliminados
        System.out.println("Documentos eliminados: " + deleteResult.getDeletedCount());
    }

    // show consults
    public void imprimir(){
        Document document = null;
        if(session.hasActiveTransaction() == true ){
            FindIterable<Document> findIterable = collection.find();
            Iterator<Document> iterator = findIterable.iterator();
            while(iterator.hasNext()){
                document = iterator.next();
                System.out.println(document);
                //session.close();
            }
        }else
            System.out.println("Error de Sesión");

        System.out.println(document);
    }

    public void logicDelete(String docID){

        if(session.hasActiveTransaction() == true){
            // Filtrar el documento que deseas actualizar

            Document filter = new Document("_id", docID);

            // Definir la actualización que deseas realizar
            Document update = new Document("$set", new Document("logic_delete", 1));

            // Ejecutar la actualización
            collection.updateOne(filter, update);

            System.out.println("Se borro logicamente");
        }else
            System.out.println("Error de Sesión");

    }


    // count all documents from to collection
    public long countDocuments() {
        // Contar documentos en la colección
        return collection.countDocuments();
    }

    // Secuencial ID
    public String asignaID(){
        long asignado = 0;
        if(countDocuments() == 0)
            return "1";
        else if(countDocuments() > 0)
            asignado = countDocuments() + 1;
        
        return Long.toString(asignado);
    }

    // verifica si existe
    public boolean exists(String idDoc) {
        try {
            Document document = collection.find(new Document("_id", idDoc)).first();
            return document != null;
        } catch (IllegalArgumentException e) {
            // El idDoc no es un ObjectId válido
            System.out.println("ID inválido");
            return false;
        }
    }

}

