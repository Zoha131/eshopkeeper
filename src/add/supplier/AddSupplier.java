package add.supplier;

import data_helper.DataHelper;
import home.Main;
import dialog.Toast;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Supplier;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.util.regex.Pattern;


public class AddSupplier {
    @FXML private TextField nametext,phntext,emailtxt, strtxt, duetxt;
    @FXML private TextArea adrstxt;
    @FXML private Button add_btn;

    private ValidationSupport validation;

    public void initialize(){
        /*
         * code to validate the form
         */
        validation = new ValidationSupport();
        Platform.runLater(()->{
            validation.registerValidator(nametext, Validator.createEmptyValidator("These fields must not be empty"));
            validation.registerValidator(strtxt, Validator.createEmptyValidator("This field must not be empty"));
            validation.registerValidator(adrstxt, Validator.createEmptyValidator("These fields must not be empty"));
            validation.registerValidator(phntext,  Validator.createRegexValidator("Must be a phn number", "\\+?[0-9]{11,13}$", Severity.ERROR));
            validation.registerValidator(emailtxt, Validator.createRegexValidator("Must be an email address", Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE), Severity.WARNING));
            validation.registerValidator(duetxt, Validator.createRegexValidator("Must be a Decimal", "[0-9]*\\.?[0-9]*", Severity.ERROR));
        });

        add_btn.disableProperty().bind(validation.invalidProperty());


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
    }
}
