package help.about;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;

import java.awt.*;
import java.net.URI;

public class About {
    @FXML    Hyperlink email, linkedin, github, icon;

    public void initialize(){
        email.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("mailto:abirhasanzoha@gmail.com"));
            }
            catch (Exception e){

            }
        });

        linkedin.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.linkedin.com/in/zoha131/"));
            }
            catch (Exception e){

            }
        });

        github.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/Zoha131"));
            }
            catch (Exception e){

            }
        });

        icon.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.flaticon.com/authors/smashicons"));
            }
            catch (Exception e){

            }
        });
    }
}
