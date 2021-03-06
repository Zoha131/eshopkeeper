package com.zoha131.eshopkeeper.dialog;

import com.zoha131.eshopkeeper.converter.ModelStringConverter;
import com.zoha131.eshopkeeper.data_helper.DataHelper;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import com.zoha131.eshopkeeper.model.Item;
import com.zoha131.eshopkeeper.model.Product;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

public class AddProductDialog {

    private static ObservableList<String>  dataProductName;
    private static ObservableList<Product> dataProduct;
    private static ModelStringConverter<Product> converterProduct;

    public static Dialog<Item> getSellDialogue(int serial, boolean isRetailer){
        Dialog<Item> sellDialogue = new Dialog<>();
        Item item = new Item();

        sellDialogue.setTitle("eShopkeeper");
        sellDialogue.setHeaderText("Insert Product Name and Quantity");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Add Item", ButtonBar.ButtonData.OK_DONE);
        sellDialogue.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the productName and quantity labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(30, 90, 20, 80));

        TextField productName = new TextField();
        productName.setPromptText("Product Name");
        AutoCompletionBinding<String> binding = TextFields.bindAutoCompletion(productName, dataProductName);
        TextField quantity = new TextField();
        quantity.setPromptText("Quantity");
        Label stockLbl = new Label();
        stockLbl.setMinWidth(40);
        Label errMsg = new Label();
        errMsg.setTextFill(Color.RED);

        grid.add(new Label("Product:"), 0, 0);
        grid.add(productName, 1, 0);
        grid.add(new Label("Quantity:"), 0, 1);
        grid.add(quantity, 1, 1);
        grid.add(stockLbl, 2,1);
        grid.add(errMsg, 0,2);
        GridPane.setColumnSpan(errMsg, 3);

        //adding validation
        ValidationSupport validationSupport = new ValidationSupport();
        validationSupport.registerValidator(quantity, Validator.createRegexValidator("Must be a number", "[0-9]+", Severity.ERROR));

