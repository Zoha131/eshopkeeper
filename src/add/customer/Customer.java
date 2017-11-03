package add.customer;

import data_helper.DataHelper;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class Customer {
    @FXML TextField nametext,phntext,emailtxt, strtxt, duetxt;
    @FXML ChoiceBox<String> typebox;
    @FXML TextArea adrstxt;
    @FXML Button add_btn;
    @FXML Label strlbl;

    public void initialize(){
        System.out.println("Add customer worked");

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

            Boolean add =  DataHelper.insertCustomer(nametext.getText(), adrstxt.getText(), phntext.getText(),
                    emailtxt.getText(), typebox.getValue(), store, Double.parseDouble(duetxt.getText()));
            if(add){
                nametext.clear();
                adrstxt.clear();
                phntext.clear();
                emailtxt.clear();
                strtxt.clear();
                duetxt.clear();

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
