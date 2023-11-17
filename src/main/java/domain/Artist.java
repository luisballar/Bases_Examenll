package domain;

import javafx.beans.property.SimpleStringProperty;
import org.bson.Document;

import java.util.Arrays;

public class Artist {
    private SimpleStringProperty artistID;
    private SimpleStringProperty name;
    private SimpleStringProperty nationality;
    private SimpleStringProperty genre;


    public Artist(Document document){
        this.artistID = new SimpleStringProperty(document.getString("_id"));
        this.name = new SimpleStringProperty(document.getString("artist_name"));
        this.nationality = new SimpleStringProperty(document.getString("nationality"));
        this.genre = new SimpleStringProperty(document.getString("gender"));
    }

    public Artist(Document document, String text){
        this.artistID = new SimpleStringProperty(document.getString("_id"));
        this.name = new SimpleStringProperty(document.getString("artist_name"));
        this.nationality = getNationality(document); // traer enmascarado
        this.genre = new SimpleStringProperty(document.getString("gender"));

    }

    public String getArtistID() {
        return artistID.get();
    }

    public SimpleStringProperty artistIDProperty() {
        return artistID;
    }

    public void setArtistID(String artistID) {
        this.artistID.set(artistID);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getNationality() {
        return nationality.get();
    }

    public SimpleStringProperty nationalityProperty() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality.set(nationality);
    }

    public String getGenre() {
        return genre.get();
    }

    public SimpleStringProperty genreProperty() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }

    // retorna la nacionalidad enmascarada
    public SimpleStringProperty getNationality(Document doc) {
        char[] enmask = new char[new String(doc.getString("nationality")).length()];
        Arrays.fill(enmask, '*');
        return new SimpleStringProperty(String.valueOf(enmask));
    }

    @Override
    public String toString() {
        return "Artist{" +
                "artistID=" + artistID +
                ", name=" + name +
                ", nationality=" + nationality +
                ", genre=" + genre +
                '}';
    }
}
