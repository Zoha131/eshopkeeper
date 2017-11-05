package model;

public class Product {
    private int id,stock;
    private String name, code, company;
    private double prate, wrate, rrate;

    public Product(int id, int stock, String name, String code, String company, double prate, double wrate, double rrate) {
        this.id = id;
        this.stock = stock;
        this.name = name;
        this.code = code;
        this.company = company;
        this.prate = prate;
        this.wrate = wrate;
        this.rrate = rrate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public double getPrate() {
        return prate;
    }

    public void setPrate(double prate) {
        this.prate = prate;
    }

    public double getWrate() {
        return wrate;
    }

    public void setWrate(double wrate) {
        this.wrate = wrate;
    }

    public double getRrate() {
        return rrate;
    }

    public void setRrate(double rrate) {
        this.rrate = rrate;
    }
}
