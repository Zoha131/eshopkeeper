package model;

import converter.DateStringConverter;

import java.time.LocalDate;

public class SellHistory {
    private String name, soldBy, strDate, company;
    private LocalDate date;
    private double rate, amount;
    private int quantity, prductId;

    private DateStringConverter converter;

    public SellHistory(String name, String company, String soldBy, String date, double rate, int quantity, int prductId) {
        converter = new DateStringConverter();
        this.name = name;
        this.company = company;
        this.soldBy = soldBy;
        this.date = converter.fromString(date);
        this.strDate = date;
        this.rate = rate;
        this.quantity = quantity;
        this.amount = rate*quantity;
        this.prductId = prductId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrductId() {
        return prductId;
    }

    public void setPrductId(int prductId) {
        this.prductId = prductId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public String getSoldBy() {
        return soldBy;
    }

    public void setSoldBy(String soldBy) {
        this.soldBy = soldBy;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
