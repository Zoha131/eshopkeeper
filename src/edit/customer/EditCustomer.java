package edit.customer;

import data_helper.DataHelper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.util.converter.DoubleStringConverter;
import model.Customer;

public class EditCustomer {

    @FXML private BorderPane cusBorderpane;

    public void initialize(){
        ObservableList<Customer> data = DataHelper.getCustomer();
        TableView<Customer> tableView = new TableView<>(data);
        setColumn(tableView);
        tableView.setEditable(true);
        tableView.setFixedCellSize(30);
        TableView.TableViewSelectionModel<Customer> tsm = tableView.getSelectionModel();
        tsm.setCellSelectionEnabled(true);
        tsm.setSelectionMode(SelectionMode.MULTIPLE);

        cusBorderpane.setCenter(tableView);
    }

    private void setColumn(TableView<Customer> tableView){
        TableColumn<Customer, Integer> id = new TableColumn<>("ID");
        id.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("id"));

        TableColumn<Customer, String> name = new TableColumn<>("Name");
        name.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
        name.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());

        TableColumn<Customer, String> address = new TableColumn<>("Address");
        address.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        address.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());

        TableColumn<Customer, String> phone = new TableColumn<>("Phone");
        phone.setCellValueFactory(new PropertyValueFactory<Customer, String>("phone"));
        phone.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());

        TableColumn<Customer, String> email = new TableColumn<>("Email");
        email.setCellValueFactory(new PropertyValueFactory<Customer, String>("email"));
        email.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());

        TableColumn<Customer, String> type = new TableColumn<>("type");
        type.setCellValueFactory(new PropertyValueFactory<Customer, String>("type"));
        type.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());

        TableColumn<Customer, String> store = new TableColumn<>("Store");
        store.setCellValueFactory(new PropertyValueFactory<Customer, String>("store"));
        store.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());

        TableColumn<Customer, Double> due = new TableColumn<>("Due");
        due.setCellValueFactory(new PropertyValueFactory<Customer, Double>("due"));
        due.setCellFactory(TextFieldTableCell.<Customer, Double>forTableColumn(new DoubleStringConverter()));

        tableView.getColumns().addAll(id,name,address,phone,email,type,store,due);
    }
}
