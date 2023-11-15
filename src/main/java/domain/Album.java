package domain;

public class Album {
    private String albumID;
    private String name;
    private String genre;
    private String year;

    public Album() {
    }

    public Album(String albumID, String name, String genre, String year) {
        this.albumID = albumID;
        this.name = name;
        this.genre = genre;
        this.year = year;
    }

    public String getAlbumID() {
        return albumID;
    }

    public void setAlbumID(String albumID) {
        this.albumID = albumID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "Album{" +
                "albumID='" + albumID + '\'' +
                ", name='" + name + '\'' +
                ", genre='" + genre + '\'' +
                ", year='" + year + '\'' +
                '}';
    }
}
