package action.sell;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

public class AddProductDialog extends Dialog<AddProductDialog.Model> {

    private ObservableList<String> dataProductName;

    public AddProductDialog(ObservableList<String> dataProductName) {
        this.dataProductName = dataProductName;

        super.setTitle("eShopkeeper");
        super.setHeaderText("Insert Product Name and Quantity");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Add Item", ButtonBar.ButtonData.OK_DONE);
        super.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

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
        Node loginButton = super.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);
        binding.setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<String> eventAuto)-> {
            loginButton.setDisable(false);
        });

        super.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> productName.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        super.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Model(productName.getText(), Integer.parseInt(quantity.getText()));
                //todo-me to add data validation logic
            }
            return null;
        });

    }

    public static class Model {
        String name;
        int quantity;

        public Model(String name, int quantity) {
            this.name = name;
            this.quantity = quantity;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}
