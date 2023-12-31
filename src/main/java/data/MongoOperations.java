package data;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import domain.Album;
import domain.Artist;
import domain.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.*;

import static com.mongodb.client.model.Indexes.descending;

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
        Bson filter = Filters.eq("logic_delete", 0); // valida que solo muestre los que no se han borrado
        FindIterable<Document> findIterable = collection.find(filter);
        ObservableList<Album> data = FXCollections.observableArrayList();

        for (Document document : findIterable) {
            data.add(new Album(document));
        }

        tableView.setItems(data);
    }

    // consulta para mostrar en el tableView Artistas
    public void fetchAndDisplayDataArtist(TableView tableView) {
        Bson filter = Filters.eq("logic_delete", 0); // valida que solo muestre los que no se han borrado
        FindIterable<Document> findIterable = collection.find(filter);
        ObservableList<Artist> data = FXCollections.observableArrayList();

        for (Document document : findIterable) {
            data.add(new Artist(document));
        }

        tableView.setItems(data);
    }

    // consulta para mostrar en el tableView Artistas
    public void fetchAndDisplayDataSong(TableView tableView) {
        Bson filter = Filters.eq("logic_delete", 0); // valida que solo muestre los que no se han borrado
        FindIterable<Document> findIterable = collection.find(filter);
        ObservableList<Song> data = FXCollections.observableArrayList();

        for (Document document : findIterable) {
            data.add(new Song(document));
        }

        tableView.setItems(data);
    }


    // muestra solo un album con el id ingresado
    public void fetchAndDisplayDataOneAlbum(TableView tableView, Document doc) {
        Bson filter = Filters.eq("logic_delete", 0); // valida que solo muestre los que no se han borrado
        FindIterable<Document> findIterable = collection.find(filter);
        ObservableList<Album> data = FXCollections.observableArrayList();

        data.add(new Album(doc));

        tableView.setItems(data);
    }


    // muestra solo un artitsta con el id ingresado
    public void fetchAndDisplayDataOneArtist(TableView tableView, Document doc) {
        Bson filter = Filters.eq("logic_delete", 0); // valida que solo muestre los que no se han borrado
        FindIterable<Document> findIterable = collection.find(filter);
        ObservableList<Artist> data = FXCollections.observableArrayList();

        data.add(new Artist(doc));

        tableView.setItems(data);
    }

    // muestra solo una cancion con el id ingresado
    public void fetchAndDisplayDataOneSong(TableView tableView, Document doc) {
        Bson filter = Filters.eq("logic_delete", 0); // valida que solo muestre los que no se han borrado
        FindIterable<Document> findIterable = collection.find(filter);
        ObservableList<Song> data = FXCollections.observableArrayList();

        data.add(new Song(doc));

        tableView.setItems(data);
    }

    // consulta para mostrar todos los albums por title
    public void showAlbumsName(TableView tableView, String partialName) {
        Bson filter = Filters.eq("logic_delete", 0); // valida que solo muestre los que no se han borrado
        FindIterable<Document> findIterable = collection.find(filter);

        ObservableList<Album> data = FXCollections.observableArrayList();

        for (Document document : findIterable) {
            String artistName = document.getString("title");

            // Utilizar expresiones regulares para verificar si contiene las letras parciales
            if (artistName != null && artistName.toLowerCase().matches(".*" + partialName.toLowerCase() + ".*")) {
                data.add(new Album(document));
            }
        }

        tableView.setItems(data);
    }

    // consulta para mostrar todos los albums por genero
    public void showAlbumsGenre(TableView tableView, String partialGenre) {
        Bson filter = Filters.eq("logic_delete", 0); // valida que solo muestre los que no se han borrado
        FindIterable<Document> findIterable = collection.find(filter);

        ObservableList<Album> data = FXCollections.observableArrayList();

        for (Document document : findIterable) {
            String albumGenre = document.getString("genre");

            // Utilizar expresiones regulares para verificar si contiene las letras parciales
            if (albumGenre != null && albumGenre.toLowerCase().matches(".*" + partialGenre.toLowerCase() + ".*")) {
                data.add(new Album(document));
            }
        }

        tableView.setItems(data);
    }

    // consulta para mostrar todos los albums por genero
    public void showAlbumsYear(TableView tableView, String name) {
        Bson filter = Filters.eq("logic_delete", 0); // valida que solo muestre los que no se han borrado
        FindIterable<Document> findIterable = collection.find(filter);

        ObservableList<Album> data = FXCollections.observableArrayList();

        for (Document document : findIterable) {
            if(document.getString("year_release").toLowerCase().equals(name.toLowerCase())) {
                data.add(new Album(document));
            }
        }

        tableView.setItems(data);
    }

    // consulta para mostrar todos los artistas de los albums
    public void showArtistsOnAlbum(TableView tableView, String albumTitle) {
        Bson filter = Filters.eq("logic_delete", 0); // valida que solo muestre los que no se han borrado
        FindIterable<Document> findIterable = collection.find(filter);

        ObservableList<Album> data = FXCollections.observableArrayList();

        for (Document document : findIterable) {
            String albTitleFound = document.getString("artist");

            // Utilizar expresiones regulares para verificar si contiene las letras parciales
            if (albTitleFound != null && albTitleFound.toLowerCase().matches(".*" + albumTitle.toLowerCase() + ".*")) {
                data.add(new Album(document));
            }
        }

        tableView.setItems(data);
    }

    // enmascara en el tableView el año
    public void maskAlbum(TableView tableView) {
        Bson filter = Filters.eq("logic_delete", 0); // valida que solo muestre los que no se han borrado
        FindIterable<Document> findIterable = collection.find(filter);

        ObservableList<Album> data = FXCollections.observableArrayList();

        for (Document document : findIterable) {
            data.add(new Album(document, ""));
        }

        tableView.setItems(data);
    }

    // enmascara en el tableView el artista
    public void maskSong(TableView tableView) {
        Bson filter = Filters.eq("logic_delete", 0); // valida que solo muestre los que no se han borrado
        FindIterable<Document> findIterable = collection.find(filter);

        ObservableList<Song> data = FXCollections.observableArrayList();

        for (Document document : findIterable) {
            data.add(new Song(document, ""));
        }

        tableView.setItems(data);
    }

    // consulta para mostrar todos los albums por title
    public void showArtistName(TableView tableView, String partialName) {
        Bson filter = Filters.eq("logic_delete", 0); // valida que solo muestre los que no se han borrado
        FindIterable<Document> findIterable = collection.find(filter);

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

    // consulta para mostrar todos los artistas por genero
    public void showArtistsGenre(TableView tableView, String partialGenre) {
        Bson filter = Filters.eq("logic_delete", 0); // valida que solo muestre los que no se han borrado
        FindIterable<Document> findIterable = collection.find(filter);

        ObservableList<Artist> data = FXCollections.observableArrayList();

        for (Document document : findIterable) {
            String artistGenre = document.getString("genre");

            // Utilizar expresiones regulares para verificar si contiene las letras parciales
            if (artistGenre != null && artistGenre.toLowerCase().matches(".*" + partialGenre.toLowerCase() + ".*")) {
                data.add(new Artist(document));
            }
        }

        tableView.setItems(data);
    }

    // consulta para mostrar todos los artistas por nacionalidad
    public void showArtistNationality(TableView tableView, String partialNat) {
        Bson filter = Filters.eq("logic_delete", 0); // valida que solo muestre los que no se han borrado
        FindIterable<Document> findIterable = collection.find(filter);

        ObservableList<Artist> data = FXCollections.observableArrayList();

        for (Document document : findIterable) {
            String nationality = document.getString("nationality");

            // Utilizar expresiones regulares para verificar si contiene las letras parciales
            if (nationality != null && nationality.toLowerCase().matches(".*" + partialNat.toLowerCase() + ".*")) {
                data.add(new Artist(document));
            }
        }

        tableView.setItems(data);
    }

    public void showSongsName(TableView tableView, String partialName) {
        Bson filter = Filters.eq("logic_delete", 0); // valida que solo muestre los que no se han borrado
        FindIterable<Document> findIterable = collection.find(filter);

        ObservableList<Song> data = FXCollections.observableArrayList();

        for (Document document : findIterable) {
            String artistName = document.getString("title");

            // Utilizar expresiones regulares para verificar si contiene las letras parciales
            if (artistName != null && artistName.toLowerCase().matches(".*" + partialName.toLowerCase() + ".*")) {
                data.add(new Song(document));
            }
        }

        tableView.setItems(data);
    }

    public void showSongsGenre(TableView tableView, String partialGenre) {
        Bson filter = Filters.eq("logic_delete", 0); // valida que solo muestre los que no se han borrado
        FindIterable<Document> findIterable = collection.find(filter);

        ObservableList<Song> data = FXCollections.observableArrayList();

        for (Document document : findIterable) {
            String songName = document.getString("title");

            // Utilizar expresiones regulares para verificar si contiene las letras parciales
            if (songName != null && songName.toLowerCase().matches(".*" + partialGenre.toLowerCase() + ".*")) {
                data.add(new Song(document));
            }
        }

    }

    public void showSongAlbum(TableView tableView, String name) {
        Bson filter = Filters.eq("logic_delete", 0); // valida que solo muestre los que no se han borrado
        FindIterable<Document> findIterable = collection.find(filter);

        ObservableList<Song> data = FXCollections.observableArrayList();

        for (Document document : findIterable) {
            if(document.getString("album").toLowerCase().equals(name.toLowerCase())) {
                data.add(new Song(document));
            }
        }

        tableView.setItems(data);
    }

    public void showSongArtist(TableView tableView, String name) {
        Bson filter = Filters.eq("logic_delete", 0); // valida que solo muestre los que no se han borrado
        FindIterable<Document> findIterable = collection.find(filter);

        ObservableList<Song> data = FXCollections.observableArrayList();

        for (Document document : findIterable) {
            if(document.getString("artist").toLowerCase().equals(name.toLowerCase())) {
                data.add(new Song(document));
            }
        }

        tableView.setItems(data);
    }

    // enmascara en el tableView la nacionalidad
    public void maskArtist(TableView tableView) {
        Bson filter = Filters.eq("logic_delete", 0); // valida que solo muestre los que no se han borrado
        FindIterable<Document> findIterable = collection.find(filter);

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
    public void deleteDocuments(int idField) {
        // crear un filtro para especificar los documentos que deseas borrar

        // borrar múltiples documentos que coinciden con el filtro
        DeleteResult deleteResult = collection.deleteOne(Filters.eq("_id",idField));

        // imprimir la cantidad de documentos eliminados
        System.out.println("Documentos eliminados: " + deleteResult.getDeletedCount());
    }

    public void logicDelete(int docID){

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

    // Revisa el ultimo ID y asignar ultimo +1
    public int asignaID() {
        int asignado;
        int asginadoSuma = 0;

        FindIterable<Document> sortedDocuments = collection.find().sort(Sorts.descending("_id")).limit(1);

        Document lastDocument = sortedDocuments.first();


        if (lastDocument != null) {
            // Obtener el valor del último _id y sumar 1
            asignado = lastDocument.getInteger("_id");
            asginadoSuma = asignado + 1;
        } else {
            return 1;
        }

        return asginadoSuma;
    }

    // verifica si existe por ID
    public Document exists(int idDoc) {
        try {

            Bson filter = Filters.and(
                    Filters.eq("_id", idDoc),
                    Filters.eq("logic_delete", 0)
            );

            Document document = collection.find(filter).first();
            return document;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Document existsAlbumForTitle(String partialName) {
        try {
            Collation collation = Collation.builder().locale("en").collationStrength(CollationStrength.SECONDARY).build();

            // Crear una expresión regular para buscar parcialmente el nombre
            Bson filter = Filters.and(
                    Filters.regex("title", partialName, "i"), // "i" para hacer la búsqueda insensible a mayúsculas y minúsculas
                    Filters.eq("logic_delete", 0)
            );

            Document document = collection.find(filter).collation(collation).first();
            System.out.println(document);
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
            Bson filter = Filters.and(
                    Filters.regex("name", partialName, "i"), // "i" para hacer la búsqueda insensible a mayúsculas y minúsculas
                    Filters.eq("logic_delete", 0)
            );

            Document document = collection.find(filter).collation(collation).first();
            System.out.println(document);
            return document;
        } catch (IllegalArgumentException e) {

            return null;
        }
    }

    public Document existsForArtistNameInSong(String partialName) {
        try {
            Collation collation = Collation.builder().locale("en").collationStrength(CollationStrength.SECONDARY).build();

            // Crear una expresión regular para buscar parcialmente el nombre
            Bson filter = Filters.and(
                    Filters.regex("artist", partialName, "i"), // "i" para hacer la búsqueda insensible a mayúsculas y minúsculas
                    Filters.eq("logic_delete", 0)
            );

            Document document = collection.find(filter).collation(collation).first();
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

            // Crear una expresión regular para buscar parcialmente el nombre
            Bson filter = Filters.and(
                    Filters.regex("genre", genreDoc, "i"),
                    Filters.eq("logic_delete", 0)
            );

            Document document = collection.find(filter).collation(collation).first();
            System.out.println(document);
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
            Bson filter = Filters.and(
                    Filters.regex("nationality", partialNat, "i"), // "i" para hacer la búsqueda insensible a mayúsculas y minúsculas
                    Filters.eq("logic_delete", 0)
            );

            Document document = collection.find(filter).collation(collation).first();
            System.out.println(document);
            return document;
        } catch (IllegalArgumentException e) {

            return null;
        }
    }

    // verifica si existe por year
    public Document existsForYear(String yearDoc) {
        try {

            // Crear una expresión regular para buscar parcialmente el nombre
            Bson filter = Filters.and(
                    Filters.regex("year_release", yearDoc),
                    Filters.eq("logic_delete", 0)
            );

            Document document = collection.find(filter).first();
            System.out.println(document);            return document;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }


    // delete docs
    public void updateArtist(int idField, String nationality, String genre) {
        // crear un filtro para especificar los documentos que deseas borrar

        Bson filter = Filters.eq("_id",idField); // identifica el documento

        Bson doc = Updates.combine(
                Updates.set("nationality", nationality),
                Updates.set("genre", genre)
        );

        collection.updateOne(filter,doc); // actualiza el documento solicitado
        System.out.println("Documento Actualizado");

    }

    public void updateAlbum(int idField, String title, String genre, String year) {
        // crear un filtro para especificar los documentos que deseas borrar

        Bson filter = Filters.eq("_id",idField); // identifica el documento

        Bson doc = Updates.combine(
                Updates.set("title", title),
                Updates.set("genre", genre),
                Updates.set("year_release", year)
        );

        collection.updateOne(filter,doc); // actualiza el documento solicitado
        System.out.println("Documento Actualizado");

    }

    public void updateSong(int idField, String title, String genre, String album, String artist) {
        // crear un filtro para especificar los documentos que deseas borrar

        Bson filter = Filters.eq("_id",idField); // identifica el documento

        Bson doc = Updates.combine(
                Updates.set("title", title),
                Updates.set("genre", genre),
                Updates.set("album", album),
                Updates.set("artist", artist)
        );

        collection.updateOne(filter,doc); // actualiza el documento solicitado
        System.out.println("Documento Actualizado");

    }

    // cargar artistas en choiceBox
    public List<String> loadArtistsIntoChoiceBox() {
        MongoCollection<Document> artistCollection = database.getCollection("Artist");

        // Realizar una consulta para obtener los nombres de los artistas
        FindIterable<Document> artists = artistCollection.find(Filters.eq("logic_delete",0));
        List<String> artistas = new ArrayList<>();

        for (Document artist : artists) {
            // Suponiendo que el nombre del artista está en el campo "name"
            String artistName = artist.getString("name");
            System.out.println(artistName);
            artistas.add(artistName);
        }

        return artistas;
    }

    // cargar albums en choiceBox
    public List<String> loadAlbumsIntoChoiceBox() {
        MongoCollection<Document> artistCollection = database.getCollection("Album");

        // Realizar una consulta para obtener los nombres de los artistas
        FindIterable<Document> artists = artistCollection.find(Filters.eq("logic_delete",0));
        List<String> artistas = new ArrayList<>();

        for (Document artist : artists) {
            // Suponiendo que el nombre del artista está en el campo "name"
            String artistName = artist.getString("title");
            System.out.println(artistName);
            artistas.add(artistName);
        }

        return artistas;
    }



}

