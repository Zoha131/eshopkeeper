package action.transaction;

import data_helper.DataHelper;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.Customer;
import converter.ModelStringConverter;
import model.Supplier;
import model.Transaction;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.time.LocalDate;
import java.util.UUID;

public class AddTransaction {
    @FXML private ComboBox<String> trType;
    @FXML private TextField nameTxt,adrsTxt, trIdTxt, phnTxt, amntTxt,takenbyTxt;
    @FXML private DatePicker datePick;
    @FXML private Button saveBtn;

    private ObservableList<Customer> dataCustomer;
    private ObservableList<String> dataCustomerName;
    private ModelStringConverter<Customer> customerStringConverter;

    private ObservableList<Supplier> dataSupplier;
    private ObservableList<String> dataSupplierName;
    private ModelStringConverter<Supplier> supplierModelStringConverter;

    private Transaction transaction;
    private Customer customer;
    private Supplier supplier;
    private ValidationSupport validation;

    public void initialize(){
        dataCustomer = DataHelper.getCustomer();
        customerStringConverter = new ModelStringConverter<>(dataCustomer);
        dataCustomerName = FXCollections.observableArrayList();
        for(Customer cus: dataCustomer){
            dataCustomerName.add(customerStringConverter.toString(cus));
        }

        dataSupplier = DataHelper.getSupplier();
        supplierModelStringConverter = new ModelStringConverter<>(dataSupplier);
        dataSupplierName = FXCollections.observableArrayList();
        for(Supplier sup: dataSupplier){
            dataSupplierName.add(supplierModelStringConverter.toString(sup));
        }


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

        validation = new ValidationSupport();
        Platform.runLater(()->{
            validation.registerValidator(nameTxt, Validator.createEmptyValidator("This field must not be empty"));
            validation.registerValidator(takenbyTxt, Validator.createEmptyValidator("This field must not be empty"));
            validation.registerValidator(amntTxt, Validator.createRegexValidator("Must be a number", "[0-9]+\\.?[0-9]*", Severity.ERROR));
        });

        saveBtn.disableProperty().bind(validation.invalidProperty());
        saveBtn.setOnAction(event -> {
            transaction.setTakenBy(takenbyTxt.getText());
            transaction.setDate(datePick.getEditor().getText());
            transaction.setPaid(Double.parseDouble(amntTxt.getText()));

            Double due = customer.getDue();
            due = due-transaction.getPaid();
            customer.setDue(due);

            boolean isSaved = DataHelper.insertSellTransaction(transaction);
            boolean isUpdated = DataHelper.updateData("customer", "due", customer.getDue(), customer.getId());
            if(isSaved && isUpdated){
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
        amntTxt.clear();
    }
}
