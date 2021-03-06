package com.zoha131.eshopkeeper.login;


import com.zoha131.eshopkeeper.action.sell.SellView;
import com.zoha131.eshopkeeper.home.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class LoginController implements Initializable {

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label wrngLbl;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String uname = username.getText();
        String pword = password.getText();

        if (true) { //uname.equals("zoha131") && pword.equals("zoha131")
            loadMain();
        } else {
            wrngLbl.setOpacity(1);
        }
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        System.exit(0);
    }

    private void closeStage() {
        ((Stage) username.getScene().getWindow()).close();
    }

    void loadMain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zoha131/eshopkeeper/action/sell/sellView.fxml"));
            Parent parent = loader.load();
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setId("scrollSell");
            scrollPane.setContent(parent);
            Main.getRoot().setCenter(scrollPane);
            Main.getRoot().setTop(Main.getMainMenu());
            SellView sellView = loader.getController();
            sellView.setScrollPane(scrollPane);

        } catch (IOException ex) {

        }
    }

}
