package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

public class Invoice {
    private ObservableList<Item> data;
    private double price, vat;
    private String amountInStr, date, transactionID, soldBy;
    private Customer customer;

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

    public String getSoldBy() {
        return soldBy;
    }

    public void setSoldBy(String soldBy) {
        this.soldBy = soldBy;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Item> getData() {
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
            this.price += item.total;
        }
    }

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }

    public String getAmountInStr() {
        return amountInStr;
    }

    public void setAmountInStr(String amountInStr) {
        this.amountInStr = amountInStr;
    }

    public static class Item{
        private int serial;
        private Product product;
        private int quantity;
        private double rate, total;
        private String description;

        public Item(int serial, Product product, boolean isRetailer, int quantity) {
            this.serial = serial;
            this.product = product;
            this.quantity = quantity;
            if(isRetailer){
                this.rate = product.getRrate();
            }
            else {
                this.rate = product.getWrate();
            }
            this.total = quantity* rate;
            this.description = product.getName()+" ("+product.getCode()+")";
        }

        public Item() {
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public void calTotal(){
            this.total = this.rate *this.quantity;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getSerial() {
            return serial;
        }

        public void setSerial(int serial) {
            this.serial = serial;
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
