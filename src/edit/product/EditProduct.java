package edit.product;

import data_helper.DataHelper;
import edit.EditUtil;
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
    private Product addProduct;
    private ObservableList<Product> data;

    public void initialize(){
        data = DataHelper.getProduct();
        TableView<Product> tableView = new TableView<>(data);
        tableView.setEditable(true);
        tableView.setFixedCellSize(30);
        tsm = tableView.getSelectionModel();
        setColumn(tableView);

        //select the whole row when first time clicked the id column
        EditUtil.SelectAllCol(tableView);

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
            Product product = new Product();
            boolean isInserted = DataHelper.insertProduct(product);

            if(isInserted){
                data = DataHelper.getProduct();
                tableView.getItems().setAll(data);
                //todo-me add logic to restrict user adding multiple template item
            }
        });
    }

    private void setColumn(TableView<Product> tableView){
        TableColumn<Product, Integer> id = new TableColumn<>("ID");
        id.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));

        TableColumn<Product, String> name = new TableColumn<>("Name");
        name.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        name.setCellFactory(TextFieldTableCell.<Product>forTableColumn());
        //added Id. this Id will help to update data. Id will act like column name
        name.setId("name");
        name.setOnEditCommit(event ->{
            EditUtil.updateString(event, "product");
        });


        TableColumn<Product, String> code = new TableColumn<>("Code");
        code.setCellValueFactory(new PropertyValueFactory<Product, String>("code"));
        code.setCellFactory(TextFieldTableCell.<Product>forTableColumn());
        code.setId("code");
        code.setOnEditCommit(event ->{
            EditUtil.updateString(event, "product");
        });

        TableColumn<Product, String> company = new TableColumn<>("Company");
        company.setCellValueFactory(new PropertyValueFactory<Product, String>("company"));
        company.setCellFactory(TextFieldTableCell.<Product>forTableColumn());
        company.setId("company");
        company.setOnEditCommit(event ->{
            EditUtil.updateString(event, "product");
        });


        TableColumn<Product, Double> prate = new TableColumn<>("Purchase Rate");
        prate.setCellValueFactory(new PropertyValueFactory<Product, Double>("prate"));
        prate.setCellFactory(TextFieldTableCell.<Product, Double>forTableColumn(new DoubleStringConverter()));
        prate.setId("prate");
        prate.setOnEditCommit(event ->{
            EditUtil.updateDouble(event, "product");
        });

        TableColumn<Product, Double> wrate = new TableColumn<>("Wholesale Rate");
        wrate.setCellValueFactory(new PropertyValueFactory<Product, Double>("wrate"));
        wrate.setCellFactory(TextFieldTableCell.<Product, Double>forTableColumn(new DoubleStringConverter()));
        wrate.setId("wrate");
        wrate.setOnEditCommit(event ->{
            EditUtil.updateDouble(event, "product");
        });

        TableColumn<Product, Double> rrate = new TableColumn<>("Retail Rate");
        rrate.setCellValueFactory(new PropertyValueFactory<Product, Double>("rrate"));
        rrate.setCellFactory(TextFieldTableCell.<Product, Double>forTableColumn(new DoubleStringConverter()));
        rrate.setId("rrate");
        rrate.setOnEditCommit(event ->{
            EditUtil.updateDouble(event, "product");
        });

        TableColumn<Product, Integer> stock = new TableColumn<>("Stock");
        stock.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));
        stock.setCellFactory(TextFieldTableCell.<Product, Integer>forTableColumn(new IntegerStringConverter()));
        stock.setId("stock");
        stock.setOnEditCommit(event ->{
            EditUtil.updateInt(event, "product");
        });

        tableView.getColumns().addAll(id,name,code,company,prate,wrate,rrate,stock);
    }


    //todo-me to clean this class and to write a note for future.
}

