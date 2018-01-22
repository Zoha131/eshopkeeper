package com.zoha131.eshopkeeper.history.transaction;

import com.zoha131.eshopkeeper.converter.MyDateConverter;
import com.zoha131.eshopkeeper.converter.ModelStringConverter;
import com.zoha131.eshopkeeper.data_helper.DataHelper;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.zoha131.eshopkeeper.model.Customer;
import com.zoha131.eshopkeeper.model.Supplier;
import com.zoha131.eshopkeeper.model.Transaction;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.time.LocalDate;


public class TransactionHistory {
    @FXML TextField nameTxt;
    @FXML DatePicker fromDatePic, toDatePic;
    @FXML TableView<Transaction> mainTbl;
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

    private ObservableList<Transaction> dataTransaction;
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
                    dataTransaction = DataHelper.getSellTransaction(customer.getId());
                    mainTbl.setItems(dataTransaction);
                    updateSummery(dataTransaction);
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
                    dataTransaction = DataHelper.getPurchaseTransaction(supplier.getId());
                    mainTbl.setItems(dataTransaction);
                    updateSummery(dataTransaction);
                });
            }
        });

        //setting go btn binding
        goBtn.disableProperty().bind(Bindings.isEmpty(nameTxt.textProperty()).or(Bindings.isEmpty(fromDatePic.getEditor().textProperty())).or(Bindings.isEmpty(toDatePic.getEditor().textProperty())));
        goBtn.setOnAction(event -> {
            LocalDate from = fromDatePic.getValue();
            LocalDate to = toDatePic.getValue();
            ObservableList<Transaction> filteredData = FXCollections.observableArrayList();
            for(Transaction transaction: dataTransaction){
                if(transaction.getLocalDate().compareTo(from)>=0 && transaction.getLocalDate().compareTo(to)<=0){
                    filteredData.add(transaction);
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

    private void updateSummery(ObservableList<Transaction> transactions){

        if(transactions == null){
            tProdLbl.setText(String.format("%-20s %d","No of Transaction:",0));
            tQuanLbl.setText(String.format("%-20s %f","Total Payable:",0f));
            tAmnLbl.setText (String.format("%-20s %f","Total Paid:",0f));
            return;
        }

        double payable = 0;
        double paid = 0;

        for(Transaction transaction: transactions){
            payable += transaction.getTotal();
            paid += transaction.getPaid();
        }

        tProdLbl.setText(String.format("%-20s %d","No of Transaction:",transactions.size()));
        tQuanLbl.setText(String.format("%-20s %f","Total Payable:",payable));
        tAmnLbl.setText (String.format("%-20s %f","Total Paid:",paid));
    }

    private void clear(){
        mainTbl.setItems(null);
        nameTxt.clear();
        toDatePic.getEditor().clear();
        fromDatePic.getEditor().clear();
        updateSummery(null);
    }

    private void addColumn(){

        TableColumn<Transaction, String> date = new TableColumn<>("Date");
        date.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Transaction, Double> total = new TableColumn<>("Payable");
        total.setCellValueFactory(new PropertyValueFactory<>("total"));

        TableColumn<Transaction, Double> paid = new TableColumn<>("Paid");
        paid.setCellValueFactory(new PropertyValueFactory<>("paid"));

        TableColumn<Transaction, String> takenBy = new TableColumn<>("Taken By");
        takenBy.setCellValueFactory(new PropertyValueFactory<>("takenBy"));

        TableColumn<Transaction, String> transactionId = new TableColumn<>("Transaction ID");
        transactionId.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        transactionId.setMinWidth(300);


        mainTbl.getColumns().addAll(date,total,paid, takenBy, transactionId);
    }
}
/*
**todo-me If the name of two or more customer is same then always the firs customer is selected. This is a bug. need to be fixed. SellView nad PurchaseView have also this but
*/