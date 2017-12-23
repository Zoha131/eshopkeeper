package dialog;

import converter.ModelStringConverter;
import data_helper.DataHelper;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import model.Item;
import model.Product;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

public class AddProductDialog {

    public static Dialog<DialogModel> getSellDialogue(ObservableList<String> dataProductName){
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

        grid.add(new Label("Product:"), 0, 0);
        grid.add(productName, 1, 0);
        grid.add(new Label("Quantity:"), 0, 1);
        grid.add(quantity, 1, 1);

        // Enable/Disable login button depending on whether a productName was entered.
        BooleanProperty autoComletedProperty = new SimpleBooleanProperty(false);
        Node selectButton = sellDialogue.getDialogPane().lookupButton(loginButtonType);
        selectButton.disableProperty().bind(autoComletedProperty.not().or(Bindings.isEmpty(quantity.textProperty())));
        binding.setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<String> eventAuto)-> {
            autoComletedProperty.setValue(true);
        });

        sellDialogue.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> productName.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        sellDialogue.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new DialogModel(productName.getText(), Integer.parseInt(quantity.getText()));
                //todo-me to add data validation logic
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


        // Enable/Disable login button depending on whether a productName was entered.
        BooleanProperty autoComletedProperty = new SimpleBooleanProperty(false);
        Node selectButton = sellDialogue.getDialogPane().lookupButton(loginButtonType);
        selectButton.disableProperty().bind(autoComletedProperty.not().or(Bindings.isEmpty(quantity.textProperty())));
        binding.setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<String> eventAuto)-> {
            autoComletedProperty.setValue(true);
        });

        sellDialogue.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> productName.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        sellDialogue.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new DialogModel(productName.getText(), Integer.parseInt(quantity.getText()), Double.parseDouble(prate.getText()));
                //todo-me to add data validation logic
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



        // Enable/Disable login button depending on whether a productName was entered.
        Node selectButton = sellDialogue.getDialogPane().lookupButton(loginButtonType);
        selectButton.disableProperty().bind(Bindings.isEmpty(productName.textProperty())
                .or(Bindings.isEmpty(code.textProperty()))
                .or(Bindings.isEmpty(company.textProperty()))
                .or(Bindings.isEmpty(prate.textProperty()))
                .or(Bindings.isEmpty(rrate.textProperty()))
                .or(Bindings.isEmpty(wrate.textProperty()))
                .or(Bindings.isEmpty(quantity.textProperty())));

        sellDialogue.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> productName.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
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
                //todo-me to add data validation logic
            }
            return null;
        });

        return sellDialogue;
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
