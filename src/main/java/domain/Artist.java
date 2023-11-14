package domain;

public class Artist {
    private int artistID;
    private String name;
    private String lastName;
    private String natioanlity;
    private String genre;

    public Artist() {
    }

    public Artist(int artistID, String name, String lastName, String natioanlity, String genre) {
        this.artistID = artistID;
        this.name = name;
        this.lastName = lastName;
        this.natioanlity = natioanlity;
        this.genre = genre;
    }

    public int getArtistID() {
        return artistID;
    }

    public void setArtistID(int artistID) {
        this.artistID = artistID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNatioanlity() {
        return natioanlity;
    }

    public void setNatioanlity(String natioanlity) {
        this.natioanlity = natioanlity;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "artistID=" + artistID +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", natioanlity='" + natioanlity + '\'' +
                ", genre='" + genre + '\'' +
                '}';
    }

}
