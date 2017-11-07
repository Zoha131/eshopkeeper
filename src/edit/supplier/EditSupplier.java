package edit.supplier;

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
import model.Customer;
import model.Supplier;

public class EditSupplier {
    @FXML private BorderPane supBorderpane;

    public void initialize(){
        ObservableList<Supplier> data = DataHelper.getSupplier();
        TableView<Supplier> tableView = new TableView<>(data);
        setColumn(tableView);
        tableView.setEditable(true);
        tableView.setFixedCellSize(30);
        TableView.TableViewSelectionModel<Supplier> tsm = tableView.getSelectionModel();
        tsm.setCellSelectionEnabled(true);
        tsm.setSelectionMode(SelectionMode.MULTIPLE);

        supBorderpane.setCenter(tableView);
    }

    private void setColumn(TableView<Supplier> tableView){
        TableColumn<Supplier, Integer> id = new TableColumn<>("ID");
        id.setCellValueFactory(new PropertyValueFactory<Supplier, Integer>("id"));

        TableColumn<Supplier, String> name = new TableColumn<>("Name");
        name.setCellValueFactory(new PropertyValueFactory<Supplier, String>("name"));
        name.setCellFactory(TextFieldTableCell.<Supplier>forTableColumn());

        TableColumn<Supplier, String> address = new TableColumn<>("Address");
        address.setCellValueFactory(new PropertyValueFactory<Supplier, String>("address"));
        address.setCellFactory(TextFieldTableCell.<Supplier>forTableColumn());

        TableColumn<Supplier, String> phone = new TableColumn<>("Phone");
        phone.setCellValueFactory(new PropertyValueFactory<Supplier, String>("phone"));
        phone.setCellFactory(TextFieldTableCell.<Supplier>forTableColumn());

        TableColumn<Supplier, String> email = new TableColumn<>("Email");
        email.setCellValueFactory(new PropertyValueFactory<Supplier, String>("email"));
        email.setCellFactory(TextFieldTableCell.<Supplier>forTableColumn());

        TableColumn<Supplier, String> store = new TableColumn<>("Store");
        store.setCellValueFactory(new PropertyValueFactory<Supplier, String>("store"));
        store.setCellFactory(TextFieldTableCell.<Supplier>forTableColumn());

        TableColumn<Supplier, Double> due = new TableColumn<>("Due");
        due.setCellValueFactory(new PropertyValueFactory<Supplier, Double>("due"));
        due.setCellFactory(TextFieldTableCell.<Supplier, Double>forTableColumn(new DoubleStringConverter()));

        tableView.getColumns().addAll(id,name,store,address,phone,email,due);
    }
}
