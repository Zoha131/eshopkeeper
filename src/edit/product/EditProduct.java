package edit.product;

import data_helper.DataHelper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import model.Product;

public class EditProduct {
    @FXML private BorderPane proBorderpane;
    @FXML private Button addbtn, dltbtn;
    private TableView.TableViewSelectionModel<Product> tsm;

    public void initialize(){
        ObservableList<Product> data = DataHelper.getProduct();
        TableView<Product> tableView = new TableView<>(data);
        tableView.setEditable(true);
        tableView.setFixedCellSize(30);
        tsm = tableView.getSelectionModel();
        tsm.setCellSelectionEnabled(true);
        tsm.setSelectionMode(SelectionMode.MULTIPLE);
        setColumn(tableView);

        //select the whole row when first time clicked the id column
        tableView.setOnMousePressed(event -> {
            if(tsm.getSelectedCells().size() == 1){
                TablePosition<Product, Integer> pos = tsm.getSelectedCells().get(0);
                if(pos.getColumn()==0)tsm.select(pos.getRow());
            }
        });

        proBorderpane.setCenter(tableView);

        dltbtn.setOnAction(event -> {
            if(tsm.getSelectedCells().size() >= 5){
                Product prod = tsm.getSelectedItem();
                boolean deleted = DataHelper.deleteData("product", prod.getId());
                if(deleted){
                    TablePosition<Product, Integer> pos = tsm.getSelectedCells().get(0);
                    tableView.getItems().remove(pos.getRow());
                    System.out.println("EditProduct::data deleted "+prod.getId());
                }

            }
        });
        addbtn.setOnAction(event -> {
            Product product = new Product(0,0,"product","404","company",0,0,0);
            boolean isInserted = DataHelper.insertProduct(product);
            if(isInserted){
                tableView.getItems().add(product);
            }
        });
    }

    private void setColumn(TableView<Product> tableView){
        TableColumn<Product, Integer> id = new TableColumn<>("ID");
        id.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
        //select the whole row when double clicked the id column
        id.setOnEditStart(event -> {
            if(tsm.getSelectedCells().size() == 1){
                TablePosition<Product, Integer> pos = tsm.getSelectedCells().get(0);
                if(pos.getColumn()==0)tsm.select(pos.getRow());
            }
        });

        TableColumn<Product, String> name = new TableColumn<>("Name");
        name.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        name.setCellFactory(TextFieldTableCell.<Product>forTableColumn());
        name.setId("name");

        //tried to add selection logic
        name.setOnEditCommit(EditProduct::updateString);
        //name.setStyle( "-fx-alignment: CENTER-LEFT;");



        TableColumn<Product, String> code = new TableColumn<>("Code");
        code.setCellValueFactory(new PropertyValueFactory<Product, String>("code"));
        code.setCellFactory(TextFieldTableCell.<Product>forTableColumn());
        code.setId("code");
        code.setOnEditCommit(EditProduct::updateString);

        TableColumn<Product, String> company = new TableColumn<>("Company");
        company.setCellValueFactory(new PropertyValueFactory<Product, String>("company"));
        company.setCellFactory(TextFieldTableCell.<Product>forTableColumn());
        company.setId("company");
        company.setOnEditCommit(EditProduct::updateString);


        TableColumn<Product, Double> prate = new TableColumn<>("Purchase Rate");
        prate.setCellValueFactory(new PropertyValueFactory<Product, Double>("prate"));
        prate.setCellFactory(TextFieldTableCell.<Product, Double>forTableColumn(new DoubleStringConverter()));
        prate.setId("prate");
        prate.setOnEditCommit(EditProduct::updateDouble);

        TableColumn<Product, Double> wrate = new TableColumn<>("Wholesale Rate");
        wrate.setCellValueFactory(new PropertyValueFactory<Product, Double>("wrate"));
        wrate.setCellFactory(TextFieldTableCell.<Product, Double>forTableColumn(new DoubleStringConverter()));
        wrate.setId("wrate");
        wrate.setOnEditCommit(EditProduct::updateDouble);

        TableColumn<Product, Double> rrate = new TableColumn<>("Retail Rate");
        rrate.setCellValueFactory(new PropertyValueFactory<Product, Double>("rrate"));
        rrate.setCellFactory(TextFieldTableCell.<Product, Double>forTableColumn(new DoubleStringConverter()));
        rrate.setId("rrate");
        rrate.setOnEditCommit(EditProduct::updateDouble);

        TableColumn<Product, Integer> stock = new TableColumn<>("Stock");
        stock.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));
        stock.setCellFactory(TextFieldTableCell.<Product, Integer>forTableColumn(new IntegerStringConverter()));
        stock.setId("stock");
        stock.setOnEditCommit(EditProduct::updateInt);

        tableView.getColumns().addAll(id,name,code,company,prate,wrate,rrate,stock);
    }

    public static void updateString(TableColumn.CellEditEvent<Product, String> event){
        Product prod =event.getRowValue();
        boolean update = DataHelper.updateData("product",event.getTableColumn().getId(), event.getNewValue(),prod.getId());
        System.out.println("Data edited: "+update);
        if (!update){
            event.getTableView().getItems().set(event.getTablePosition().getRow(), prod);
        }
    }

    public static void updateDouble(TableColumn.CellEditEvent<Product, Double> event){
        Product prod =event.getRowValue();
        boolean update = DataHelper.updateData("product",event.getTableColumn().getId(), event.getNewValue(),prod.getId());
        System.out.println("Data edited: "+update);
        if (!update){
            event.getTableView().getItems().set(event.getTablePosition().getRow(), prod);
        }
    }

    public static void updateInt(TableColumn.CellEditEvent<Product, Integer> event){
        Product prod =event.getRowValue();
        boolean update = DataHelper.updateData("product",event.getTableColumn().getId(), event.getNewValue(),prod.getId());
        System.out.println("Data edited: "+update);
        if (!update){
            event.getTableView().getItems().set(event.getTablePosition().getRow(), prod);
        }
    }

    //todo-me to clean this class and to write a note for future.
}
