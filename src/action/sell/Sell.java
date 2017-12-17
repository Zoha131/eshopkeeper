package action.sell;

import add.customer.AddCustomer;
import data_helper.DataHelper;
import home.Main;
import home.Toast;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import model.*;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import sun.security.krb5.SCDynamicStoreConfig;

import javax.naming.Binding;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class Sell {
    @FXML    TextField cusTxt, adrsTxt, phnTxt, emailTxt, invoiceTxt, soldByTxt,priceTxt, vatTxt, totalTxt, paidTxt, dueTxt;
    @FXML    TableView<Invoice.Item> itemTable;
    @FXML    ComboBox<String> invoiceType;
    @FXML    DatePicker datePick;
    @FXML    Button saveBtn, addBtn, calcBtn, printBtn;
    @FXML    GridPane grdPan;
    @FXML    Label amountWordLBL;

    private ScrollPane scrollPane;

    private final double GRID_HEIGHT=1000;
    private int scrollTime = 0;
    private boolean isScrollable = true;

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

            //to scroll below to show to add button
            scrollPane =(ScrollPane) Main.getRoot().getCenter();
            if(isScrollable){
                scrollPane.setVvalue(.4);
                isScrollable = false;
            }
        });
        addOnAction();



    }


    private void addOnAction() {
        addBtn.disableProperty().bind(Bindings.isEmpty(cusTxt.textProperty()));
        addBtn.setOnAction((ActionEvent event) -> {

            AddProductDialog dialog = new AddProductDialog(dataProductName);

            Optional<AddProductDialog.Model> result = dialog.showAndWait();

            result.ifPresent(model -> {
                //get product by name and add to the invoice data
                addProduct = converterProduct.fromString(model.getName());
                invoice.addData(new Invoice.Item(++serial, addProduct, invoice.getCustomer().isRetailer(), model.getQuantity()));
                addProduct = null;

                //calculate the invoice price and update the price in textField
                invoice.calPrice();
                priceTxt.setText(String.valueOf(invoice.getPrice()));
                totalTxt.setText(String.valueOf(invoice.getPrice()));
                itemTable.getItems().setAll(invoice.getData());

                //enlarge the height for better view
                tableHeight = tableHeight+30;
                itemTable.setMaxHeight(tableHeight);
                grdPan.setMinHeight(tableHeight+GRID_HEIGHT);

                //scrollpane will be updated two times. and then the table will be fixed.
                if(scrollTime<2){
                    scrollPane.setVvalue(scrollPane.getVmax());
                    scrollTime++;
                }
            });
        });
        calcBtn.setOnAction(this::priceCalculate);
        printBtn.setOnAction(event -> {
            printNode(grdPan);
        });
        saveBtn.setOnAction((event -> {
            //calucating price and due before saving in database
            priceCalculate(null);

            //setting due to the customer
            Customer customer = invoice.getCustomer();
            customer.setDue(customer.getDue()+Double.parseDouble(dueTxt.getText()));

            //adding data to invoice before saving
            invoice.setDate(datePick.getEditor().getText());
            invoice.setTransactionID(invoiceTxt.getText());
            invoice.setSoldBy(soldByTxt.getText());
            Transaction transaction = new Transaction(0, invoice.getCustomer().getId(),Double.parseDouble(paidTxt.getText()),invoice.getDate() ,invoice.getTransactionID(), invoice.getSoldBy());

            //lowering the opacity when saving to database and sowing message
            grdPan.setOpacity(.5);
            Toast.makeText(Main.getMainStage(), "Saving", 500, 200, 200);

            //adding the data in another thread to have better UI experience
            Thread t1 = new Thread(()-> {
                boolean isSaved = DataHelper.insertSell(invoice) && DataHelper.insertSellTransaction(transaction);
                boolean isUpdated = DataHelper.updateData("customer", "due", customer.getDue(), customer.getId());
                if(isSaved && isUpdated){
                    Platform.runLater(()->{
                        allClear();
                        scrollPane.setVvalue(.32);
                        grdPan.setOpacity(1);
                    });
                }
            });
            t1.start();
        }));
    }

    //method to clear all the textfield and tableview after save to databes
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

    private void priceCalculate(ActionEvent actionEvent){
        double price = Double.parseDouble(totalTxt.getText());
        double paid = Double.parseDouble(paidTxt.getText());
        double due = price - paid;
        dueTxt.setText(String.valueOf(due));
        amountWordLBL.setText(CashWordConverter.doubleConvert(invoice.getPrice()));
    }


}
