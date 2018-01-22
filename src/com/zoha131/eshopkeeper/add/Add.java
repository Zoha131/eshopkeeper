package com.zoha131.eshopkeeper.add;

import com.zoha131.eshopkeeper.home.Main;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

public class Add {
    @FXML
    TabPane addTab;

    BorderPane root;

    public void initialize(){
        //I am sure that the parent would be borderpane
        root = Main.getRoot();
        addTab.tabMinWidthProperty().bind(root.widthProperty().divide(addTab.getTabs().size()).subtract(21));
    }
}

