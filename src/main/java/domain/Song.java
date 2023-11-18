package domain;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import org.bson.Document;

import java.util.Arrays;

public class Song {

    private SimpleIntegerProperty songID;
    private SimpleStringProperty title;
    private SimpleStringProperty genre;
    private SimpleStringProperty album;
    private SimpleStringProperty artist;

    public Song(Document document) {
        this.songID = new SimpleIntegerProperty(document.getInteger("_id"));
        this.title = new SimpleStringProperty(document.getString("title"));
        this.genre = new SimpleStringProperty(document.getString("genre"));
        this.album = new SimpleStringProperty(document.getString("album"));
        this.artist = new SimpleStringProperty(document.getString("artist"));
    }

    public Song(Document document, String text) {
        this.songID = new SimpleIntegerProperty(document.getInteger("_id"));
        this.title = new SimpleStringProperty(document.getString("title"));
        this.genre = new SimpleStringProperty(document.getString("genre"));
        this.album = new SimpleStringProperty(document.getString("album"));
        this.artist = getArtist(document);
    }

    public int getSongID() {
        return songID.get();
    }

    public SimpleIntegerProperty songIDProperty() {
        return songID;
    }

    public void setSongID(int songID) {
        this.songID.set(songID);
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
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

    public String getAlbum() {
        return album.get();
    }

    public SimpleStringProperty albumProperty() {
        return album;
    }

    public void setAlbum(String album) {
        this.album.set(album);
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

    public SimpleStringProperty getArtist(Document doc) {
        char[] enmask = new char[new String(doc.getString("artist")).length()];
        Arrays.fill(enmask, '*');
        return new SimpleStringProperty(String.valueOf(enmask));
    }

    @Override
    public String toString() {
        return "Song{" +
                "muiscID=" + songID +
                ", title=" + title +
                ", genre=" + genre +
                ", album=" + album +
                ", artist=" + artist +
                '}';
    }
}
