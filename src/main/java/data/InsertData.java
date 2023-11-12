package data;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class InsertData {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;


    public InsertData(String connectionString, String databaseName, String colectionName){

        // configura la conexión a tu clúster de MongoDB Atlas
        this.mongoClient = MongoClients.create(MongoClientSettings.builder().applyConnectionString(new ConnectionString(connectionString)).build());

        // selecciona la db y coleccion en la que trabajar
        this.database = mongoClient.getDatabase("luisballar");
        this.collection = database.getCollection("Song");

    }

    public void insertDocument(Document document) {
        // Realiza la inserción
        collection.insertOne(document);
        System.out.println("Agregado Correctamente");
        mongoClient.close();
    }

}
