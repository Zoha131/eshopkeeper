package action.purchase;

import dialog.AddProductDialog;
import converter.DateStringConverter;
import converter.ModelStringConverter;
import data_helper.DataHelper;
import home.Main;
import home.Toast;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import model.*;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

public class PurchaseView {
    @FXML TextField supNameTxt, strNameTxt, adrsTxt, phnTxt, idTxt, purchasedByTxt, totalTxt, paidTxt, dueTxt;
    @FXML DatePicker datePick;
    @FXML Button addBtn, addNewBtn, saveBtn,clrBtn;
    @FXML GridPane purInvoiceGrid;
    @FXML TableView<Item> itemTable;

    private ScrollPane scrollPane;

    private final double GRID_HEIGHT=600;
    private double tableHeight = 50;

    ObservableList<Supplier> dataSupplier;
    ModelStringConverter<Supplier> converterSupplier;

    ObservableList<Product> dataProduct;
    ModelStringConverter<Product> converterProduct;

    ObservableList<String> dataSupplierName, dataProductName;


    private Invoice<Supplier> invoice;
    private int serial=0;
    private Product addProduct;
    private DateStringConverter dateStringConverter;
    private ValidationSupport priceValidation, supplierValidation;



    public void initialize(){

        idTxt.setText(UUID.randomUUID().toString());

        dateStringConverter = new DateStringConverter();
        datePick.setConverter(dateStringConverter);
        datePick.setValue(LocalDate.now());

        // to update the dueTxt with total and paid text
        paidTxt.textProperty().addListener(this::updateDueTxt);
        totalTxt.textProperty().addListener(this::updateDueTxt);

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

            //disable the textfields
            setTextEditable(false);

        });

        //adding validation
        priceValidation = new ValidationSupport();
        supplierValidation = new ValidationSupport();
        Platform.runLater(()->{
            priceValidation.registerValidator(paidTxt, Validator.createRegexValidator("Must be a Decimal", "[0-9]+\\.?[0-9]*", Severity.ERROR));
            priceValidation.registerValidator(totalTxt, Validator.createRegexValidator("Must be a Decimal", "[0-9]+\\.?[0-9]*", Severity.ERROR));
            priceValidation.registerValidator(dueTxt, Validator.createRegexValidator("Must be a Decimal", "[0-9]+\\.?[0-9]*", Severity.ERROR));
        });
        Platform.runLater(()->{
            supplierValidation.registerValidator(supNameTxt, Validator.createEmptyValidator("These fields must not be empty"));
            supplierValidation.registerValidator(strNameTxt, Validator.createEmptyValidator("These fields must not be empty"));
            supplierValidation.registerValidator(adrsTxt, Validator.createEmptyValidator("These fields must not be empty"));
            supplierValidation.registerValidator(phnTxt,  Validator.createRegexValidator("Must be a phn number", "\\+?[0-9]{11,13}$", Severity.ERROR));
            supplierValidation.registerValidator(purchasedByTxt, Validator.createEmptyValidator("These fields must not be empty", Severity.WARNING));
        });

        addOnAction();
    }

    //this function will be called from the loader
    public void setScrollPane(ScrollPane scrollPane) {
        this.scrollPane = scrollPane;
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        purInvoiceGrid.minWidthProperty().bind(scrollPane.widthProperty().subtract(15));
    }

    private void addOnAction() {
        addBtn.disableProperty().bind(supplierValidation.invalidProperty());
        addNewBtn.disableProperty().bind(supplierValidation.invalidProperty());
        saveBtn.disableProperty().bind(priceValidation.invalidProperty());

        addBtn.setOnAction((ActionEvent event) -> {

            Dialog<AddProductDialog.DialogModel> dialog = AddProductDialog.getPurchaseDialog(dataProductName);

            Optional<AddProductDialog.DialogModel> result = dialog.showAndWait();

            result.ifPresent(this::addConsumer);
        });
        addNewBtn.setOnAction((ActionEvent event) -> {

            Dialog<AddProductDialog.DialogModel> dialog = AddProductDialog.getNewProductDialog();

            Optional<AddProductDialog.DialogModel> result = dialog.showAndWait();

            result.ifPresent(dialogModel -> {
                //updating dataProduct as we insert new product which was not available in the database before
                //todo-me this is inefficient. need to make this efficient
                dataProduct = DataHelper.getProduct();
                converterProduct.setData(dataProduct);

                addConsumer(dialogModel);
            });
        });
        saveBtn.setOnAction(event -> {
            if(invoice.getTrader() == null){
                Supplier supplier= new Supplier(0, 0,
                        supNameTxt.getText(),
                        strNameTxt.getText(),
                        adrsTxt.getText(),
                        phnTxt.getText(),
                        "default@domain.com");
                DataHelper.insertSupplier(supplier);

                //todo-me this is inefficient. need to make this efficient
                dataSupplier = DataHelper.getSupplier();
                converterSupplier.setData(dataSupplier);
                invoice.setTrader(converterSupplier.fromString(supplier.getName()));
            }

            //setting due to the supplier
            Supplier supplier = invoice.getTrader();
            supplier.setDue(supplier.getDue()+Double.parseDouble(dueTxt.getText()));

            //adding data to invoice before saving
            invoice.setDate(datePick.getEditor().getText());
            invoice.setTransactionID(idTxt.getText());
            invoice.setAuthorityName(purchasedByTxt.getText());
            Transaction transaction = new Transaction(0, invoice.getTrader().getId(), invoice.getPrice(), Double.parseDouble(paidTxt.getText()),invoice.getDate(), invoice.getTransactionID(), invoice.getAuthorityName());

            //lowering the opacity when saving to database and sowing message
            purInvoiceGrid.setOpacity(.5);
            Toast.makeText(Main.getMainStage(), "Saving", 500, 200, 200);

            //adding the data in another thread to have better UI experience
            Thread t1 = new Thread(()-> {
                boolean isSaved = DataHelper.insertPurchase(invoice) && DataHelper.insertPurchaseTransaction(transaction);
                boolean isUpdated = DataHelper.updateData("supplier", "due", supplier.getDue(), supplier.getId());
                if(isSaved && isUpdated){
                    Platform.runLater(()->{
                        purInvoiceGrid.setOpacity(1);
                        scrollPane.setVvalue(0);
                    });
                }
            });
            t1.start();
            clearAll(null);
        });

        clrBtn.setOnAction(this::clearAll);
    }

    private void  addConsumer(AddProductDialog.DialogModel dialogModel){
        //get product by name and add to the invoice data
        addProduct = converterProduct.fromString(dialogModel.getName());
        invoice.addData(new Item(++serial, addProduct, dialogModel.getPrate(), dialogModel.getQuantity()));
        addProduct = null;

        //calculate the invoice price and update the price in textField
        invoice.calPrice();
        totalTxt.setText(String.valueOf(invoice.getPrice()));
        itemTable.setItems(invoice.getData());

        //enlarge the height for better view
        tableHeight = tableHeight+30;
        purInvoiceGrid.setMinHeight(tableHeight+GRID_HEIGHT);
        itemTable.setMinHeight(tableHeight);

        //scrollpane will be updated
        scrollPane.setVvalue(1);
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

            Item invoiceItem = invoice.getData().get(data.getSerial()-1);
            invoiceItem.setQuantity(event.getNewValue());
            invoiceItem.calTotal();

            invoice.calPrice();

            totalTxt.setText(String.valueOf(invoice.getPrice()));

            itemTable.setItems(invoice.getData());
            itemTable.refresh();
            /*
            * itemTable.getItems().setAll(invoice.getData());
            * I don't know why this code don't work here.
            * This line works fine in SellView class in line 309
            */
            itemTable.requestFocus();
        });

        DoubleStringConverter doubleStringConverter = new DoubleStringConverter();

        TableColumn<Item, Double> rateColumn = new TableColumn<>("Rate");
        rateColumn.setCellValueFactory(new PropertyValueFactory<>("rate"));
        rateColumn.setCellFactory(TextFieldTableCell.forTableColumn(doubleStringConverter));
        rateColumn.setOnEditCommit(event -> {
            Item data = event.getRowValue();

            Item invoiceItem = invoice.getData().get(data.getSerial()-1);
            invoiceItem.setRate(event.getNewValue());
            invoiceItem.calTotal();

            invoice.calPrice();

            totalTxt.setText(String.valueOf(invoice.getPrice()));

            itemTable.setItems(invoice.getData());
            itemTable.refresh();
            itemTable.requestFocus();
            //todo-me Tab->next edit cell Enter->commit edit
        });

        TableColumn<Item, Double> wrateColumn = new TableColumn<>("Wh. Rate");
        wrateColumn.setCellValueFactory(new PropertyValueFactory<>("wrate"));
        wrateColumn.setCellFactory(TextFieldTableCell.forTableColumn(doubleStringConverter));
        wrateColumn.setOnEditCommit(event -> {
            Item data = event.getRowValue();

            Item invoiceItem = invoice.getData().get(data.getSerial()-1);
            invoiceItem.setWrate(event.getNewValue());
            invoiceItem.calTotal();

            invoice.calPrice();

            totalTxt.setText(String.valueOf(invoice.getPrice()));

            itemTable.setItems(invoice.getData());
            itemTable.refresh();
            itemTable.requestFocus();
        });

        TableColumn<Item, Double> rrateColumn = new TableColumn<>("Ret. Rate");
        rrateColumn.setCellValueFactory(new PropertyValueFactory<>("rrate"));
        rrateColumn.setCellFactory(TextFieldTableCell.forTableColumn(doubleStringConverter));
        rrateColumn.setOnEditCommit(event -> {
            Item data = event.getRowValue();

            Item invoiceItem = invoice.getData().get(data.getSerial()-1);
            invoiceItem.setRrate(event.getNewValue());
            invoiceItem.calTotal();

            invoice.calPrice();

            totalTxt.setText(String.valueOf(invoice.getPrice()));

            itemTable.setItems(invoice.getData());
            itemTable.refresh();
            itemTable.requestFocus();
        });

        TableColumn<Item, Double> totalColumn = new TableColumn<>("Total");
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        itemTable.getColumns().addAll(serialColumn, itemDesc, quantityColumn, rateColumn,rrateColumn,wrateColumn, totalColumn);

        Double descMinWidth = 940 - (serialColumn.getWidth()+ quantityColumn.getWidth()+rateColumn.getWidth()+totalColumn.getWidth()+rrateColumn.getWidth()+wrateColumn.getWidth());
        itemDesc.setMinWidth(descMinWidth);

        itemTable.setMaxHeight(tableHeight);
        itemTable.setFixedCellSize(30);
        itemTable.setEditable(true);
    }

    private void setTextEditable(boolean editable){
        supNameTxt.setEditable(editable);
        strNameTxt.setEditable(editable);
        adrsTxt.setEditable(editable);
        phnTxt.setEditable(editable);
    }

    private void clearAll(ActionEvent event) {
        supNameTxt.clear();
        strNameTxt.clear();
        adrsTxt.clear();
        phnTxt.clear();
        idTxt.clear();
        totalTxt.clear();
        paidTxt.clear();
        dueTxt.clear();
        itemTable.setItems(null);
        invoice = new Invoice<>();

        //enlarge the height for better view
        tableHeight = 50;
        purInvoiceGrid.setMinHeight(tableHeight+GRID_HEIGHT);
        itemTable.setMinHeight(tableHeight);

        //scrollpane will be updated
        scrollPane.setVvalue(0);

        setTextEditable(true);
        idTxt.setText(UUID.randomUUID().toString());
        supNameTxt.requestFocus();
    }

    private void updateDueTxt(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        double paid = 0;
        double total = 0;
        try{
            total = Double.parseDouble(totalTxt.getText());
            paid = Double.parseDouble(paidTxt.getText());
        }catch (NumberFormatException e){
            System.out.println("PuchaseView: paidTxt Property Exception");
        }finally {
            double due = total-paid;
            dueTxt.setText(String.format("%.2f", due));
        }
    }

}
