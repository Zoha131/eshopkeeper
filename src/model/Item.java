package model;

public class Item{
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

    public Item(int serial, Product product, double prate, int quantity) {
        this.serial = serial;
        this.product = product;
        this.quantity = quantity;
        this.rate = prate;
        this.total = quantity* rate;
        this.description = product.getName()+" ("+product.getCode()+")";
    }

    public Item() {
    }

    public void constuctItem(int serial, boolean isRetailer, int quantity) {
        this.serial = serial;
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

    public double getRrate(){
        return product.getRrate();
    }

    public void setRrate(double rrate){
        product.setRrate(rrate);
    }

    public double getWrate(){
        return product.getWrate();
    }

    public void setWrate(double wrate){
        product.setWrate(wrate);
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
