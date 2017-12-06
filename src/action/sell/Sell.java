package action.sell;

import add.customer.AddCustomer;
import data_helper.DataHelper;
import home.Main;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import model.*;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import javax.naming.Binding;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class Sell {
    @FXML
    TextField cusTxt, adrsTxt, phnTxt, emailTxt, invoiceTxt, soldByTxt,priceTxt, vatTxt, totalTxt, paidTxt, dueTxt;
    @FXML
    TableView<Invoice.Item> itemTable;
    @FXML
    ComboBox<String> invoiceType;
    @FXML
    DatePicker datePick;
    @FXML
    Button saveBtn, addBtn, calcBtn, printBtn;
    @FXML
    GridPane grdPan;
    @FXML Label amountWordLBL;

    private final double GRID_HEIGHT=1000;

    ObservableList<String> dataCustomerName, dataProductName;
    CustomerStringConverter converterCustomer;
    ObservableList<Customer> dataCustomer;
    ObservableList<Product> dataProduct;
    ProductStringConverter converterProduct;


    private Invoice invoice;
    private int serial=0;
    private Product addProduct;
    private double tableHeight = 50;


    public void initialize() {

        dataCustomer = DataHelper.getCustomer();
        dataCustomerName = FXCollections.observableArrayList();
        for (Customer cus : dataCustomer) {
            dataCustomerName.add(cus.getName());
        }
        converterCustomer = new CustomerStringConverter(dataCustomer);

        dataProduct = DataHelper.getProduct();
        dataProductName = FXCollections.observableArrayList();
        converterProduct = new ProductStringConverter(dataProduct);
        for(Product prod: dataProduct){
            dataProductName.add(converterProduct.toString(prod));
        }


        invoice = new Invoice();
        setTableColumn();



        invoiceType.getItems().addAll(AddCustomer.CusType.WholeSale.toString(), AddCustomer.CusType.Retailer.toString());
        invoiceType.setValue(AddCustomer.CusType.Retailer.toString());


        AutoCompletionBinding<String> binding = TextFields.bindAutoCompletion(cusTxt, dataCustomerName);
        binding.setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<String> event) -> {
            Customer curCustomer = converterCustomer.fromString(event.getCompletion());
            invoice.setCustomer(curCustomer);
            adrsTxt.setText(curCustomer.getAddress());
            emailTxt.setText(curCustomer.getEmail());
            phnTxt.setText(curCustomer.getPhone());
            invoiceTxt.setText(UUID.randomUUID().toString());
            datePick.setValue(LocalDate.now());
            if(curCustomer.getType().equals(AddCustomer.CusType.Retailer.toString())) {
                invoiceType.setValue(AddCustomer.CusType.Retailer.toString());
            }
            else{
                invoiceType.setValue(AddCustomer.CusType.WholeSale.toString());
            }
        });

        addOnAction();


    }


    private void addOnAction() {
        addBtn.disableProperty().bind(Bindings.isEmpty(cusTxt.textProperty()));
        addBtn.setOnAction(event -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Adding Product");
            dialog.setHeaderText("Adding Product");
            dialog.setContentText("Please enter product name:");

            AutoCompletionBinding<String> binding = TextFields.bindAutoCompletion(dialog.getEditor(), dataProductName);

            binding.setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<String> eventAuto)-> {
                    System.out.println("auto completion happened " + eventAuto.getCompletion());


            });

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> {
                addProduct = converterProduct.fromString(name);
                invoice.addData(new Invoice.Item(++serial, addProduct, invoice.getCustomer().isRetailer(), 0));
                addProduct = null;
                itemTable.getItems().setAll(invoice.getData());
                tableHeight = tableHeight+30;
                itemTable.setMaxHeight(tableHeight);
                grdPan.setMinHeight(tableHeight+GRID_HEIGHT);
            });
        });
        calcBtn.setOnAction(event -> {
            double price = Double.parseDouble(totalTxt.getText());
            double paid = Double.parseDouble(paidTxt.getText());
            double due = price - paid;
            dueTxt.setText(String.valueOf(due));
            amountWordLBL.setText(CashWordConverter.doubleConvert(invoice.getPrice()));
        });
        printBtn.setOnAction(event -> {
            printNode(grdPan);
        });
        saveBtn.setOnAction(event -> {
            invoice.setDate(datePick.getEditor().getText());
            invoice.setTransactionID(invoiceTxt.getText());
            invoice.setSoldBy(soldByTxt.getText());
            Transaction transaction = new Transaction(0, invoice.getCustomer().getId(),
                    Double.parseDouble(paidTxt.getText()),invoice.getDate() ,invoice.getTransactionID());

            if(DataHelper.insertSell(invoice) && DataHelper.insertSellTransaction(transaction)) {
                allClear();
            }
        });
    }

    private void allClear(){
        cusTxt.clear();
        dueTxt.setText("0.00");
        totalTxt.setText("0.00");
        vatTxt.setText("0.00");
        soldByTxt.clear();
        phnTxt.clear();
        paidTxt.setText("0.00");
        invoiceTxt.clear();
        emailTxt.clear();
        adrsTxt.clear();
        priceTxt.setText("0.00");
        amountWordLBL.setText("");
        invoice = new Invoice();
        itemTable.getItems().setAll(invoice.getData());
        tableHeight = 50;
        itemTable.setMaxHeight(tableHeight);
        grdPan.setMinHeight(tableHeight+GRID_HEIGHT);
    }

    private void setTableColumn(){
        TableColumn<Invoice.Item, Integer> serialColumn = new TableColumn<>("SL");
        serialColumn.setCellValueFactory(new PropertyValueFactory<Invoice.Item, Integer>("serial"));

        TableColumn<Invoice.Item, String> itemDesc = new TableColumn<>("Item Description");
        itemDesc.setCellValueFactory(new PropertyValueFactory<Invoice.Item, String>("description"));


        TableColumn<Invoice.Item, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<Invoice.Item, Integer>("quantity"));
        IntegerStringConverter integerStringConverter = new IntegerStringConverter();
        quantityColumn.setCellFactory(TextFieldTableCell.<Invoice.Item, Integer>forTableColumn(integerStringConverter));
        quantityColumn.setOnEditCommit(event -> {
            Invoice.Item data = event.getRowValue();

            invoice.getData().get(data.getSerial()-1).setQuantity(event.getNewValue());
            invoice.getData().get(data.getSerial()-1).calTotal();
            invoice.calPrice();
            priceTxt.setText(String.valueOf(invoice.getPrice()));
            totalTxt.setText(String.valueOf(invoice.getPrice()));
            itemTable.getItems().setAll(invoice.getData());
        });

        TableColumn<Invoice.Item, Double> rateColumn = new TableColumn<>("Rate");
        rateColumn.setCellValueFactory(new PropertyValueFactory<Invoice.Item, Double>("rate"));

        TableColumn<Invoice.Item, Double> totalColumn = new TableColumn<>("Total");
        totalColumn.setCellValueFactory(new PropertyValueFactory<Invoice.Item, Double>("total"));

        itemTable.getColumns().addAll(serialColumn, itemDesc, quantityColumn, rateColumn, totalColumn);

        Double descMinWidth = 940 - (serialColumn.getWidth()+ quantityColumn.getWidth()+rateColumn.getWidth()+totalColumn.getWidth());
        itemDesc.setMinWidth(descMinWidth);

        itemTable.setMaxHeight(tableHeight);
        itemTable.setFixedCellSize(30);
        itemTable.setEditable(true);
    }

    public void printNode(final Node node){
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

    void setBtnVisible(boolean p){
        saveBtn.setVisible(p);
        addBtn.setVisible(p);
        calcBtn.setVisible(p);
        printBtn.setVisible(p);
    }


}
