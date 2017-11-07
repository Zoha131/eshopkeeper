package add.customer;

import data_helper.DataHelper;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Customer;


public class AddCustomer {
    @FXML TextField nametext,phntext,emailtxt, strtxt, duetxt;
    @FXML ChoiceBox<String> typebox;
    @FXML TextArea adrstxt;
    @FXML Button add_btn;
    @FXML Label strlbl;

    public void initialize(){
        typebox.getItems().addAll(CusType.WholeSale.toString(), CusType.Retailer.toString());
        typebox.setValue(CusType.Retailer.toString());


        /*
        * code to bind button with nametext and phonetext. so that
        * when these two field is empty then button will be disabled
        */
        add_btn.disableProperty().bind(
                Bindings.isEmpty(nametext.textProperty()).or(Bindings.isEmpty(phntext.textProperty()))
        );

        BooleanBinding isStore = Bindings.equal(CusType.Retailer.toString(), typebox.valueProperty());

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

        //todo-me need to add email validation logic
        //todo-me need to add due text validation logic

    }

    public static enum CusType {
        WholeSale,
        Retailer
    }
}
