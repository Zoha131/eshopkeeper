package add.product;

import data_helper.DataHelper;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


public class Product {
    @FXML
    TextField nametxt, codetxt, companytxt, pratetxt, wratetxt, rratetxt, stocktxt;
    @FXML
    Button addbtn;

    public void initialize(){


        addbtn.disableProperty().bind(
                Bindings.isEmpty(nametxt.textProperty())
                        .or(Bindings.isEmpty(codetxt.textProperty()))
                        .or(Bindings.isEmpty(companytxt.textProperty()))
                        .or(Bindings.isEmpty(pratetxt.textProperty()))
                        .or(Bindings.isEmpty(wratetxt.textProperty()))
                        .or(Bindings.isEmpty(pratetxt.textProperty()))
                        .or(Bindings.isEmpty(rratetxt.textProperty()))
                        .or(Bindings.isEmpty(stocktxt.textProperty()))
        );

        addbtn.setOnAction(event -> {
            Double prate, wrate, rrate, stock;
            prate = Double.parseDouble(pratetxt.getText());
            wrate = Double.parseDouble(wratetxt.getText());
            rrate = Double.parseDouble(rratetxt.getText());
            stock = Double.parseDouble(stocktxt.getText());

            boolean data = DataHelper.insertProduct(nametxt.getText(), codetxt.getText(), companytxt.getText(),
                    prate, wrate, rrate, stock);
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
            System.out.println("Product Data inserted: "+ data);

        });
    }
}

//todo-me add pratetxt, wratetxt, rratetxt, stocktxt validation logic
//todo-me add some view to show message to user if insert is failed
