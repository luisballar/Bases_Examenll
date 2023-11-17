package data;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.CollationStrength;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import domain.Album;
import domain.Artist;
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

    // consulta para mostrar en el tableView Albums
    public void fetchAndDisplayDataAlbum(TableView tableView) {
        FindIterable<Document> findIterable = collection.find();
        ObservableList<Album> data = FXCollections.observableArrayList();

        for (Document document : findIterable) {
            data.add(new Album(document));
        }

        tableView.setItems(data);
    }

    // consulta para mostrar en el tableView Artistas
    public void fetchAndDisplayDataArtist(TableView tableView) {
        FindIterable<Document> findIterable = collection.find();
        ObservableList<Artist> data = FXCollections.observableArrayList();

        for (Document document : findIterable) {
            data.add(new Artist(document));
        }

        tableView.setItems(data);
    }


    // muestra solo un album con el id ingresado
    public void fetchAndDisplayDataOneAlbum(TableView tableView, Document doc) {
        ObservableList<Album> data = FXCollections.observableArrayList();

        data.add(new Album(doc));

        tableView.setItems(data);
    }


    // muestra solo un artitsta con el id ingresado
    public void fetchAndDisplayDataOneArtist(TableView tableView, Document doc) {
        ObservableList<Artist> data = FXCollections.observableArrayList();

        data.add(new Artist(doc));

        tableView.setItems(data);
    }


    // consulta para mostrar todos los albums por title
    public void showAlbumsName(TableView tableView, String name) {
        FindIterable<Document> findIterable = collection.find();
        ObservableList<Album> data = FXCollections.observableArrayList();

        for (Document document : findIterable) {
            if(document.getString("title").toLowerCase().equals(name.toLowerCase())) {
                data.add(new Album(document));
            }
        }

        tableView.setItems(data);
    }


    // consulta para mostrar todos los albums por title
    public void showArtistName(TableView tableView, String partialName) {
        FindIterable<Document> findIterable = collection.find();

        ObservableList<Artist> data = FXCollections.observableArrayList();

        for (Document document : findIterable) {
            String artistName = document.getString("name");

            // Utilizar expresiones regulares para verificar si contiene las letras parciales
            if (artistName != null && artistName.toLowerCase().matches(".*" + partialName.toLowerCase() + ".*")) {
                data.add(new Artist(document));
            }
        }

        tableView.setItems(data);
    }



    // consulta para mostrar todos los albums por genero
    public void showAlbumsGenre(TableView tableView, String name) {
        FindIterable<Document> findIterable = collection.find();
        ObservableList<Album> data = FXCollections.observableArrayList();

        for (Document document : findIterable) {
            if(document.getString("genre").toLowerCase().equals(name.toLowerCase())) {
                data.add(new Album(document));
            }
        }

        tableView.setItems(data);
    }

    // consulta para mostrar todos los artistas por genero
    public void showArtistsGenre(TableView tableView, String name) {
        FindIterable<Document> findIterable = collection.find();
        ObservableList<Artist> data = FXCollections.observableArrayList();

        for (Document document : findIterable) {
            if(document.getString("genre").toLowerCase().equals(name.toLowerCase())) {
                data.add(new Artist(document));
            }
        }

        tableView.setItems(data);
    }

    // consulta para mostrar todos los albums por title
    public void showArtistNationality(TableView tableView, String partialNat) {
        FindIterable<Document> findIterable = collection.find();

        ObservableList<Artist> data = FXCollections.observableArrayList();

        for (Document document : findIterable) {
            String artistName = document.getString("nationality");

            // Utilizar expresiones regulares para verificar si contiene las letras parciales
            if (artistName != null && artistName.toLowerCase().matches(".*" + partialNat.toLowerCase() + ".*")) {
                data.add(new Artist(document));
            }
        }

        tableView.setItems(data);
    }



    // consulta para mostrar todos los albums por genero
    public void showAlbumsYear(TableView tableView, String name) {
        FindIterable<Document> findIterable = collection.find();
        ObservableList<Album> data = FXCollections.observableArrayList();

        for (Document document : findIterable) {
            if(document.getString("year_release").toLowerCase().equals(name.toLowerCase())) {
                data.add(new Album(document));
            }
        }

        tableView.setItems(data);
    }

    // enmascara en el tableView el año
    public void maskAlbum(TableView tableView) {
        FindIterable<Document> findIterable = collection.find();
        ObservableList<Album> data = FXCollections.observableArrayList();

        for (Document document : findIterable) {
            data.add(new Album(document, ""));
        }

        tableView.setItems(data);
    }

    // enmascara en el tableView la nacionalidad
    public void maskArtist(TableView tableView) {
        FindIterable<Document> findIterable = collection.find();
        ObservableList<Artist> data = FXCollections.observableArrayList();

        for (Document document : findIterable) {
            data.add(new Artist(document, ""));
        }

        tableView.setItems(data);
    }

    // inserta un documento en una coleccion
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

    // Revisa el ultimo ID y asignar ultimo +1
    public String asignaID() {
        String asignado;
        long asginadoSuma = 0;

        // Obtener el último documento ordenando por _id de manera descendente
        Document lastDocument = collection.find().sort(new Document("_id", -1)).first();

        if (lastDocument != null) {
            // Obtener el valor del último _id y sumar 1
            asignado = lastDocument.getString("_id");
            asginadoSuma = Long.parseLong(asignado) + 1;
        } else {
            return "1";
        }

        return Long.toString(asginadoSuma);
    }

    // verifica si existe por ID
    public Document exists(String idDoc) {
        try {
            Document document = collection.find(new Document("_id", idDoc)).first();
            return document;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    // verifica si existe por titulo
    public Document existsForTitle(String titleDoc) {
        try {
            Collation collation = Collation.builder().locale("en").collationStrength(CollationStrength.SECONDARY).build();

            Document document = collection.find(new Document("title", titleDoc)).collation(collation).first(); // standard para ignorar minusculas
            return document;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    // verifica si existe por titulo
    public Document existsForArtistName(String partialName) {
        try {
            Collation collation = Collation.builder().locale("en").collationStrength(CollationStrength.SECONDARY).build();

            // Crear una expresión regular para buscar parcialmente el nombre
            Document document = collection.find(
                    Filters.regex("name", partialName, "i") // "i" para hacer la búsqueda insensible a mayúsculas y minúsculas
            ).collation(collation).first();

            System.out.println(document);
            return document;
        } catch (IllegalArgumentException e) {

            return null;
        }
    }

    // verifica si existe por genero
    public Document existsForGenre(String genreDoc) {
        try {
            Collation collation = Collation.builder().locale("en").collationStrength(CollationStrength.SECONDARY).build();

            Document document = collection.find(new Document("genre", genreDoc)).collation(collation).first();
            return document;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    // verifica si existe artista por nacionalidad
    public Document existsForNationality(String partialNat) {
        try {
            Collation collation = Collation.builder().locale("en").collationStrength(CollationStrength.SECONDARY).build();

            // Crear una expresión regular para buscar parcialmente el nombre
            Document document = collection.find(
                    Filters.regex("nationality", partialNat, "i") // "i" para hacer la búsqueda insensible a mayúsculas y minúsculas
            ).collation(collation).first();

            System.out.println(document);
            return document;
        } catch (IllegalArgumentException e) {

            return null;
        }
    }

    // verifica si existe por year
    public Document existsForYear(String yearDoc) {
        try {
            Document document = collection.find(new Document("year_release", yearDoc)).first();
            return document;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}

