package cr.ac.ucr.paraiso.ie.bases_examenll;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.UnaryOperator;

public class MethodsInit {
    private static MethodsInit instance;

    public static MethodsInit getInstance() {
        if(instance == null){
            instance = new MethodsInit();
        }
        return instance;
    }

    // set buttons
    public void setImages(Scene actual){
        actual.getStylesheets().add(getClass().getResource("/butStyle.css").toExternalForm());
    }

    // set sexuality on choiceBoxes
    public void setSex(ChoiceBox<String> genreBox){
        String[] genres = {"M","F", "Otro"};
        genreBox.getItems().addAll(genres);
    }

    public void disable(Button but){
        but.setDisable(true);
    }

    // set genres on choiceBoxes
    public void setGenres(ChoiceBox<String> genreBox){

        String[] genres = {
                "Ambient",
                "Bachata",
                "Blues",
                "Bluegrass",
                "Chillout",
                "Clásica",
                "Country",
                "Cumbia",
                "Disco",
                "Dubstep",
                "Electrónica",
                "Experimental",
                "Flamenco",
                "Folk",
                "Funk",
                "Fusion",
                "Gospel",
                "Grime",
                "Grunge",
                "Hard Rock",
                "Hip-hop",
                "Indie",
                "Jazz",
                "Metal",
                "Merengue",
                "Nu Metal",
                "Opera",
                "Punk",
                "R&B",
                "Rap",
                "Reggae",
                "Reguetón",
                "Rock",
                "Salsa",
                "Samba",
                "Ska",
                "Soul",
                "Synthwave",
                "Tango",
                "Techno",
                "Trap",
                "World"
        };

        genreBox.getItems().addAll(genres);

    }


    // validar que no entren solo blank spaces
    public UnaryOperator<TextFormatter.Change> validateBlankSpaces() {
        return change -> {
            String newText = change.getControlNewText();

            // Permitir el cambio si la nueva cadena es vacía o no contiene solo espacios en blanco
            if (newText.isEmpty() || !newText.trim().isEmpty()) {
                return change;
            }

            return null; // No permitir cambios (solo espacios en blanco)
        };
    }

    // validar que solo entren numeros
    public TextFormatter<String> onlyNumber(){
        // Crear un filtro para permitir solo números
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;  // Permitir cambios si la nueva entrada son solo números
            }
            return null;  // No permitir cambios si la nueva entrada contiene otros caracteres
        });

        return formatter;
    }

    public UnaryOperator<TextFormatter.Change> validateSpacesAndNumbers() {
        return change -> {
            String newText = change.getControlNewText();

            // Permitir el cambio si la nueva cadena es vacía o no contiene solo espacios en blanco
            if (newText.isEmpty() || !newText.trim().isEmpty()) {
                // También validar que la nueva entrada son solo números
                if (newText.matches("\\d*")) {
                    return change;  // Permitir cambios si la nueva entrada cumple ambas condiciones
                }
            }

            return null;  // No permitir cambios (solo espacios en blanco o no son solo números)
        };
    }


    // codigo para acceder a los demas stages
    public void showWindow(FXMLLoader loader, Object objectClass, Scene scene, Stage nuevoStage, Stage actual, Button node, String fmxl) throws IOException {
        if(objectClass == null){
            loader = new FXMLLoader(getClass().getResource(fmxl));
            scene = new Scene(loader.load());

            objectClass = loader.getController();

            nuevoStage = new Stage();
            nuevoStage.setScene(scene);
            setImages(scene);// set buttons images
            nuevoStage.show();
            actual = (Stage) node.getScene().getWindow(); // asignar el stage actual

        }else
            nuevoStage.show();


        nuevoStage.setResizable(false);
        actual.close();


        int hashCode = objectClass.hashCode();

        // Imprimir el valor hash
        System.out.println(fmxl + " direccion: " + hashCode);
    }



}
