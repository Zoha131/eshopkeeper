package history.sell.customer;

import data_helper.DataHelper;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.util.ArrayList;
import java.util.List;


public class CustomerHistory {
    @FXML TextField nameTxt;
    @FXML DatePicker fromDatePic, toDatePic;
    @FXML TableView<SellHistory> mainTbl;
    @FXML Label tProdLbl,tQuanLbl,tAmnLbl;
    @FXML Button goBtn;

    private ObservableList<Customer> dataCustomer;
    private ObservableList<String> dataCustomerName;
    private ObservableList<SellHistory> dataSellHistory;
    private ModelStringConverter<Customer> customerStringConverter;
    private Customer customer;
    private DateStringConverter dateConverter;

    private List<Integer> products;
    private int tQuantity;
    private double tAmount;

    public void initialize(){
        dataCustomer = DataHelper.getCustomer();
        dataCustomerName = FXCollections.observableArrayList();
        for(Customer cus: dataCustomer){
            dataCustomerName.add(cus.getName());
        }
        customerStringConverter = new ModelStringConverter(dataCustomer);

        //setting go btn binding
        //goBtn.disableProperty().bind(Bindings.isEmpty(nameTxt.textProperty()).or(Bindings.isEmpty(fromDatePic.getEditor().textProperty())).or(Bindings.isEmpty(toDatePic.getEditor().textProperty())));
        goBtn.setDisable(true);
        //todo-me to add logic to filter result table

        //setting converter to the Datepicker
        dateConverter = new DateStringConverter();
        fromDatePic.setConverter(dateConverter);
        toDatePic.setConverter(dateConverter);

        //setting tableview
        addColumn();

        //binding the textview to make name searchable
        AutoCompletionBinding<String> binding = TextFields.bindAutoCompletion(nameTxt, dataCustomerName);
        binding.setOnAutoCompleted(event -> {
            //setting items to table
            customer = customerStringConverter.fromString(event.getCompletion());
            dataSellHistory = DataHelper.getSellHistory(customer.getId());
            mainTbl.getItems().setAll(dataSellHistory);

            //
            products = new ArrayList<>();
            for(SellHistory sel: dataSellHistory){
                if(!products.contains(sel.getPrductId())){
                    products.add(sel.getPrductId());
                }
                tAmount += sel.getAmount();
                tQuantity += sel.getQuantity();
            }
            System.out.println(products.size());

            tProdLbl.setText("Total Product Types: "+products.size());
            tQuanLbl.setText("Total Quantity:      "+tQuantity);
            tAmnLbl.setText ("Total Amount:        "+tAmount);

        });
    }

    private void addColumn(){

        TableColumn<SellHistory, String> date = new TableColumn<>("Date");
        date.setCellValueFactory(new PropertyValueFactory<>("strDate"));

        TableColumn<SellHistory, String> name = new TableColumn<>("Product");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<SellHistory, String> company = new TableColumn<>("Company");
        company.setCellValueFactory(new PropertyValueFactory<>("company"));

        TableColumn<SellHistory, Double> rate = new TableColumn<>("Rate");
        rate.setCellValueFactory(new PropertyValueFactory<>("rate"));

        TableColumn<SellHistory, Integer> quantity = new TableColumn<>("Quantity");
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<SellHistory, Double> amount = new TableColumn<>("Amount");
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<SellHistory, String> soldBy = new TableColumn<>("Sold By");
        soldBy.setCellValueFactory(new PropertyValueFactory<>("soldBy"));

        mainTbl.getColumns().addAll(date,name,company,rate,quantity,amount,soldBy);
    }
}
