package add.supplier;

import data_helper.DataHelper;
import home.Main;
import home.Toast;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Supplier;


public class AddSupplier {
    @FXML private TextField nametext,phntext,emailtxt, strtxt, duetxt;
    @FXML private TextArea adrstxt;
    @FXML private Button add_btn;

    public void initialize(){



        /*
        * code to bind button with nametext and phonetext. so that
        * when these two field is empty then button will be disabled
        */

        add_btn.disableProperty().bind(
                Bindings.isEmpty(nametext.textProperty()).or(Bindings.isEmpty(phntext.textProperty()))
        );


        add_btn.setOnAction((ActionEvent event) -> {

            double due = duetxt.getText().equals("")? 0.00: Double.parseDouble(duetxt.getText());
            Supplier sup = new Supplier(0,due,
                    nametext.getText(),
                    strtxt.getText(),
                    adrstxt.getText(),
                    phntext.getText(),
                    emailtxt.getText());

            Boolean add =  DataHelper.insertSupplier(sup);
            if(add){
                nametext.clear();
                adrstxt.clear();
                phntext.clear();
                emailtxt.clear();
                strtxt.clear();
                duetxt.clear();
                nametext.requestFocus();
                Toast.makeText(Main.getMainStage(), "added",3500, 500, 500);
            }
        });

        //todo-me need to add email validation logic
        //todo-me need to add due text validation logic

    }
}
