package action.sell;

import add.customer.AddCustomer;
import converter.MyDateConverter;
import converter.ModelStringConverter;
import data_helper.DataHelper;
import dialog.AddProductDialog;
import home.Main;
import dialog.Toast;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.*;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.util.converter.IntegerStringConverter;
import model.*;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import print.InvoiceSell;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

public class SellView {
    @FXML    TextField cusTxt, adrsTxt, phnTxt, emailTxt, invoiceTxt, soldByTxt,priceTxt, vatTxt, totalTxt, paidTxt, dueTxt, strTxt;
    @FXML    TableView<Item> itemTable;
    @FXML    ComboBox<String> invoiceType;
    @FXML    DatePicker datePick;
    @FXML    Button saveBtn, addBtn, clrBtn, printBtn;
    @FXML    GridPane grdPan;
    @FXML    Label strLbl;

    private ScrollPane scrollPane;

    private final double GRID_HEIGHT=525;
    private double tableHeight = 50;

    ObservableList<String> dataCustomerName;
    ModelStringConverter<Customer> converterCustomer;
    ObservableList<Customer> dataCustomer;


    private static Invoice<Customer> invoice;
    private int serial=1;
    private MyDateConverter myDateConverter;
    private boolean isRetailer=true;
    private ValidationSupport priceValidation, customerValidation;


    public void initialize() {
        invoiceTxt.setText(UUID.randomUUID().toString());

        myDateConverter = new MyDateConverter();
        datePick.setConverter(myDateConverter);
        datePick.setValue(LocalDate.now());

        //to update the totalTxt with vatText and paid text
        vatTxt.textProperty().addListener(this::updateTotalTxt);
        priceTxt.textProperty().addListener(this::updateTotalTxt);

        // to update the dueTxt with total and paid text
        paidTxt.textProperty().addListener(this::updateDueTxt);
        totalTxt.textProperty().addListener(this::updateDueTxt);

        dataCustomer = DataHelper.getCustomer();
        dataCustomerName = FXCollections.observableArrayList();
        converterCustomer = new ModelStringConverter<>(dataCustomer);
        for (Customer cus : dataCustomer) {
            dataCustomerName.add(cus.getName());
        }

        //setting the AddProductDialoge class
        AddProductDialog.setDataProduct(DataHelper.getProduct());


        invoice = new Invoice<>();
        setTableColumn();

        invoiceType.getItems().addAll(AddCustomer.CusType.WholeSale.toString(), AddCustomer.CusType.Retailer.toString());
        invoiceType.setValue(AddCustomer.CusType.Retailer.toString());
        invoiceType.setStyle("-fx-opacity: 1;"); //opacity has been set explicitly with css so this would be readable even after disabling it
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

        //adding validation
        priceValidation = new ValidationSupport();
        customerValidation = new ValidationSupport();
        Platform.runLater(()->{
            priceValidation.registerValidator(vatTxt, Validator.createRegexValidator("Must be a number", "[0-9]*\\.?[0-9]*", Severity.ERROR));
            priceValidation.registerValidator(paidTxt, Validator.createRegexValidator("Must be a Decimal", "[0-9]+\\.?[0-9]*", Severity.ERROR));
            priceValidation.registerValidator(totalTxt, Validator.createRegexValidator("Must be a Decimal", "[0-9]+\\.?[0-9]*", Severity.ERROR));
            priceValidation.registerValidator(priceTxt, Validator.createRegexValidator("Must be a Decimal", "[0-9]+\\.?[0-9]*", Severity.ERROR));
            priceValidation.registerValidator(dueTxt, Validator.createRegexValidator("Must be a Decimal", "[0-9]+\\.?[0-9]*", Severity.ERROR));
        });
        Platform.runLater(()->{
            customerValidation.registerValidator(cusTxt, Validator.createEmptyValidator("These fields must not be empty"));
            customerValidation.registerValidator(adrsTxt, Validator.createEmptyValidator("These fields must not be empty"));
            customerValidation.registerValidator(phnTxt,  Validator.createRegexValidator("Must be a phn number", "\\+?[0-9]{11,13}$", Severity.ERROR));
            customerValidation.registerValidator(emailTxt, Validator.createRegexValidator("Must be an email address", Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE), Severity.WARNING));
            customerValidation.registerValidator(soldByTxt, Validator.createEmptyValidator("These fields must not be empty", Severity.WARNING));
        });

