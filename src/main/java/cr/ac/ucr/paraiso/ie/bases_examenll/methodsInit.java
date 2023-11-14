package cr.ac.ucr.paraiso.ie.bases_examenll;

import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;

public class methodsInit {



    // set buttons
    public void setImages(Scene actual){
        actual.getStylesheets().add(getClass().getResource("/butStyle.css").toExternalForm());
    }

    // set genres on choiceBoxes
    public void setGenres(ChoiceBox<String> genreBox){


        String[] genres = {
                "Rock",
                "Pop",
                "Hip-hop",
                "Jazz",
                "Electrónica",
                "Clásica",
                "Country",
                "Reggae",
                "Reggaeton",
                "Blues",
                "Metal",
                "Rap",
                "Indie",
                "Folk",
                "Punk",
                "Soul",
                "R&B",
                "Funk",
                "Disco",
                "Gospel",
                "Ska",
                "Experimental",
                "World",
                "Ambient",
                "Dubstep",
                "Trap",
                "Reguetón",
                "Cumbia",
                "Salsa",
                "Merengue",
                "Bachata",
                "Flamenco",
                "Tango",
                "Fusion",
                "Chillout",
                "Synthwave",
                "Grime",
                "Nu Metal",
                "Opera",
                "Hard Rock",
                "Bluegrass",
                "Grunge",
                "Samba",
                "Techno"

        };
        genreBox.getItems().addAll(genres);

    }

}
