package domain;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import org.bson.Document;

import java.util.Arrays;

public class Album {
    private SimpleIntegerProperty albumID;
    private SimpleStringProperty name;
    private SimpleStringProperty genre;
    private SimpleStringProperty year;
    private SimpleStringProperty artist;

    public Album(Document document) {
        this.albumID = new SimpleIntegerProperty(document.getInteger("_id"));
        this.name = new SimpleStringProperty(document.getString("title"));
        this.genre = new SimpleStringProperty(document.getString("genre"));
        this.year = new SimpleStringProperty(document.getString("year_release"));
        this.artist = new SimpleStringProperty(document.getString("artist"));
    }

    public Album(Document document, String text) {
        this.albumID = new SimpleIntegerProperty(document.getInteger("_id"));
        this.name = new SimpleStringProperty(document.getString("title"));
        this.genre = new SimpleStringProperty(document.getString("genre"));
        this.year = getYear(document);
        this.artist = new SimpleStringProperty(document.getString("artist"));

    }

    public int getAlbumID() {
        return albumID.get();
    }

    public SimpleIntegerProperty albumIDProperty() {
        return albumID;
    }

    public void setAlbumID(int albumID) {
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

    public String getArtist() {
        return artist.get();
    }

    public SimpleStringProperty artistProperty() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist.set(artist);
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
