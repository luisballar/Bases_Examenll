package cr.ac.ucr.paraiso.ie.bases_examenll.domain;

public class Song {

    private int muiscID;
    private String title;
    private String artist;
    private String genre;

    private String year;

    public Song() {
    }

    public Song(int muiscID, String title, String artist, String genre, String year) {
        this.muiscID = muiscID;
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.year = year;
    }

    public int getMuiscID() {
        return muiscID;
    }

    public void setMuiscID(int muiscID) {
        this.muiscID = muiscID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "Song{" +
                "muiscID=" + muiscID +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", genre='" + genre + '\'' +
                ", year='" + year + '\'' +
                '}';
    }
}
