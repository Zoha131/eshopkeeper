package com.zoha131.eshopkeeper.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Invoice<T> {
    private ObservableList<Item> data;
    private double price, vat, paid;
    private String date, transactionID, authorityName;
    private T trader;

    public Invoice() {
        data = FXCollections.observableArrayList();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    public T getTrader() {
        return trader;
    }

    public void setTrader(T trader) {
        this.trader = trader;
    }

    public ObservableList<Item> getData() {
        return data;
    }

    public void setData(ObservableList<Item> data) {
        this.data = data;
    }

    public void addData(Item item){
        this.data.add(item);
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void calPrice(){
        this.price=0;
        for(Item item: data){
            this.price += item.getTotal();
        }
    }

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }

    public double getPaid() {
        return paid;
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }
}
