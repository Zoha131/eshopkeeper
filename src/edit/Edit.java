package edit;

import home.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import model.Customer;

public class Edit {
    @FXML private TabPane editTab;
    BorderPane root;

    public void initialize(){
        ObservableList<Customer> data = FXCollections.observableArrayList();
        root = Main.getRoot();
        editTab.tabMinWidthProperty().bind(root.widthProperty().divide(editTab.getTabs().size()).subtract(21));
        //todo-me to refactor edit data in table column. Current problem is the data don't update in the current observable list.
    }
}
