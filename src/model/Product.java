package model;

public class Product extends Model {
    private int stock;
    private String code, company;
    private double prate, wrate, rrate;

    public Product(int id, int stock, String name, String code, String company, double prate, double wrate, double rrate) {
        super(id, name);
        this.stock = stock;
        this.code = code;
        this.company = company;
        this.prate = prate;
        this.wrate = wrate;
        this.rrate = rrate;
    }

    public Product(){
        this(0,0,"product","404","company",0,0,0);
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
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

    public boolean equals(Product o) {
        boolean equal = getName().equals(o.getName())
                && code.equals(o.getCode())
                && company.equals(o.getCompany())
                && prate == o.getPrate()
                && wrate == o.getWrate()
                && rrate == o.getRrate();

        return equal;
    }
}
