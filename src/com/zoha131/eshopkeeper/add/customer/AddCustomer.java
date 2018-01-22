package com.zoha131.eshopkeeper.add.customer;

import com.zoha131.eshopkeeper.data_helper.DataHelper;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.zoha131.eshopkeeper.model.Customer;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.util.regex.Pattern;


public class AddCustomer {
    @FXML TextField nametext,phntext,emailtxt, strtxt, duetxt;
    @FXML ComboBox<String> typebox;
    @FXML TextArea adrstxt;
    @FXML Button add_btn;
    @FXML Label strlbl;

    private ValidationSupport validation;

    public void initialize(){
        typebox.getItems().addAll(CusType.WholeSale.toString(), CusType.Retailer.toString());
        typebox.setValue(CusType.Retailer.toString());
        BooleanBinding isStore = Bindings.equal(CusType.Retailer.toString(), typebox.valueProperty());


        /*
        * code to validate the form
        */
        validation = new ValidationSupport();
        Platform.runLater(()->{
            validation.registerValidator(nametext, Validator.createEmptyValidator("These fields must not be empty"));
            validation.registerValidator(adrstxt, Validator.createEmptyValidator("These fields must not be empty"));
            validation.registerValidator(phntext,  Validator.createRegexValidator("Must be a phn number", "\\+?[0-9]{11,13}$", Severity.ERROR));
            validation.registerValidator(emailtxt, Validator.createRegexValidator("Must be an email address", Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE), Severity.WARNING));
            validation.registerValidator(duetxt, Validator.createRegexValidator("Must be a Decimal", "[0-9]*\\.?[0-9]*", Severity.ERROR));
        });
        //register and deregister based on the value of combo-box
        isStore.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)-> {
            if(newValue){
                validation.registerValidator(strtxt, false, ( c, value) -> ValidationResult.fromErrorIf( c, "", false) );
            }
            else {
                validation.registerValidator(strtxt, Validator.createEmptyValidator("Practitioner(s) is required"));
            }

        });
        add_btn.disableProperty().bind(validation.invalidProperty());



        strlbl.disableProperty().bind(isStore);
        strtxt.disableProperty().bind(isStore);

        add_btn.setOnAction(event -> {
            String store = typebox.getValue().equals(CusType.WholeSale.toString())? strtxt.getText() : "";
            double due = duetxt.getText().equals("")? 0.00: Double.parseDouble(duetxt.getText());

            Customer cus = new Customer(due,
                    nametext.getText(),
                    store,adrstxt.getText(),
                    phntext.getText(),
                    emailtxt.getText(),
                    typebox.getValue());

            Boolean add =  DataHelper.insertCustomer(cus);
            if(add){
                nametext.clear();
                adrstxt.clear();
                phntext.clear();
                emailtxt.clear();
                strtxt.clear();
                duetxt.clear();
                typebox.setValue(CusType.Retailer.toString());
                nametext.requestFocus();
            }
        });

    }

    public static enum CusType {
        WholeSale,
        Retailer
    }
}
