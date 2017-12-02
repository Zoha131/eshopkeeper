package model;

public class Supplier extends Model {
    double due;
    String store, address, phone, email;

    public Supplier(int id, double due, String name, String store, String address, String phone, String email) {
        super(id, name);
        this.due = due;
        this.store = store;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    public Supplier(){
        this(0, 0.00, "Default Name", "Default Store", "Default Address","017","default@gmail.com");
    }

    public double getDue() {
        return due;
    }

    public void setDue(double due) {
        this.due = due;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
