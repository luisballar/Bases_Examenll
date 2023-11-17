package domain;

import javafx.beans.property.SimpleStringProperty;
import org.bson.Document;

import java.util.Arrays;

public class Album {
    private SimpleStringProperty albumID;
    private SimpleStringProperty name;
    private SimpleStringProperty genre;
    private SimpleStringProperty year;

    public Album(Document document) {
        this.albumID = new SimpleStringProperty(document.getString("_id"));
        this.name = new SimpleStringProperty(document.getString("title"));
        this.genre = new SimpleStringProperty(document.getString("genre"));
        this.year = new SimpleStringProperty(document.getString("year_release"));
    }

    public Album(Document document, String text) {
        this.albumID = new SimpleStringProperty(document.getString("_id"));
        this.name = new SimpleStringProperty(document.getString("title"));
        this.genre = new SimpleStringProperty(document.getString("genre"));
        this.year = getYear(document);
    }


    public Album(SimpleStringProperty albumID, SimpleStringProperty name, SimpleStringProperty genre, SimpleStringProperty year) {
        this.albumID = albumID;
        this.name = name;
        this.genre = genre;
        this.year = year;
    }

    public String getAlbumID() {
        return albumID.get();
    }

    public SimpleStringProperty albumIDProperty() {
        return albumID;
    }

    public void setAlbumID(String albumID) {
        this.albumID.set(albumID);
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

    public String getGenre() {
        return genre.get();
    }

    public SimpleStringProperty genreProperty() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }

    public String getYear() {
        return year.get();
    }


    public SimpleStringProperty yearProperty() {
        return year;
    }

    public void setYear(String year) {
        this.year.set(year);
    }

    // return enmascaramiento
    public SimpleStringProperty getYear(Document doc) {
        char[] enmask = new char[new String(doc.getString("year_release")).length()];
        Arrays.fill(enmask, '*');
        return new SimpleStringProperty(String.valueOf(enmask));
    }

    @Override
    public String toString() {
        return "Album{" +
                "albumID=" + albumID +
                ", name=" + name +
                ", genre=" + genre +
                ", year=" + year +
                '}';
    }
}
