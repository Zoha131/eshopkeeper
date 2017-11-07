package data_helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import model.Product;
import model.Supplier;

import java.sql.*;

public class DataHelper {

    private static Connection conn;


    public static Connection getConnection(){
        try{
            conn = DriverManager.getConnection("jdbc:sqlite:eshopkeeper.db");
            return conn;
        }catch (Exception e){
            System.out.println("sqlite Connection failed "+e.getMessage());
        }

        return null;
    }

    public static boolean creatCustomerTable(){
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE IF NOT EXISTS customer ( ");
        query.append(customer.id);
        query.append(" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        query.append(customer.name);
        query.append(" TEXT NOT NULL, ");
        query.append(customer.store);
        query.append(" TEXT, ");
        query.append(customer.address);
        query.append(" TEXT, ");
        query.append(customer.phone);
        query.append(" TEXT NOT NULL, ");
        query.append(customer.email);
        query.append(" TEXT, ");
        query.append(customer.type);
        query.append(" TEXT NOT NULL, ");
        query.append(customer.due);
        query.append(" REAL NOT NULL DEFAULT 0.00 ) ");

        try{
            Connection conn = getConnection();
            PreparedStatement prp = conn.prepareStatement(query.toString());
            prp.execute();

            prp.close();
            conn.close();
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    public static boolean insertCustomer(Customer cus){

        String query = "INSERT INTO customer ( name, address, phone, email, type) VALUES (?,?,?,?,?)";
        String query1 = String.format("INSERT INTO customer ( %s, %s, %s, %s, %s, %s, %s) VALUES (?,?,?,?,?,?,?)",
                customer.name,
                customer.address,
                customer.phone,
                customer.email,
                customer.type,
                customer.store,
                customer.due);

        try {
            Connection conn = getConnection();
            PreparedStatement prep = conn.prepareStatement(query1);
            prep.setString(1, cus.getName());
            prep.setString(2, cus.getAddress());
            prep.setString(3, cus.getPhone());
            prep.setString(4, cus.getEmail());
            prep.setString(5, cus.getType());
            prep.setString(6, cus.getStore());
            prep.setDouble(7, cus.getDue());
            prep.execute();

            prep.close();
            conn.close();
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    public static ObservableList<Customer> getCustomer(){
        ObservableList<Customer> data = FXCollections.observableArrayList();
        String query = "SELECT * FROM customer ORDER BY id";

        try {
            ResultSet res = getConnection().createStatement().executeQuery(query);
            while (res.next()){
                Customer raw = new Customer(res.getInt(customer.id.toString()),
                        res.getDouble(customer.due.toString()),
                        res.getString(customer.name.toString()),
                        res.getString(customer.store.toString()),
                        res.getString(customer.address.toString()),
                        res.getString(customer.phone.toString()),
                        res.getString(customer.email.toString()),
                        res.getString(customer.type.toString()));
                data.add(raw);
            }

            return data;
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    public static boolean creatSupplierTable(){
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE IF NOT EXISTS supplier ( ");
        query.append(supplier.id);
        query.append(" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        query.append(supplier.name);
        query.append(" TEXT NOT NULL, ");
        query.append(supplier.store);
        query.append(" TEXT NOT NULL, ");
        query.append(supplier.address);
        query.append(" TEXT, ");
        query.append(supplier.phone);
        query.append(" TEXT NOT NULL, ");
        query.append(supplier.email);
        query.append(" TEXT, ");
        query.append(supplier.due);
        query.append(" REAL NOT NULL DEFAULT 0.00 ) ");

        try{
            Connection conn = getConnection();
            PreparedStatement prp = conn.prepareStatement(query.toString());
            prp.execute();

            prp.close();
            conn.close();
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    public static boolean insertSupplier(Supplier sup){

        String query1 = String.format("INSERT INTO supplier ( %s, %s, %s, %s, %s, %s) VALUES (?,?,?,?,?,?)",
                supplier.name,
                supplier.store,
                supplier.address,
                supplier.phone,
                supplier.email,
                supplier.due);

        try {
            Connection conn = getConnection();
            PreparedStatement prep = conn.prepareStatement(query1);
            prep.setString(1, sup.getName());
            prep.setString(2, sup.getStore());
            prep.setString(3, sup.getAddress());
            prep.setString(4, sup.getPhone());
            prep.setString(5, sup.getEmail());
            prep.setDouble(6, sup.getDue());
            prep.execute();

            prep.close();
            conn.close();
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    public static ObservableList<Supplier> getSupplier(){
        ObservableList<Supplier> data = FXCollections.observableArrayList();
        String query = "SELECT * FROM supplier ORDER BY id";

        try {
            ResultSet res = getConnection().createStatement().executeQuery(query);
            while (res.next()){
                Supplier raw = new Supplier(res.getInt(customer.id.toString()),
                        res.getDouble(customer.due.toString()),
                        res.getString(customer.name.toString()),
                        res.getString(customer.store.toString()),
                        res.getString(customer.address.toString()),
                        res.getString(customer.phone.toString()),
                        res.getString(customer.email.toString()));
                data.add(raw);
            }
            return data;
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    public static boolean createProductTable() {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE IF NOT EXISTS product ( ");
        query.append(product.id);
        query.append(" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        query.append(product.name);
        query.append(" TEXT NOT NULL, ");
        query.append(product.code);
        query.append(" TEXT, ");
        query.append(product.company);
        query.append(" TEXT NOT NULL, ");
        query.append(product.prate);
        query.append(" DOUBLE DEFAULT 0.00, ");
        query.append(product.wrate);
        query.append(" DOUBLE NOT NULL, ");
        query.append(product.rrate);
        query.append(" DOUBLE NOT NULL, ");
        query.append(product.stock);
        query.append(" INTEGER NOT NULL ) ");


        try {
            Connection conn = getConnection();
            PreparedStatement prp = conn.prepareStatement(query.toString());
            prp.execute();

            conn.close();
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    public static boolean insertProduct(Product prod){
        //String query = "INSERT INTO product ( name, code, consumer_rate, holesale_rate, company) VALUES (?,?,?,?,?)";
        String query = String.format("INSERT INTO product ( %s, %s, %s, %s, %s, %s, %s) VALUES (?,?,?,?,?,?,?)",
                        product.name, product.code, product.company, product.prate,
                        product.wrate, product.rrate, product.stock);

        try {
            Connection conn = getConnection();
            PreparedStatement prep = conn.prepareStatement(query);
            prep.setString(1, prod.getName());
            prep.setString(2, prod.getCode());
            prep.setString(3, prod.getCompany());
            prep.setDouble(4, prod.getPrate());
            prep.setDouble(5, prod.getWrate());
            prep.setDouble(6, prod.getRrate());
            prep.setInt(7, prod.getStock());
            prep.execute();

            conn.close();
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    public static ObservableList<Product> getProduct(){
        ObservableList<Product> data = FXCollections.observableArrayList();
        String query = String.format("SELECT * FROM product ORDER BY id");
        try {
            ResultSet res = getConnection().createStatement().executeQuery(query);
            while (res.next()){
                Product raw = new Product(res.getInt(product.id.toString()),
                        res.getInt(product.stock.toString()),
                        res.getString(product.name.toString()),
                        res.getString(product.code.toString()),
                        res.getString(product.company.toString()),
                        res.getDouble(product.prate.toString()),
                        res.getDouble(product.wrate.toString()),
                        res.getDouble(product.rrate.toString()));
                data.add(raw);
            }
            return data;
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    public static boolean updateData(String table, String column, String value, int id){
        String query = String.format("UPDATE %s SET %s='%s' WHERE id=%d",table,column,value,id);
        return execute(query);
    }

    public static boolean updateData(String table, String column, double value, int id){
        String query = String.format("UPDATE %s SET %s=%f WHERE id=%d",table,column,value,id);
        return execute(query);
    }

    public static boolean updateData(String table, String column, int value, int id){
        String query = String.format("UPDATE %s SET %s=%d WHERE id=%d",table,column,value,id);
        return execute(query);
    }

    public static boolean deleteData(String table, int id){
        String query = String.format("DELETE FROM %s WHERE id=%d",table,id);
        return execute(query);
    }

    private static boolean execute(String query){
        try {
            Connection con = getConnection();
            Statement std = con.createStatement();
            std.execute(query);
            std.close();
            con.close();
            return true;
        }catch (Exception e){
            System.out.println("DataHelper:: "+e);
            return false;
        }
    }

    public static boolean createAllTables(){
        return creatCustomerTable() &&
                createProductTable() &&
                creatSupplierTable();
    }

    public static enum customer {
        id,
        name,
        phone,
        address,
        email,
        type,
        store,
        due
    }
    public static enum product {
        id,
        name,
        code,
        company,
        prate,
        wrate,
        rrate,
        stock

    }
    public static enum supplier {
        id,
        name,
        phone,
        address,
        email,
        store,
        due
    }
}



