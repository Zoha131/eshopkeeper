package model;

import add.customer.AddCustomer;

public class Customer extends Model {
    double due;
    String store, address, phone, email, type;

    public Customer(int id, double due, String name, String store, String address, String phone, String email, String type) {
        super(id, name);
        this.due = due;
        this.store = store;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.type = type;
    }

    public Customer(double due, String name, String store, String address, String phone, String email, String type) {
        this(0,due, name,store,address,phone,email,type);
    }

    public Customer(){
        this(0,"Default Name", null, "Default Address", "01", "default@gmeil.com", "Retailer");
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRetailer(){
        return this.type.equals(AddCustomer.CusType.Retailer.toString());
    }
}
