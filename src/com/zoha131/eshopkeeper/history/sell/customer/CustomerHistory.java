package com.zoha131.eshopkeeper.history.sell.customer;

import com.zoha131.eshopkeeper.converter.MyDateConverter;
import com.zoha131.eshopkeeper.converter.ModelStringConverter;
import com.zoha131.eshopkeeper.data_helper.DataHelper;
import com.zoha131.eshopkeeper.model.*;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class CustomerHistory {
    @FXML TextField nameTxt;
    @FXML DatePicker fromDatePic, toDatePic;
    @FXML TableView<SellHistory> mainTbl;
    @FXML Label tProdLbl,tQuanLbl,tAmnLbl;
    @FXML Button goBtn;
    @FXML ComboBox<String> typeCombo;

    private ObservableList<Customer> dataCustomer;
    private ObservableList<String> dataCustomerName;
    private ModelStringConverter<Customer> customerStringConverter;
    AutoCompletionBinding<String> customerBinding;

    private ObservableList<Supplier> dataSupplier;
    private ObservableList<String> dataSupplierName;
    private ModelStringConverter<Supplier> supplierModelStringConverter;
    AutoCompletionBinding<String> supplierBinding;

    private ObservableList<SellHistory> dataSellHistory;
    private MyDateConverter dateConverter;



    public void initialize(){

        //user must have to select combobox before typing name
        nameTxt.setDisable(true);

        //setting combobox
        typeCombo.getItems().setAll("Customer", "Supplier");
        typeCombo.setOnAction(event -> {
            nameTxt.setDisable(false);
            clear();

            if(typeCombo.getSelectionModel().getSelectedItem().equals("Customer")){
                dataCustomer = DataHelper.getCustomer();
                customerStringConverter = new ModelStringConverter<>(dataCustomer);
                dataCustomerName = FXCollections.observableArrayList();
                for(Customer cus: dataCustomer){
                    dataCustomerName.add(customerStringConverter.toString(cus));
                }

                //removing existing binding before adding new bindings
                if(supplierBinding != null) supplierBinding.dispose();

                //binding the textview to make name searchable
                customerBinding = TextFields.bindAutoCompletion(nameTxt, dataCustomerName);
                customerBinding.setOnAutoCompleted(BindingEvent -> {
                    //setting items to table
                    Customer customer = customerStringConverter.fromString(BindingEvent.getCompletion());
                    dataSellHistory = DataHelper.getSellHistory(customer.getId());
                    mainTbl.setItems(dataSellHistory);
                    updateSummery(dataSellHistory);
                });

            }
            else {
                dataSupplier = DataHelper.getSupplier();
                supplierModelStringConverter = new ModelStringConverter<>(dataSupplier);
                dataSupplierName = FXCollections.observableArrayList();
                for(Supplier sup: dataSupplier){
                    dataSupplierName.add(supplierModelStringConverter.toString(sup));
                }

                //removing existing binding before adding new bindings
                if(customerBinding != null) customerBinding.dispose();

                //binding the textview to make name searchable
                supplierBinding = TextFields.bindAutoCompletion(nameTxt, dataSupplierName);
                supplierBinding.setOnAutoCompleted(BindingEvent -> {
                    //setting items to table
                    Supplier supplier = supplierModelStringConverter.fromString(BindingEvent.getCompletion());
                    dataSellHistory = DataHelper.getPurchaseHistory(supplier.getId());
                    mainTbl.setItems(dataSellHistory);
                    updateSummery(dataSellHistory);
                });
            }
        });

        //setting go btn binding
        goBtn.disableProperty().bind(Bindings.isEmpty(nameTxt.textProperty()).or(Bindings.isEmpty(fromDatePic.getEditor().textProperty())).or(Bindings.isEmpty(toDatePic.getEditor().textProperty())));
        goBtn.setOnAction(event -> {
            LocalDate from = fromDatePic.getValue();
            LocalDate to = toDatePic.getValue();
            ObservableList<SellHistory> filteredData = FXCollections.observableArrayList();
            for(SellHistory history: dataSellHistory){
                if(history.getDate().compareTo(from)>=0 && history.getDate().compareTo(to)<=0){
                    filteredData.add(history);
                }
            }
            mainTbl.setItems(filteredData);
            updateSummery(filteredData);
        });

        //setting com.zoha131.eshopkeeper.converter to the Datepicker
        dateConverter = new MyDateConverter();
        fromDatePic.setConverter(dateConverter);
        toDatePic.setConverter(dateConverter);

        //setting tableview
        addColumn();
    }

    private void updateSummery(ObservableList<SellHistory> sellHistories){

        if(sellHistories == null){
            tProdLbl.setText("Total Product Types: 0");
            tQuanLbl.setText("Total Quantity:      0");
            tAmnLbl.setText ("Total Amount:        0");
            return;
        }

        List<Integer> products= new ArrayList<>();
        int tQuantity=0;
        double tAmount=0;

        for(SellHistory sel: sellHistories){
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
    }

    private void clear(){
        mainTbl.setItems(null);
        nameTxt.clear();
        toDatePic.getEditor().clear();
        fromDatePic.getEditor().clear();
        updateSummery(null);
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
/*
**todo-me If the name of two or more customer is same then always the firs customer is selected. This is a bug. need to be fixed. SellView nad PurchaseView have also this but
*/