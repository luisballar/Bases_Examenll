package data;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import domain.Album;
import domain.Song;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MongoOperations {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;
    private ClientSession session;

    public MongoOperations(String connectionString, String databaseName, String colectionName) {

        // configura la conexión a tu clúster de MongoDB Atlas
        this.mongoClient = MongoClients.create(MongoClientSettings.builder().applyConnectionString(new ConnectionString(connectionString)).build());

        // selecciona la db y coleccion en la que trabajar
        this.database = mongoClient.getDatabase(databaseName);
        this.collection = database.getCollection(colectionName);
        session = mongoClient.startSession();
        session.startTransaction();

    }

    public void insertDocument(Document document) {
        // Realiza la inserción
        collection.insertOne(document);
        System.out.println("Agregado Correctamente");
        mongoClient.close();
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

        if(session.hasActiveTransaction() == true ){
            FindIterable<Document> findIterable = collection.find();
            Iterator<Document> iterator = findIterable.iterator();
            while(iterator.hasNext()){
                Document document = iterator.next();
                System.out.println(document);
                System.out.println(document.get("title"));
                //session.close();
            }
        }else
            System.out.println("Error de Sesión");

    }

    public void logicDelete(String docID){

        // Filtrar el documento que deseas actualizar
        Document filter = new Document("_id", docID);

        // Definir la actualización que deseas realizar
        Document update = new Document("$set", new Document("logic_delete", 1));

        // Ejecutar la actualización
        collection.updateOne(filter, update);

        System.out.println("Se borro logicamente");
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

}

