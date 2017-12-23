package action.sell;

import add.customer.AddCustomer;
import add.product.AddProduct;
import converter.CashWordConverter;
import converter.DateStringConverter;
import converter.ModelStringConverter;
import data_helper.DataHelper;
import dialog.AddProductDialog;
import home.Main;
import home.Toast;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.transform.Scale;
import javafx.util.converter.IntegerStringConverter;
import model.*;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class SellView {
    @FXML    TextField cusTxt, adrsTxt, phnTxt, emailTxt, invoiceTxt, soldByTxt,priceTxt, vatTxt, totalTxt, paidTxt, dueTxt, strTxt;
    @FXML    TableView<Item> itemTable;
    @FXML    ComboBox<String> invoiceType;
    @FXML    DatePicker datePick;
    @FXML    Button saveBtn, addBtn, clrBtn, printBtn;
    @FXML    GridPane grdPan;
    @FXML    Label strLbl;

    private ScrollPane scrollPane;

    private final double GRID_HEIGHT=750;
    private double tableHeight = 50;

    ObservableList<String> dataCustomerName, dataProductName;
    ModelStringConverter<Customer> converterCustomer;
    ObservableList<Customer> dataCustomer;
    ObservableList<Product> dataProduct;
    ModelStringConverter<Product> converterProduct;


    private Invoice<Customer> invoice;
    private int serial=0;
    private Product addProduct;
    private DateStringConverter dateStringConverter;
    private boolean isRetailer=true;


    public void initialize() {

        invoiceTxt.setText(UUID.randomUUID().toString());

        dateStringConverter = new DateStringConverter();
        datePick.setConverter(dateStringConverter);
        datePick.setValue(LocalDate.now());

        dataCustomer = DataHelper.getCustomer();
        dataCustomerName = FXCollections.observableArrayList();
        converterCustomer = new ModelStringConverter<>(dataCustomer);
        for (Customer cus : dataCustomer) {
            dataCustomerName.add(cus.getName());
        }


        dataProduct = DataHelper.getProduct();
        dataProductName = FXCollections.observableArrayList();
        converterProduct = new ModelStringConverter<>(dataProduct);
        for(Product prod: dataProduct){
            dataProductName.add(converterProduct.toString(prod));
        }


        invoice = new Invoice<>();
        setTableColumn();

        invoiceType.getItems().addAll(AddCustomer.CusType.WholeSale.toString(), AddCustomer.CusType.Retailer.toString());
        invoiceType.setValue(AddCustomer.CusType.Retailer.toString());
        invoiceType.setOnAction(event -> {
            if(invoiceType.getSelectionModel().getSelectedItem().equals(AddCustomer.CusType.WholeSale.toString())){
                strLbl.setOpacity(1);
                strTxt.setOpacity(1);
                isRetailer=false;
            }
            else {
                strLbl.setOpacity(0);
                strTxt.setOpacity(0);
                isRetailer=true;
            }
        });


        AutoCompletionBinding<String> binding = TextFields.bindAutoCompletion(cusTxt, dataCustomerName);
        binding.setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<String> event) -> {
            Customer curCustomer = converterCustomer.fromString(event.getCompletion());
            setEditable(false);
            invoice.setTrader(curCustomer);
            adrsTxt.setText(curCustomer.getAddress());
            emailTxt.setText(curCustomer.getEmail());
            phnTxt.setText(curCustomer.getPhone());

            if(curCustomer.getType().equals(AddCustomer.CusType.Retailer.toString())) {
                invoiceType.setValue(AddCustomer.CusType.Retailer.toString());
            }
            else{
                invoiceType.setValue(AddCustomer.CusType.WholeSale.toString());
                strTxt.setText(curCustomer.getStore());
            }
        });
        addOnAction();
    }

    //this function will be called from the loader
    public void setScrollPane(ScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }


    private void addOnAction() {
        addBtn.disableProperty().bind(Bindings.isEmpty(cusTxt.textProperty()));
        saveBtn.disableProperty().bind(Bindings.isEmpty(paidTxt.textProperty()));
        printBtn.disableProperty().bind(Bindings.isEmpty(paidTxt.textProperty()));
        addBtn.setOnAction((ActionEvent event) -> {

            Dialog<AddProductDialog.DialogModel> dialog = AddProductDialog.getSellDialogue(dataProductName);

            Optional<AddProductDialog.DialogModel> result = dialog.showAndWait();

            result.ifPresent(dialogModel -> {
                //get product by name and add to the invoice data
                addProduct = converterProduct.fromString(dialogModel.getName());
                invoice.addData(new Item(++serial, addProduct, isRetailer, dialogModel.getQuantity()));
                addProduct = null;

                //calculate the invoice price and update the price in textField
                invoice.calPrice();
                priceTxt.setText(String.valueOf(invoice.getPrice()));
                totalTxt.setText(String.valueOf(invoice.getPrice()));
                itemTable.getItems().setAll(invoice.getData());

                //enlarge the height for better view
                tableHeight = tableHeight+30;
                grdPan.setMinHeight(tableHeight+GRID_HEIGHT);
                itemTable.setMaxHeight(tableHeight);

                //scrollpane will be updated two times. and then the table will be fixed.
                scrollPane.setVvalue(.6);
            });
        });
        printBtn.setOnAction(event -> {
            printNode(grdPan);
        });
        saveBtn.setOnAction((event -> {

            if(invoice.getTrader()==null){
                Customer customer = new Customer(0,0,
                        cusTxt.getText(),
                        strTxt.getText(),
                        adrsTxt.getText(),
                        phnTxt.getText(),
                        emailTxt.getText(),
                        invoiceType.getSelectionModel().getSelectedItem());
                DataHelper.insertCustomer(customer);

                //todo-me this is inefficient. need to make this efficient
                dataCustomer = DataHelper.getCustomer();
                converterCustomer.setData(dataCustomer);
                invoice.setTrader(converterCustomer.fromString(customer.getName()));
            }

            //calucating price and due before saving in database
            priceCalculate(null);

            //setting due to the customer
            Customer customer = invoice.getTrader();
            customer.setDue(customer.getDue()+Double.parseDouble(dueTxt.getText()));

            //adding data to invoice before saving
            invoice.setDate(datePick.getEditor().getText());
            invoice.setTransactionID(invoiceTxt.getText());
            invoice.setAuthorityName(soldByTxt.getText());
            Transaction transaction = new Transaction(0, invoice.getTrader().getId(), invoice.getPrice() ,Double.parseDouble(paidTxt.getText()),invoice.getDate() ,invoice.getTransactionID(), invoice.getAuthorityName());

            //lowering the opacity when saving to database and sowing message
            grdPan.setOpacity(.5);
            Toast.makeText(Main.getMainStage(), "Saving", 500, 200, 200);

            //adding the data in another thread to have better UI experience
            Thread t1 = new Thread(()-> {
                boolean isSaved = DataHelper.insertSell(invoice) && DataHelper.insertSellTransaction(transaction);
                boolean isUpdated = DataHelper.updateData("customer", "due", customer.getDue(), customer.getId());
                if(isSaved && isUpdated){
                    Platform.runLater(()->{
                        grdPan.setOpacity(1);
                        scrollPane.setVvalue(0);
                    });
                }
            });
            t1.start();
            allClear();
        }));
        clrBtn.setOnAction(event -> {
            allClear();
        });
    }

    //method to clear all the textfield and tableview after save to databes
    private void allClear(){
        cusTxt.clear();
        dueTxt.clear();
        totalTxt.clear();
        vatTxt.clear();
        soldByTxt.clear();
        phnTxt.clear();
        paidTxt.clear();
        invoiceTxt.clear();
        emailTxt.clear();
        adrsTxt.clear();
        priceTxt.clear();
        invoiceTxt.setText(UUID.randomUUID().toString());
        setEditable(true);
        invoice = new Invoice();
        itemTable.getItems().setAll(invoice.getData());
        tableHeight = 50;
        itemTable.setMaxHeight(tableHeight);
        grdPan.setMinHeight(tableHeight+GRID_HEIGHT);
        scrollPane.setVvalue(0);
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
            priceTxt.setText(String.valueOf(invoice.getPrice()));
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

    private void printNode(final Node node){
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);
        PrinterAttributes attr = printer.getPrinterAttributes();
        PrinterJob job = PrinterJob.createPrinterJob();
        double scaleX = pageLayout.getPrintableWidth() / node.getBoundsInParent().getWidth();
        double scaleY = pageLayout.getPrintableHeight() / node.getBoundsInParent().getHeight();
        Scale scale = new Scale(scaleX, scaleY);
        node.getTransforms().add(scale);

        setBtnVisible(false);

        if (job != null && job.showPrintDialog(node.getScene().getWindow())) {
            boolean success = job.printPage(pageLayout, node);
            if (success) {
                job.endJob();

            }
        }
        node.getTransforms().remove(scale);
        setBtnVisible(true);
    }

    private void setEditable(boolean editable){
        adrsTxt.setEditable(editable);
        phnTxt.setEditable(editable);
        emailTxt.setEditable(editable);
        strTxt.setEditable(editable);
    }

    void setBtnVisible(boolean p){
        saveBtn.setVisible(p);
        addBtn.setVisible(p);
        printBtn.setVisible(p);
    }

    private void priceCalculate(ActionEvent actionEvent){
        double price = Double.parseDouble(totalTxt.getText());
        double paid = Double.parseDouble(paidTxt.getText());
        double due = price - paid;
        dueTxt.setText(String.valueOf(due));
    }


}
