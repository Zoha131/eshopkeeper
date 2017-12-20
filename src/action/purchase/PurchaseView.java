package action.purchase;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import model.*;

public class PurchaseView {
    @FXML TextField supNameTxt, strNameTxt, adrsTxt, phnTxt, idTxt, purchasedByTxt;
    @FXML DatePicker datePick;
    @FXML Button addBtn, addNewBtn, saveBtn;

    private ScrollPane scrollPane;

    private final double GRID_HEIGHT=1000;
    private int scrollTime = 0;
    private boolean isScrollable = true;

    ObservableList<String> dataSupplierName, dataProductName;
    ObservableList<Customer> dataCustomer;
    ObservableList<Product> dataProduct;
    ModelStringConverter<Product> converterProduct;


    private Invoice<Customer> invoice;
    private int serial=0;
    private Product addProduct;
    private double tableHeight = 50;
    private DateStringConverter dateStringConverter;

    public void initialize(){

    }
}
