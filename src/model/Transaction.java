package model;

public class Transaction {
    private int id, customerId;
    private double amount;
    private String date, transactionId, takenBy;

    public Transaction(int id, int customerId, double amount, String date, String transactionId, String takenBy) {
        this.id = id;
        this.customerId = customerId;
        this.amount = amount;
        this.date = date;
        this.transactionId = transactionId;
        this.takenBy = takenBy;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
