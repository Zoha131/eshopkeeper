package action.transaction;

import data_helper.DataHelper;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.Customer;
import model.CustomerStringConverter;
import model.Transaction;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.time.LocalDate;
import java.util.UUID;

public class AddTransaction {
    @FXML private ComboBox<String> trType;
    @FXML private TextField nameTxt,adrsTxt, trIdTxt, phnTxt, amntTxt,takenbyTxt;
    @FXML private DatePicker datePick;
    @FXML private Button saveBtn;

    private ObservableList<Customer> dataCustomer;
    private ObservableList<String> dataCustomerName;
    private CustomerStringConverter customerStringConverter;

    private Transaction transaction;
    private Customer customer;

    public void initialize(){
        dataCustomer = DataHelper.getCustomer();
        dataCustomerName = FXCollections.observableArrayList();
        for(Customer cus: dataCustomer){
            dataCustomerName.add(cus.getName());
        }
        customerStringConverter = new CustomerStringConverter(dataCustomer);

        trType.getItems().setAll("Customer", "Supplier");
        trType.setValue("Customer");

        AutoCompletionBinding<String> binding = TextFields.bindAutoCompletion(nameTxt, dataCustomerName);
        binding.setOnAutoCompleted(event -> {
            customer = customerStringConverter.fromString(event.getCompletion());

            //building transaction object
            transaction = new Transaction();
            transaction.setCustomerId(customer.getId());
            transaction.setTransactionId(UUID.randomUUID().toString());

            //update the view with the customer
            adrsTxt.setText(customer.getAddress());
            phnTxt.setText(customer.getPhone());
            trIdTxt.setText(transaction.getTransactionId());
            datePick.setValue(LocalDate.now());
        });

        saveBtn.disableProperty().bind(Bindings.isEmpty(nameTxt.textProperty()).or(Bindings.isEmpty(takenbyTxt.textProperty())).or(Bindings.isEmpty(amntTxt.textProperty())));

        saveBtn.setOnAction(event -> {
            transaction.setTakenBy(takenbyTxt.getText());
            transaction.setDate(datePick.getEditor().getText());
            transaction.setAmount(Double.parseDouble(amntTxt.getText()));
            //todo-me to add validation rule

            boolean isSaved = DataHelper.insertSellTransaction(transaction);
            if(isSaved){
                clear();
                nameTxt.requestFocus();
            }
        });
    }

    private void clear(){
        nameTxt.clear();
        adrsTxt.clear();
        phnTxt.clear();
        trIdTxt.clear();
        datePick.getEditor().clear();
        amntTxt.clear();
    }
}
