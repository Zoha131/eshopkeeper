package com.zoha131.eshopkeeper.add.product;

import com.zoha131.eshopkeeper.data_helper.DataHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import com.zoha131.eshopkeeper.model.Product;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

public class AddProduct {
    @FXML TextField nametxt, codetxt, companytxt, pratetxt, wratetxt, rratetxt, stocktxt;
    @FXML Button addbtn;

    private ValidationSupport validation;

    public void initialize(){

//   tried to com.zoha131.eshopkeeper.add validation rules
//        nametxt.textProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println("oldvalue: "+oldValue);
//            System.out.println("newValue "+newValue);
//            nametxt.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
//        });

        validation = new ValidationSupport();
        Platform.runLater(()->{
            validation.registerValidator(nametxt, Validator.createEmptyValidator("This field must not be empty"));
            validation.registerValidator(codetxt, Validator.createEmptyValidator("This field must not be empty"));
            validation.registerValidator(companytxt, Validator.createEmptyValidator("This field must not be empty"));
            validation.registerValidator(stocktxt, Validator.createRegexValidator("Must be a number", "[0-9]+", Severity.ERROR));
            validation.registerValidator(pratetxt, Validator.createRegexValidator("Must be a Decimal", "[0-9]+\\.?[0-9]*", Severity.ERROR));
            validation.registerValidator(wratetxt, Validator.createRegexValidator("Must be a Decimal", "[0-9]+\\.?[0-9]*", Severity.ERROR));
            validation.registerValidator(rratetxt, Validator.createRegexValidator("Must be a Decimal", "[0-9]+\\.?[0-9]*", Severity.ERROR));
        });

        addbtn.disableProperty().bind(validation.invalidProperty());

        addbtn.setOnAction(event -> {
            double prate, wrate, rrate;
            int stock;
            prate = Double.parseDouble(pratetxt.getText());
            wrate = Double.parseDouble(wratetxt.getText());
            rrate = Double.parseDouble(rratetxt.getText());
            stock = Integer.parseInt(stocktxt.getText());

            Product prod = new Product(0,stock, nametxt.getText(), codetxt.getText(), companytxt.getText(), prate,wrate,rrate);
            boolean data = DataHelper.insertProduct(prod);
            if(data){
                nametxt.clear();
                codetxt.clear();
                companytxt.clear();
                wratetxt.clear();
                pratetxt.clear();
                rratetxt.clear();
                stocktxt.clear();
                nametxt.requestFocus();
            }
            System.out.println("product Data inserted: "+ data);

        });
    }
}
