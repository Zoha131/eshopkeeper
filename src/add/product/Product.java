package add.product;

import data_helper.DataHelper;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


public class Product {
    @FXML
    TextField nametxt, codetxt, cratetxt, hratetxt, companytxt;
    @FXML
    Button addbtn;

    public void initialize(){


        addbtn.disableProperty().bind(
                Bindings.isEmpty(nametxt.textProperty())
                .or(Bindings.isEmpty(codetxt.textProperty()))
                .or(Bindings.isEmpty(cratetxt.textProperty()))
                .or(Bindings.isEmpty(hratetxt.textProperty()))
                .or(Bindings.isEmpty(companytxt.textProperty()))
        );

        addbtn.setOnAction(event -> {
            Double cRate, hRate;
            cRate = Double.parseDouble(cratetxt.getText());
            hRate = Double.parseDouble(hratetxt.getText());

            boolean data = DataHelper.insertProduct(nametxt.getText(), codetxt.getText(), cRate, hRate, companytxt.getText());
            if(data){
                nametxt.clear();
                codetxt.clear();
                hratetxt.clear();
                cratetxt.clear();
                companytxt.clear();
            }
            System.out.println("Product Data inserted: "+ data);

        });
    }
}
