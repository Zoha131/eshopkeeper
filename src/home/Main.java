package home;

import data_helper.DataHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    private static BorderPane root = new BorderPane();
    private static Stage mainStage;

    public static BorderPane getRoot(){
        return Main.root;
    }
    public static Stage getMainStage(){
        return mainStage;
    }


    @Override
    public void start(Stage primaryStage) throws Exception{

        mainStage = primaryStage;

        boolean data = DataHelper.createAllTables();

        TabPane addCustomerView = FXMLLoader.load(getClass().getResource("../edit/edit.fxml"));


        MenuBar menuBar = this.getMenubar();
        root.setTop(menuBar);
        root.setCenter(addCustomerView);
        //root.setBottom(new Text("majha"));

        addCustomerView.prefWidthProperty().bind(root.widthProperty());
        addCustomerView.prefHeightProperty().bind(root.heightProperty().subtract(30));


        root.setMinWidth(1000);
        root.setMinHeight(600);
        primaryStage.setMinWidth(1030);
        primaryStage.setMinHeight(640);

        System.out.println("Main:: all table created: "+DataHelper.creatCustomerTable());

        primaryStage.setTitle("eShopkeeper");
        primaryStage.setScene(new Scene(root, 1000, 600));

        //primaryStage.setMaximized(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    private MenuBar getMenubar(){
        MenuBar menuBar = new MenuBar();

        Menu views = new Menu("Views");
        MenuItem add = new MenuItem("ADD");
        MenuItem edit = new MenuItem("Edit");
        add.setOnAction(event -> {
            try{
                TabPane add_tab = FXMLLoader.load(getClass().getResource("../add/add.fxml"));
                boolean sameView = Main.getRoot().getCenter().getId().equals(add_tab.getId());
                if(!sameView){
                    Main.getRoot().setCenter(add_tab);
                }
            }catch (Exception e){
                System.out.println("error " + e);
            }
        });
        edit.setOnAction(event -> {
            try{
                TabPane edit_tab = FXMLLoader.load(getClass().getResource("../edit/edit.fxml"));
                boolean sameView = Main.getRoot().getCenter().getId().equals(edit_tab.getId());
                if(!sameView){
                    Main.getRoot().setCenter(edit_tab);
                }

            }catch (Exception e){
                System.out.println("error " + e);
            }
        });
        views.getItems().addAll(add,edit);

        Menu help = new Menu("Help");
        MenuItem about = new MenuItem("About");
        help.getItems().addAll(about);

        menuBar.getMenus().addAll(views,help);
        menuBar.setPrefHeight(30);


        return menuBar;
    }
}

