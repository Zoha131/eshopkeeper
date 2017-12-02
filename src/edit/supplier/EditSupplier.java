package edit.supplier;

import data_helper.DataHelper;
import edit.EditUtil;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.util.converter.DoubleStringConverter;
import model.Customer;
import model.Product;
import model.Supplier;

public class EditSupplier {
    @FXML private BorderPane supBorderpane;
    @FXML private Button addbtn, dltbtn;

    TableView.TableViewSelectionModel<Supplier> tsm;

    public void initialize(){
        ObservableList<Supplier> data = DataHelper.getSupplier();
        TableView<Supplier> tableView = new TableView<>(data);
        setColumn(tableView);
        tableView.setEditable(true);
        tableView.setFixedCellSize(30);
        tsm = tableView.getSelectionModel();

        //select the whole row when first time clicked the id column
        EditUtil.SelectAllCol(tableView);

        dltbtn.setOnAction(event -> {
            if(tsm.getSelectedCells().size() >= 5){
                Supplier sup = tsm.getSelectedItem();
                boolean deleted = DataHelper.deleteData("supplier", sup.getId());
                if(deleted){
                    TablePosition<Product, Integer> pos = tsm.getSelectedCells().get(0);
                    tableView.getItems().remove(pos.getRow());
                    System.out.println("EditProduct::data deleted "+sup.getId());
                }

            }
        });

        addbtn.setOnAction(event -> {
            Supplier supplier = new Supplier();
            boolean isInserted = DataHelper.insertSupplier(supplier);
            if(isInserted){
                tableView.getItems().add(supplier);
            }
        });

        supBorderpane.setCenter(tableView);
    }

    private void setColumn(TableView<Supplier> tableView){
        TableColumn<Supplier, Integer> id = new TableColumn<>("ID");
        id.setCellValueFactory(new PropertyValueFactory<Supplier, Integer>("id"));

        TableColumn<Supplier, String> name = new TableColumn<>("Name");
        name.setCellValueFactory(new PropertyValueFactory<Supplier, String>("name"));
        name.setCellFactory(TextFieldTableCell.<Supplier>forTableColumn());
        //added Id. this Id will help to update data. Id will act like column name
        name.setId("name");
        name.setOnEditCommit(event ->{
            EditUtil.updateString(event, "supplier");
        });

        TableColumn<Supplier, String> address = new TableColumn<>("Address");
        address.setCellValueFactory(new PropertyValueFactory<Supplier, String>("address"));
        address.setCellFactory(TextFieldTableCell.<Supplier>forTableColumn());
        address.setId("address");
        address.setOnEditCommit(event ->{
            EditUtil.updateString(event, "supplier");
        });

        TableColumn<Supplier, String> phone = new TableColumn<>("Phone");
        phone.setCellValueFactory(new PropertyValueFactory<Supplier, String>("phone"));
        phone.setCellFactory(TextFieldTableCell.<Supplier>forTableColumn());
        phone.setId("phone");
        phone.setOnEditCommit(event ->{
            EditUtil.updateString(event, "supplier");
        });

        TableColumn<Supplier, String> email = new TableColumn<>("Email");
        email.setCellValueFactory(new PropertyValueFactory<Supplier, String>("email"));
        email.setCellFactory(TextFieldTableCell.<Supplier>forTableColumn());
        email.setId("email");
        email.setOnEditCommit(event ->{
            EditUtil.updateString(event, "supplier");
        });

        TableColumn<Supplier, String> store = new TableColumn<>("Store");
        store.setCellValueFactory(new PropertyValueFactory<Supplier, String>("store"));
        store.setCellFactory(TextFieldTableCell.<Supplier>forTableColumn());
        store.setId("store");
        store.setOnEditCommit(event ->{
            EditUtil.updateString(event, "supplier");
        });

        TableColumn<Supplier, Double> due = new TableColumn<>("Due");
        due.setCellValueFactory(new PropertyValueFactory<Supplier, Double>("due"));
        due.setCellFactory(TextFieldTableCell.<Supplier, Double>forTableColumn(new DoubleStringConverter()));
        due.setId("due");
        due.setOnEditCommit(event ->{
            EditUtil.updateDouble(event, "supplier");
        });

        tableView.getColumns().addAll(id,name,store,address,phone,email,due);
    }
}
