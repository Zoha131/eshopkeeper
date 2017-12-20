package action.purchase;

import action.sell.AddProductDialog;
import add.customer.AddCustomer;
import converter.DateStringConverter;
import converter.ModelStringConverter;
import data_helper.DataHelper;
import home.Main;
import home.Toast;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;
import model.*;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class PurchaseView {
    @FXML TextField supNameTxt, strNameTxt, adrsTxt, phnTxt, idTxt, purchasedByTxt, totalTxt, paidTxt, dueTxt;
    @FXML DatePicker datePick;
    @FXML Button addBtn, addNewBtn, saveBtn;
    @FXML GridPane purInvoiceGrid;
    @FXML TableView<Item> itemTable;

    private ScrollPane scrollPane;

    private final double GRID_HEIGHT=600;

    ObservableList<Supplier> dataSupplier;
    ModelStringConverter<Supplier> converterSupplier;

    ObservableList<Product> dataProduct;
    ModelStringConverter<Product> converterProduct;

    ObservableList<String> dataSupplierName, dataProductName;


    private Invoice<Supplier> invoice;
    private int serial=0;
    private Product addProduct;
    private double tableHeight = 50;
    private DateStringConverter dateStringConverter;



    public void initialize(){

        dateStringConverter = new DateStringConverter();
        datePick.setConverter(dateStringConverter);
        // to update the dueTxt with total and paid text
        paidTxt.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue)-> {
            double paid = 0;
            double total = 0;
            try{
                total = Double.parseDouble(totalTxt.getText());
                paid = Double.parseDouble(newValue);
            }catch (NumberFormatException e){
                System.out.println("PuchaseView: paidTxt Property Exception");
            }finally {
                dueTxt.setText(String.valueOf((total-paid)));
            }
        });

        dataSupplier = DataHelper.getSupplier();
        dataSupplierName = FXCollections.observableArrayList();
        for(Supplier sup: dataSupplier){
            dataSupplierName.add(sup.getName());
        }
        converterSupplier = new ModelStringConverter<>(dataSupplier);

        dataProduct = DataHelper.getProduct();
        dataProductName = FXCollections.observableArrayList();
        converterProduct = new ModelStringConverter<>(dataProduct);
        for(Product prod: dataProduct){
            dataProductName.add(converterProduct.toString(prod));
        }

        invoice = new Invoice<>();
        setTableColumn();

        AutoCompletionBinding<String> binding = TextFields.bindAutoCompletion(supNameTxt, dataSupplierName);
        binding.setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<String> event) -> {
            Supplier curSupplier = converterSupplier.fromString(event.getCompletion());
            invoice.setTrader(curSupplier);
            adrsTxt.setText(curSupplier.getAddress());
            strNameTxt.setText(curSupplier.getStore());
            phnTxt.setText(curSupplier.getPhone());
            idTxt.setText(UUID.randomUUID().toString());
            datePick.setValue(LocalDate.now());

            //to scroll below to show to add button
            scrollPane =(ScrollPane) Main.getRoot().getCenter();
        });
        addOnAction();

    }

    private void addOnAction() {
        addBtn.disableProperty().bind(Bindings.isEmpty(supNameTxt.textProperty()));
        addNewBtn.disableProperty().bind(Bindings.isEmpty(supNameTxt.textProperty()));
        addBtn.setOnAction((ActionEvent event) -> {

            AddProductDialog dialog = new AddProductDialog(dataProductName);

            Optional<AddProductDialog.Model> result = dialog.showAndWait();

            result.ifPresent(model -> {
                //get product by name and add to the invoice data
                addProduct = converterProduct.fromString(model.getName());
                invoice.addData(new Item(++serial, addProduct, false, model.getQuantity()));
                addProduct = null;

                //calculate the invoice price and update the price in textField
                invoice.calPrice();
                totalTxt.setText(String.valueOf(invoice.getPrice()));
                itemTable.getItems().setAll(invoice.getData());

                //enlarge the height for better view
                tableHeight = tableHeight+30;
                purInvoiceGrid.setMinHeight(tableHeight+GRID_HEIGHT);
                itemTable.setMinHeight(tableHeight);

                //scrollpane will be updated
                scrollPane.setVvalue(scrollPane.getVmax());
            });
        });

        saveBtn.setOnAction(event -> {
            System.out.println("save hoise re sagla");
        });
    }

    private void setTableColumn(){
        TableColumn<Item, Integer> serialColumn = new TableColumn<>("SL");
        serialColumn.setCellValueFactory(new PropertyValueFactory<>("serial"));

        TableColumn<Item, String> itemDesc = new TableColumn<>("Item Description");
        itemDesc.setCellValueFactory(new PropertyValueFactory<>("description"));


        TableColumn<Item, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        IntegerStringConverter integerStringConverter = new IntegerStringConverter();
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(integerStringConverter));
        quantityColumn.setOnEditCommit(event -> {
            Item data = event.getRowValue();

            invoice.getData().get(data.getSerial()-1).setQuantity(event.getNewValue());
            invoice.getData().get(data.getSerial()-1).calTotal();
            invoice.calPrice();
            totalTxt.setText(String.valueOf(invoice.getPrice()));
            itemTable.getItems().setAll(invoice.getData());
        });

        TableColumn<Item, Double> rateColumn = new TableColumn<>("Rate");
        rateColumn.setCellValueFactory(new PropertyValueFactory<>("rate"));

        TableColumn<Item, Double> totalColumn = new TableColumn<>("Total");
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        itemTable.getColumns().addAll(serialColumn, itemDesc, quantityColumn, rateColumn, totalColumn);

        Double descMinWidth = 940 - (serialColumn.getWidth()+ quantityColumn.getWidth()+rateColumn.getWidth()+totalColumn.getWidth());
        itemDesc.setMinWidth(descMinWidth);

        itemTable.setMaxHeight(tableHeight);
        itemTable.setFixedCellSize(30);
        itemTable.setEditable(true);
    }
}
