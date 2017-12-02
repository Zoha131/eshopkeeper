package action.sell;

import add.customer.AddCustomer;
import data_helper.DataHelper;
import home.Main;
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

    ObservableList<String> dataCustomerName, dataProductName;
    CustomerStringConverter converterCustomer;
    ObservableList<Customer> dataCustomer;
    ObservableList<Product> dataProduct;
    ProductStringConverter converterProduct;


    private Invoice invoice;
    private int serial=0;
    private Product addProduct;
    private double tableHeight = 50;


    public Sell() {

    }

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
        datePick.setValue(LocalDate.now());
        setTableColumn();



        invoiceType.getItems().addAll(AddCustomer.CusType.WholeSale.toString(), AddCustomer.CusType.Retailer.toString());
        invoiceType.setValue(AddCustomer.CusType.Retailer.toString());


        AutoCompletionBinding<String> binding = TextFields.bindAutoCompletion(cusTxt, dataCustomerName);
        binding.setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<String> event) -> {
            System.out.println("auto completion happened ");
            Customer curCustomer = converterCustomer.fromString(event.getCompletion());
            invoice.setCustomer(curCustomer);
            adrsTxt.setText(curCustomer.getAddress());
            emailTxt.setText(curCustomer.getEmail());
            phnTxt.setText(curCustomer.getPhone());
            invoiceTxt.setText(UUID.randomUUID().toString());
            System.out.println(datePick.getEditor().getText());
        });

        addOnAction();


    }


    private void addOnAction() {
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
                invoice.addData(new Invoice.Item(++serial, addProduct, 0));
                addProduct = null;
                itemTable.getItems().setAll(invoice.getData());
                tableHeight = tableHeight+25;
                itemTable.setMaxHeight(tableHeight);
                grdPan.setMinHeight(tableHeight+800);
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

            if(DataHelper.insertSell(invoice)) {
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
        invoice = new Invoice();
        itemTable.getItems().setAll(invoice.getData());
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
        rateColumn.setCellValueFactory(new PropertyValueFactory<Invoice.Item, Double>("rrate"));

        TableColumn<Invoice.Item, Double> totalColumn = new TableColumn<>("Total");
        totalColumn.setCellValueFactory(new PropertyValueFactory<Invoice.Item, Double>("total"));

        itemTable.getColumns().addAll(serialColumn, itemDesc, quantityColumn, rateColumn, totalColumn);

        Double descMinWidth = 940 - (serialColumn.getWidth()+ quantityColumn.getWidth()+rateColumn.getWidth()+totalColumn.getWidth());
        itemDesc.setMinWidth(descMinWidth);

        itemTable.setMaxHeight(tableHeight);
        itemTable.setEditable(true);
    }

    public static void printNode(final Node node){
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);
        PrinterAttributes attr = printer.getPrinterAttributes();
        PrinterJob job = PrinterJob.createPrinterJob();
        double scaleX = pageLayout.getPrintableWidth() / node.getBoundsInParent().getWidth();
        double scaleY = pageLayout.getPrintableHeight() / node.getBoundsInParent().getHeight();
        Scale scale = new Scale(scaleX, scaleY);
        node.getTransforms().add(scale);

        if (job != null && job.showPrintDialog(node.getScene().getWindow())) {
            boolean success = job.printPage(pageLayout, node);
            if (success) {
                job.endJob();

            }
        }
        node.getTransforms().remove(scale);
    }


}
