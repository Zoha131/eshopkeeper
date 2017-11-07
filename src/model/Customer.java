package model;

public class Customer {
    int id;
    double due;
    String name, store, address, phone, email, type;

    public Customer(int id, double due, String name, String store, String address, String phone, String email, String type) {
        this.id = id;
        this.due = due;
        this.name = name;
        this.store = store;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.type = type;
    }

    public Customer(double due, String name, String store, String address, String phone, String email, String type) {
        this(0,due, name,store,address,phone,email,type);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDue() {
        return due;
    }

    public void setDue(double due) {
        this.due = due;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
