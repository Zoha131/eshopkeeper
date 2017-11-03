package add.customer;

import data_helper.DataHelper;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class Customer {
    @FXML
    TextField nametext,phntext,emailtxt;
    @FXML
    ChoiceBox<String> typebox;
    @FXML
    TextArea adrstxt;
    @FXML
    Button add_btn;

    public void initialize(){
        System.out.println("Add customer worked");

        typebox.getItems().addAll("HoleSale", "Consumer");
        typebox.setValue("Consumer");


        /*
        * code to bind button with nametext and phonetext. so that
        * when these two field is empty then button will be disabled
        */
        add_btn.disableProperty().bind(
                Bindings.isEmpty(nametext.textProperty()).or(Bindings.isEmpty(phntext.textProperty()))
        );

        add_btn.setOnAction(event -> {
            Boolean add =  DataHelper.insertCustomer(nametext.getText(), adrstxt.getText(), phntext.getText(), emailtxt.getText(), typebox.getValue());
            if(add){
                nametext.clear();
                adrstxt.clear();
                phntext.clear();
                emailtxt.clear();
            }
        });

        //todo-me need to add email checker in form

    }
}