        addOnAction();
    }

    //this function will be called from the loader
    public void setScrollPane(ScrollPane scrollPane) {
        this.scrollPane = scrollPane;
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        grdPan.minWidthProperty().bind(scrollPane.widthProperty().subtract(15));
    }


    private void addOnAction() {
        addBtn.disableProperty().bind(customerValidation.invalidProperty());
        saveBtn.disableProperty().bind(priceValidation.invalidProperty());
        printBtn.disableProperty().bind(priceValidation.invalidProperty());
        addBtn.setOnAction((ActionEvent event) -> {
            //after adding an item the customer type will be locked
            invoiceType.setDisable(true);

            Dialog<Item> itemDialog = AddProductDialog.getSellDialogue(serial, isRetailer);

            Optional<Item> result = itemDialog.showAndWait();

            result.ifPresent(item -> {
                invoice.addData(item);
                serial++;

                //calculate the invoice price and update the price in textField
                invoice.calPrice();
                priceTxt.setText(String.valueOf(invoice.getPrice()));
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
            completeInvoice();
            printNode();
        });
        saveBtn.setOnAction((event -> {

            completeInvoice();
            Customer customer = invoice.getTrader();

            //creating transaction
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

                        //updating the data so that the stock in the ui would be updated.
                        //if I don't do this, then when the seller try to sell to multiple
                        // customer the stock in the ui don't tell the current stock
                        AddProductDialog.setDataProduct(DataHelper.getProduct());
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

    private void completeInvoice(){
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

        //setting due to the customer
        Customer customer = invoice.getTrader();
        customer.setDue(customer.getDue()+Double.parseDouble(dueTxt.getText()));

        //adding data to invoice before saving
        invoice.setDate(datePick.getEditor().getText());
        invoice.setTransactionID(invoiceTxt.getText());
        invoice.setAuthorityName(soldByTxt.getText());
        invoice.setPaid(Double.parseDouble(paidTxt.getText()));
        double vat;
        try{
            vat = Double.parseDouble(vatTxt.getText());
        }catch (Exception e){
            vat = 0;
        }
        invoice.setVat(vat);
    }

    //method to clear all the textfield and tableview after save to databes
    private void allClear(){
        cusTxt.clear();
        dueTxt.clear();
        totalTxt.clear();
        strTxt.clear();
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
        grdPan.setMinHeight(GRID_HEIGHT);
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
            int stock = data.getProduct().getStock();
            if(event.getNewValue()>stock){
                Toast.makeText(Main.getMainStage(), String.format("Quantity must be less than or equalt to %d", stock), 1500, 500,500);
                itemTable.getItems().setAll(invoice.getData());
            }
            else {

                invoice.getData().get(data.getSerial() - 1).setQuantity(event.getNewValue());
                invoice.getData().get(data.getSerial() - 1).calTotal();
                invoice.calPrice();
                priceTxt.setText(String.valueOf(invoice.getPrice()));

            }
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

    private void printNode(){
        Parent node;
        int size = invoice.getData().size();
        int pages = (size/29) + 1;
        if(size % 29 > 19 ) pages++;
        try {
            Printer printer = Printer.getDefaultPrinter();
            PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);
            PrinterJob job = PrinterJob.createPrinterJob();


            setBtnVisible(false);

            if (job != null ) {
                boolean success = false;

                for(int i=1;i<=pages;i++){
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/print/invoiceSell.fxml"));
                    node = loader.load();
                    InvoiceSell invoiceSell = loader.getController();
                    invoiceSell.preparePage(invoice, pages, i);
                    success = job.printPage(pageLayout, node);
                }
                if (success) {
                    job.endJob();

                }
            }
            setBtnVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setEditable(boolean editable){
        adrsTxt.setEditable(editable);
        phnTxt.setEditable(editable);
        emailTxt.setEditable(editable);
        strTxt.setEditable(editable);
        invoiceType.setDisable(!editable);
    }

    private void setBtnVisible(boolean p){
        saveBtn.setVisible(p);
        addBtn.setVisible(p);
        printBtn.setVisible(p);
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
            dueTxt.setText(String.format("%.2f",due));
        }
    }

    private void updateTotalTxt(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        double price = 0;
        double vat = 0;
        try{
            price = Double.parseDouble(priceTxt.getText());
            vat = Double.parseDouble(vatTxt.getText());
        }catch (NumberFormatException e){
            System.out.println("SellView: VatText property Exception");
        }finally {
            double total = price+((price*vat)/100);
            totalTxt.setText(String.format("%.2f", total));
        }
    }

    public static Invoice<Customer> getInvoice(){
        return SellView.invoice;
    }

    //todo-me add a logic so that a product can't be added twice in the invoice

}