        // Enable/Disable com.zoha131.eshopkeeper.login button depending on whether a productName was entered.
        BooleanProperty autoComletedProperty = new SimpleBooleanProperty(false);
        Node selectButton = sellDialogue.getDialogPane().lookupButton(loginButtonType);
        selectButton.disableProperty().bind(autoComletedProperty.not().or(validationSupport.invalidProperty()));
        binding.setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<String> eventAuto)-> {
            autoComletedProperty.setValue(true);
            Product product = converterProduct.fromString(eventAuto.getCompletion());
            stockLbl.setText(String.format("{ %d }", product.getStock()));

            item.setProduct(product);
        });

        quantity.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue)-> {
                try {
                    int count = Integer.parseInt(newValue);
                    int stock = item.getProduct().getStock();
                    if(count>stock){
                        errMsg.setText(String.format("Quantity must be less than or equal to %d", item.getProduct().getStock()));
                        autoComletedProperty.setValue(false);
                    }
                    else {
                        errMsg.setText("");
                        autoComletedProperty.setValue(true);
                    }
                }catch (Exception e){
                    errMsg.setText("Quantity must be a number");
                }

        });

        sellDialogue.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> productName.requestFocus());

        // Convert the result to a username-password-pair when the com.zoha131.eshopkeeper.login button is clicked.
        sellDialogue.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                item.constuctItem(serial, isRetailer, Integer.parseInt(quantity.getText()));
                return item;
            }
            return null;
        });

        return sellDialogue;
    }

    public static Dialog<DialogModel> getPurchaseDialog(ObservableList<String> dataProductName){
        Dialog<DialogModel> sellDialogue = new Dialog<>();

        sellDialogue.setTitle("eShopkeeper");
        sellDialogue.setHeaderText("Insert Product Name and Quantity");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Add Item", ButtonBar.ButtonData.OK_DONE);
        sellDialogue.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the productName and quantity labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(30, 90, 20, 80));

        TextField productName = new TextField();
        productName.setPromptText("Product Name");
        AutoCompletionBinding<String> binding = TextFields.bindAutoCompletion(productName, dataProductName);
        TextField quantity = new TextField();
        quantity.setPromptText("Quantity");
        TextField prate = new TextField();
        prate.setPromptText("Purchase Rate");

        grid.add(new Label("Product:"), 0, 0);
        grid.add(productName, 1, 0);
        grid.add(new Label("Quantity:"), 0, 1);
        grid.add(quantity, 1, 1);
        grid.add(new Label("Purchase Rate:"), 0, 2);
        grid.add(prate, 1, 2);

        //adding validation
        ValidationSupport validationSupport = new ValidationSupport();
        Platform.runLater(()->{
            validationSupport.registerValidator(quantity, Validator.createRegexValidator("Must be a number", "[0-9]+", Severity.ERROR));
            validationSupport.registerValidator(prate, Validator.createRegexValidator("Must be a Decimal", "[0-9]+\\.?[0-9]*", Severity.ERROR));
        });

        // Enable/Disable com.zoha131.eshopkeeper.login button depending on whether a productName was entered.
        BooleanProperty autoComletedProperty = new SimpleBooleanProperty(false);
        Node selectButton = sellDialogue.getDialogPane().lookupButton(loginButtonType);
        selectButton.disableProperty().bind(autoComletedProperty.not().or(validationSupport.invalidProperty()));
        binding.setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<String> eventAuto)-> {
            autoComletedProperty.setValue(true);
        });

        sellDialogue.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> productName.requestFocus());

        // Convert the result to a username-password-pair when the com.zoha131.eshopkeeper.login button is clicked.
        sellDialogue.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new DialogModel(productName.getText(), Integer.parseInt(quantity.getText()), Double.parseDouble(prate.getText()));
            }
            return null;
        });

        return sellDialogue;
    }

    public static Dialog<DialogModel> getNewProductDialog(){
        Dialog<DialogModel> sellDialogue = new Dialog<>();

        sellDialogue.setTitle("eShopkeeper");
        sellDialogue.setHeaderText("Insert Product Details");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Add Item", ButtonBar.ButtonData.OK_DONE);
        sellDialogue.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the productName and quantity labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(30, 90, 20, 80));

        TextField productName = new TextField();
        productName.setPromptText("Product Name");
        grid.add(new Label("Product Name:"), 0, 0);
        grid.add(productName, 1, 0);

        TextField code = new TextField();
        code.setPromptText("Product Code");
        grid.add(new Label("Product Code:"), 0, 1);
        grid.add(code, 1, 1);

        TextField company = new TextField();
        company.setPromptText("Company Name");
        grid.add(new Label("Company Name:"), 0, 2);
        grid.add(company, 1, 2);

        TextField prate = new TextField();
        prate.setPromptText("Purchase Rate");
        grid.add(new Label("Purchase Rate:"), 0, 3);
        grid.add(prate, 1, 3);

        TextField rrate = new TextField();
        rrate.setPromptText("Retail Rate");
        grid.add(new Label("Retail Rate:"), 0, 4);
        grid.add(rrate, 1, 4);

        TextField wrate = new TextField();
        wrate.setPromptText("Wholesale Rate");
        grid.add(new Label("Wholesale Rate:"), 0, 5);
        grid.add(wrate, 1, 5);

        TextField quantity = new TextField();
        quantity.setPromptText("Quantity");
        grid.add(new Label("Quantity:"), 0, 6);
        grid.add(quantity, 1, 6);


        //adding validation
        ValidationSupport validationSupport = new ValidationSupport();
        Platform.runLater(()->{
            validationSupport.registerValidator(quantity, Validator.createRegexValidator("Must be a number", "[0-9]+", Severity.ERROR));
            validationSupport.registerValidator(prate, Validator.createRegexValidator("Must be a Decimal", "[0-9]+\\.?[0-9]*", Severity.ERROR));
            validationSupport.registerValidator(wrate, Validator.createRegexValidator("Must be a Decimal", "[0-9]+\\.?[0-9]*", Severity.ERROR));
            validationSupport.registerValidator(rrate, Validator.createRegexValidator("Must be a Decimal", "[0-9]+\\.?[0-9]*", Severity.ERROR));
        });

        // Enable/Disable com.zoha131.eshopkeeper.login button depending on whether a productName was entered.
        Node selectButton = sellDialogue.getDialogPane().lookupButton(loginButtonType);
        selectButton.disableProperty().bind(Bindings.isEmpty(productName.textProperty())
                .or(Bindings.isEmpty(code.textProperty()))
                .or(Bindings.isEmpty(company.textProperty()))
                .or(validationSupport.invalidProperty()));

        sellDialogue.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> productName.requestFocus());

        // Convert the result to a username-password-pair when the com.zoha131.eshopkeeper.login button is clicked.
        sellDialogue.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {

                Product product = new Product(0, 0,
                        productName.getText(),
                        code.getText(),
                        company.getText(),
                        Double.parseDouble(prate.getText()),
                        Double.parseDouble(wrate.getText()),
                        Double.parseDouble(rrate.getText()));

                DataHelper.insertProduct(product);

                return new DialogModel(productName.getText(), Integer.parseInt(quantity.getText()), Double.parseDouble(prate.getText()));
            }
            return null;
        });

        return sellDialogue;
    }

    public static void setDataProduct(ObservableList<Product> dataProduct) {
        AddProductDialog.dataProduct = dataProduct;
        AddProductDialog.converterProduct = new ModelStringConverter<>(dataProduct);
        AddProductDialog.dataProductName = FXCollections.observableArrayList();
        for(Product product: dataProduct){
            dataProductName.add(converterProduct.toString(product));
        }
    }

    public static class DialogModel {
        String name;
        int quantity;
        double prate;

        public DialogModel(String name, int quantity) {
            this.name = name;
            this.quantity = quantity;
        }

        public DialogModel(String name, int quantity, double prate) {
            this.name = name;
            this.quantity = quantity;
            this.prate = prate;
        }

        public double getPrate() {
            return prate;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}
