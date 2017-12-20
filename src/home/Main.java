package home;

import data_helper.DataHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    private static BorderPane root = new BorderPane();
    private static Stage mainStage;
    private static MenuBar mainMenu;

    public static BorderPane getRoot(){
        return Main.root;
    }
    public static Stage getMainStage(){
        return mainStage;
    }
    public static MenuBar getMainMenu() {
        return mainMenu;
    }


    @Override
    public void start(Stage primaryStage) throws Exception{

        mainStage = primaryStage;

        boolean data = DataHelper.createAllTables();

        TabPane addCustomerView = FXMLLoader.load(getClass().getResource("/edit/edit.fxml"));
        Parent parent = FXMLLoader.load(getClass().getResource("/login/login.fxml"));


        mainMenu = this.getMenubar();
        root.setCenter(parent);

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

        Menu views = new Menu("Action");
        MenuItem add = new MenuItem("ADD");
        MenuItem edit = new MenuItem("Edit");
        MenuItem sell = new MenuItem("SellView");
        MenuItem transaction = new MenuItem("Transaction");
        MenuItem sellCustomerHistory = new MenuItem("Cus History");
        MenuItem logout = new MenuItem("Log Out");

        add.setOnAction(event -> {
            try{
                TabPane add_tab = FXMLLoader.load(getClass().getResource("/add/add.fxml"));
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
                TabPane edit_tab = FXMLLoader.load(getClass().getResource("/edit/edit.fxml"));
                boolean sameView = Main.getRoot().getCenter().getId().equals(edit_tab.getId());
                if(!sameView){
                    Main.getRoot().setCenter(edit_tab);
                }

            }catch (Exception e){
                System.out.println("error " + e);
            }
        });

        sell.setOnAction(event -> {
            try{
                Parent parent = FXMLLoader.load(getClass().getResource("/action/sell/sellView.fxml"));
                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setId("scroll");
                scrollPane.setContent(parent);

                boolean sameView = Main.getRoot().getCenter().getId().equals(scrollPane.getId());
                if(!sameView){
                    Main.getRoot().setCenter(scrollPane);
                }

            }catch (Exception e){
                System.out.println("error " + e);
            }
        });

        transaction.setOnAction(event -> {
            try{
                Parent parent = FXMLLoader.load(getClass().getResource("/action/transaction/addTransaction.fxml"));
                boolean sameView = Main.getRoot().getCenter().getId().equals(parent.getId());

                if(!sameView){
                    Main.getRoot().setCenter(parent);
                }

            }catch (Exception e){
                System.out.println("error " + e.getStackTrace());
            }
        });

        sellCustomerHistory.setOnAction(event -> {
            try{
                Parent parent = FXMLLoader.load(getClass().getResource("/history/sell/customer/customerHistory.fxml"));
                System.out.println("worked");
                boolean sameView = Main.getRoot().getCenter().getId().equals(parent.getId());

                if(!sameView){
                    Main.getRoot().setCenter(parent);
                }

            }catch (Exception e){
                System.out.println("Main class: error " + e);
            }
        });

        logout.setOnAction(event -> {
            try{
                Parent loginView = FXMLLoader.load(getClass().getResource("/login/login.fxml"));
                boolean sameView = Main.getRoot().getCenter().getId().equals(loginView.getId());
                if(!sameView){
                    Main.getRoot().setCenter(loginView);
                    Main.getRoot().setTop(null);
                }

            }catch (Exception e){
                System.out.println("error " + e);
            }
        });

        views.getItems().addAll(add,edit,sell,transaction,sellCustomerHistory, logout);

        Menu help = new Menu("Help");
        MenuItem about = new MenuItem("About");
        help.getItems().addAll(about);

        menuBar.getMenus().addAll(views,help);
        menuBar.setPrefHeight(30);


        return menuBar;
    }
}

