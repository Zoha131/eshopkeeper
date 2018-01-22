package com.zoha131.eshopkeeper.print;

import com.zoha131.eshopkeeper.converter.CashWordConverter;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import com.zoha131.eshopkeeper.model.Customer;
import com.zoha131.eshopkeeper.model.Invoice;
import com.zoha131.eshopkeeper.model.Item;

import java.util.List;
import java.util.stream.Collectors;

public class InvoiceSell {
    @FXML    Label nameLbl, phnLbl, adrsLbl, idLbl, dateLbl,soldByLbl, priceLbl, vatLbl, totalLbl, paidLbl, dueLbl, amntLbl, pageLbl;
    @FXML    TableView<Item> itemTable;

    private  Customer customer;
    private double price, vat, paid, total, due;
    private List<Item> items;

    public void initialize(){
        setTableColumn();
    }

    private void setTableColumn(){
        TableColumn<Item, Integer> serialColumn = new TableColumn<>("SL");
        serialColumn.setCellValueFactory(new PropertyValueFactory<>("serial"));

        TableColumn<Item, String> itemDesc = new TableColumn<>("Item Description");
        itemDesc.setCellValueFactory(new PropertyValueFactory<>("description"));


        TableColumn<Item, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Item, Double> rateColumn = new TableColumn<>("Rate");
        rateColumn.setCellValueFactory(new PropertyValueFactory<>("rate"));

        TableColumn<Item, Double> totalColumn = new TableColumn<>("Total");
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        itemTable.getColumns().addAll(serialColumn, itemDesc, quantityColumn, rateColumn, totalColumn);
        //todo-me to set the column width for better UI

        itemTable.setFixedCellSize(20);

    }

    public void preparePage(Invoice<Customer> invoice, int totalPage, int curPage){

        int lower = (curPage - 1) * 29;
        int upper = lower + 29;

        int size = invoice.getData().size();

        items = invoice.getData().stream().filter(item -> {
            return item.getSerial() > lower && item.getSerial() <= upper;
        }).collect(Collectors.toList());
        GridPane.setRowSpan(itemTable, 8);


        if(totalPage-curPage == 0){
            customer = invoice.getTrader();
            price = invoice.getPrice();
            vat = invoice.getVat();
            paid = invoice.getPaid();
            total = price + ((price * vat)/100);
            due = total - paid;

            nameLbl.setText(customer.getName());
            phnLbl.setText(customer.getPhone());
            adrsLbl.setText(customer.getAddress());
            idLbl.setText(invoice.getTransactionID());
            dateLbl.setText(invoice.getDate());
            soldByLbl.setText(invoice.getAuthorityName());
            priceLbl.setText(String.format("%.2f", price));
            vatLbl.setText(String.format("%.2f", vat));
            totalLbl.setText(String.format("%.2f", total));
            paidLbl.setText(String.format("%.2f", paid));
            dueLbl.setText(String.format("%.2f", due));
            amntLbl.setText(CashWordConverter.doubleConvert(total));

            GridPane.setRowSpan(itemTable, 1);
        }

        itemTable.setItems(FXCollections.observableArrayList(items));
        pageLbl.setText(String.format("Page %d of %d", curPage, totalPage));
    }
}
