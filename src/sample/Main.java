package sample;

import data_helper.DataHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    private static BorderPane root = new BorderPane();

    public static BorderPane getRoot(){
        return Main.root;
    }


    @Override
    public void start(Stage primaryStage) throws Exception{

        boolean data = DataHelper.createAllTables();
        System.out.println("All tables Created" + data);

        Parent addCustomerView = FXMLLoader.load(getClass().getResource("../add/product/product.fxml"));
        primaryStage.setTitle("eShopkeeper");

        MenuBar menuBar = this.getMenubar();
        root.setTop(menuBar);
        root.setCenter(addCustomerView);

        root.setMinWidth(1000);
        root.setMinHeight(600);

        System.out.println(DataHelper.creatCustomerTable());

        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    private MenuBar getMenubar(){
        MenuBar menuBar = new MenuBar();

        Menu views = new Menu("Views");
        MenuItem add_customer = new MenuItem("Add Customer");
        MenuItem add_product = new MenuItem("Add Product");
        views.getItems().addAll(add_customer,add_product);

        Menu help = new Menu("Help");
        MenuItem about = new MenuItem("About");
        help.getItems().addAll(about);

        menuBar.getMenus().addAll(views,help);


        return menuBar;
    }
}
