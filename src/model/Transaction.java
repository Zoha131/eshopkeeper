package model;

import converter.MyDateConverter;

import java.time.LocalDate;

public class Transaction {
    private int id, customerId;
    private double paid, total;
    private String date, transactionId, takenBy;
    private LocalDate localDate;
    private MyDateConverter myDateConverter;

    public Transaction(int id, int customerId, double paid, String date, String transactionId, String takenBy) {
        this.id = id;
        this.customerId = customerId;
        this.paid = paid;
        this.date = date;
        this.transactionId = transactionId;
        this.takenBy = takenBy;
    }

    public Transaction(int id, int customerId, double total, double paid, String date, String transactionId, String takenBy) {
        myDateConverter = new MyDateConverter();
        this.id = id;
        this.customerId = customerId;
        this.paid = paid;
        this.total = total;
        this.date = date;
        this.transactionId = transactionId;
        this.takenBy = takenBy;
        this.localDate = myDateConverter.fromString(date);
    }

    public Transaction() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getPaid() {
        return paid;
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTakenBy() {
        return takenBy;
    }

    public void setTakenBy(String takenBy) {
        this.takenBy = takenBy;
    }
}
