package edit.product;

import data_helper.DataHelper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import model.Product;

public class EditProduct {
    @FXML private BorderPane proBorderpane;
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




        //tried to add custom selection logic
        tsm.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("tsm:: selected ");
            tsm.select(newValue.getId());
        });
        //todo-me to add selection logic

        proBorderpane.setCenter(tableView);
    }

    private void setColumn(TableView<Product> tableView){
        TableColumn<Product, Integer> id = new TableColumn<>("ID");
        id.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
        id.setOnEditStart(event -> {
            System.out.println("started "+ event.getTablePosition().getRow());
            tsm.select(event.getTablePosition().getRow());
        });

        TableColumn<Product, String> name = new TableColumn<>("Name");
        name.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        name.setCellFactory(TextFieldTableCell.<Product>forTableColumn());

        //tried to add selection logic
        name.setOnEditCommit(event -> {
            System.out.println("edited "+event.getTableColumn().getText());
        });
        //name.setStyle( "-fx-alignment: CENTER-LEFT;");



        TableColumn<Product, String> code = new TableColumn<>("Code");
        code.setCellValueFactory(new PropertyValueFactory<Product, String>("code"));
        code.setCellFactory(TextFieldTableCell.<Product>forTableColumn());

        TableColumn<Product, String> company = new TableColumn<>("Company");
        company.setCellValueFactory(new PropertyValueFactory<Product, String>("company"));
        company.setCellFactory(TextFieldTableCell.<Product>forTableColumn());


        TableColumn<Product, Double> prate = new TableColumn<>("Purchase Rate");
        prate.setCellValueFactory(new PropertyValueFactory<Product, Double>("prate"));
        prate.setCellFactory(TextFieldTableCell.<Product, Double>forTableColumn(new DoubleStringConverter()));

        TableColumn<Product, Double> wrate = new TableColumn<>("Wholesale Rate");
        wrate.setCellValueFactory(new PropertyValueFactory<Product, Double>("wrate"));
        wrate.setCellFactory(TextFieldTableCell.<Product, Double>forTableColumn(new DoubleStringConverter()));

        TableColumn<Product, Double> rrate = new TableColumn<>("Retail Rate");
        rrate.setCellValueFactory(new PropertyValueFactory<Product, Double>("rrate"));
        rrate.setCellFactory(TextFieldTableCell.<Product, Double>forTableColumn(new DoubleStringConverter()));

        TableColumn<Product, Integer> stock = new TableColumn<>("Stock");
        stock.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));
        stock.setCellFactory(TextFieldTableCell.<Product, Integer>forTableColumn(new IntegerStringConverter()));

        tableView.getColumns().addAll(id,name,code,company,prate,wrate,rrate,stock);
    }
}
