package edit.customer;

import add.customer.AddCustomer;
import data_helper.DataHelper;
import edit.EditUtil;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.util.converter.DoubleStringConverter;
import model.Customer;
import model.Product;

public class EditCustomer {

    @FXML private BorderPane cusBorderpane;
    @FXML private Button addbtn, dltbtn;
    TableView.TableViewSelectionModel<Customer> tsm;

    public void initialize(){
        ObservableList<Customer> data = DataHelper.getCustomer();
        TableView<Customer> tableView = new TableView<>(data);
        setColumn(tableView);
        tableView.setEditable(true);
        tableView.setFixedCellSize(30);
        tsm = tableView.getSelectionModel();

        //select the whole row when first time clicked the id column
        EditUtil.SelectAllCol(tableView);

        dltbtn.setOnAction(event -> {
            if(tsm.getSelectedCells().size() >= 5){
                Customer cus = tsm.getSelectedItem();
                boolean deleted = DataHelper.deleteData("customer", cus.getId());
                if(deleted){
                    TablePosition<Product, Integer> pos = tsm.getSelectedCells().get(0);
                    tableView.getItems().remove(pos.getRow());
                    System.out.println("EditProduct::data deleted "+cus.getId());
                }

            }
        });

        addbtn.setOnAction(event -> {
            Customer customer = new Customer();
            boolean isInserted = DataHelper.insertCustomer(customer);
            if(isInserted){
                tableView.getItems().add(customer);
            }
        });
        addbtn.setOpacity(0);
        //todo-me to add Add Button in a user friendly way.

        cusBorderpane.setCenter(tableView);
    }

    private void setColumn(TableView<Customer> tableView){
        TableColumn<Customer, Integer> id = new TableColumn<>("ID");
        id.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("id"));


        TableColumn<Customer, String> name = new TableColumn<>("Name");
        name.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
        name.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());
        //added Id. this Id will help to update data. Id will act like column name
        name.setId("name");
        name.setOnEditCommit(event ->{
            EditUtil.updateString(event, "customer");
        });

        TableColumn<Customer, String> address = new TableColumn<>("Address");
        address.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        address.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());
        address.setId("address");
        address.setOnEditCommit(event ->{
            EditUtil.updateString(event, "customer");
        });

        TableColumn<Customer, String> phone = new TableColumn<>("Phone");
        phone.setCellValueFactory(new PropertyValueFactory<Customer, String>("phone"));
        phone.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());
        phone.setId("phone");
        phone.setOnEditCommit(event ->{
            EditUtil.updateString(event, "customer");
        });

        TableColumn<Customer, String> email = new TableColumn<>("Email");
        email.setCellValueFactory(new PropertyValueFactory<Customer, String>("email"));
        email.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());
        email.setId("email");
        email.setOnEditCommit(event ->{
            EditUtil.updateString(event, "customer");
        });

        TableColumn<Customer, String> type = new TableColumn<>("type");
        type.setCellValueFactory(new PropertyValueFactory<Customer, String>("type"));
        type.setCellFactory(ComboBoxTableCell.<Customer,String>forTableColumn(AddCustomer.CusType.WholeSale.toString(), AddCustomer.CusType.Retailer.toString()));
        type.setId("type");
        type.setMinWidth(100);
        type.setOnEditCommit(event ->{
            EditUtil.updateString(event, "customer");
        });

        TableColumn<Customer, String> store = new TableColumn<>("Store");
        store.setCellValueFactory(new PropertyValueFactory<Customer, String>("store"));
        store.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());
        store.setId("store");
        store.setOnEditCommit(event ->{
            EditUtil.updateString(event, "customer");
        });

        TableColumn<Customer, Double> due = new TableColumn<>("Due");
        due.setCellValueFactory(new PropertyValueFactory<Customer, Double>("due"));
        due.setCellFactory(TextFieldTableCell.<Customer, Double>forTableColumn(new DoubleStringConverter()));
        due.setId("due");
        due.setOnEditCommit(event ->{
            EditUtil.updateDouble(event, "customer");
        });

        tableView.getColumns().addAll(id,name,address,phone,email,type,store,due);
    }
}
