package data_helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;

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
        query.append(" REAL DEFAULT 0.00 ) ");

        /*try{
            Connection conn = getConnection();
            PreparedStatement prp = conn.prepareStatement(query.toString());
            prp.execute();

            prp.close();
            conn.close();
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }*/

        return execute(query.toString());
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

        /*try{
            Connection conn = getConnection();
            PreparedStatement prp = conn.prepareStatement(query.toString());
            prp.execute();

            prp.close();
            conn.close();
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }*/
        return execute(query.toString());
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


        /*try {
            Connection conn = getConnection();
            PreparedStatement prp = conn.prepareStatement(query.toString());
            prp.execute();

            conn.close();
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }*/

        return execute(query.toString());
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

    public static boolean createSellTransactionTable(){
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE IF NOT EXISTS sellTransaction ( ");
        query.append(sellTransaction.id);
        query.append(" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        query.append(sellTransaction.transactionId);
        query.append(" TEXT NOT NULL UNIQUE, ");
        query.append(sellTransaction.customerId);
        query.append(" INTEGER NOT NULL, ");
        query.append(sellTransaction.total);
        query.append(" DOUBLE DEFAULT 0.0, ");
        query.append(sellTransaction.paid);
        query.append(" DOUBLE NOT NULL, ");
        query.append(sellTransaction.takenBy);
        query.append(" TEXT NOT NULL, ");
        query.append(sellTransaction.date);
        query.append(" TEXT NOT NULL ) ");

        return execute(query.toString());

    }
    public static boolean insertSellTransaction(Transaction transaction){
        String query = String.format("INSERT INTO sellTransaction ( %s, %s, %s, %s, %s, %s) VALUES (?,?,?,?,?,?)",
                sellTransaction.transactionId, sellTransaction.customerId, sellTransaction.total,
                sellTransaction.paid, sellTransaction.date, sellTransaction.takenBy);

        try {
            Connection conn = getConnection();
            PreparedStatement prep = conn.prepareStatement(query);
            prep.setString(1, transaction.getTransactionId());
            prep.setInt(2, transaction.getCustomerId());
            prep.setDouble(3, transaction.getTotal());
            prep.setDouble(4, transaction.getPaid());
            prep.setString(5, transaction.getDate());
            prep.setString(6, transaction.getTakenBy());
            prep.execute();

            conn.close();
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }

    }
    public static ObservableList<Transaction> getPurchaseTransaction(int supplierId){
        ObservableList<Transaction> data = FXCollections.observableArrayList();
        String query = String.format("SELECT * FROM purchaseTransaction WHERE supplierId = %d ORDER BY id", supplierId);

        try {
            ResultSet res = getConnection().createStatement().executeQuery(query);
            while (res.next()){
                Transaction raw = new Transaction(res.getInt(purchaseTransaction.id.toString()),
                        res.getInt(purchaseTransaction.supplierId.toString()),
                        res.getDouble(purchaseTransaction.total.toString()),
                        res.getDouble(purchaseTransaction.paid.toString()),
                        res.getString(purchaseTransaction.date.toString()),
                        res.getString(purchaseTransaction.transactionId.toString()),
                        res.getString(purchaseTransaction.takenBy.toString()));
                data.add(raw);
            }
            return data;
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    public static boolean createSellTable(){
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE IF NOT EXISTS sell ( ");
        query.append(sell.id);
        query.append(" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        query.append(sell.customerId);
        query.append(" INTEGER NOT NULL, ");
        query.append(sell.productID);
        query.append(" INTEGER NOT NULL, ");
        query.append(sell.rate);
        query.append(" DOUBLE NOT NULL, ");
        query.append(sell.quantity);
        query.append(" INTEGER NOT NULL, ");
        query.append(sell.date);
        query.append(" TEXT NOT NULL, ");
        query.append(sell.transactionID);
        query.append(" TEXT NOT NULL, ");
        query.append(sell.soldby);
        query.append(" TEXT NOT NULL ) ");

        return execute(query.toString());
    }
    public static boolean insertSell(Invoice<Customer> invoice) {
        try {
            String query = String.format("INSERT INTO sell ( %s, %s, %s, %s, %s, %s, %s) VALUES (?,?,?,?,?,?,?)",
                    sell.customerId, sell.productID, sell.rate, sell.quantity,
                    sell.date, sell.transactionID, sell.soldby);

            Connection conn = getConnection();

            for (Item item : invoice.getData()) {
                PreparedStatement prep = conn.prepareStatement(query);
                prep.setInt(1, invoice.getTrader().getId());
                prep.setInt(2, item.getProduct().getId());
                prep.setDouble(3, item.getRate());
                prep.setInt(4, item.getQuantity());
                prep.setString(5, invoice.getDate());
                prep.setString(6, invoice.getTransactionID());
                prep.setString(7, invoice.getAuthorityName());
                prep.execute();
                updateData("product", "stock", item.getProduct().getStock()-item.getQuantity(), item.getProduct().getId());
            }

            conn.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public static ObservableList<SellHistory> getSellHistory(int customerId){
        ObservableList<SellHistory> data = FXCollections.observableArrayList();
        StringBuilder query = new StringBuilder();
        query.append("SELECT name, code, company, date, rate, quantity, soldby, product.id as productId ");
        query.append("FROM sell, product ");
        query.append("WHERE sell.productID = product.id AND sell.customerId = ? ");
        query.append("ORDER BY sell.id ");
        try {
            PreparedStatement prep = conn.prepareStatement(query.toString());
            prep.setInt(1, customerId);
            ResultSet res = prep.executeQuery();
            while (res.next()){
                SellHistory raw = new SellHistory(
                        res.getString(product.name.toString()) +" ("+ res.getString(product.code.toString()) + ")",
                        res.getString(product.company.toString()),
                        res.getString(sell.soldby.toString()),
                        res.getString(sell.date.toString()),
                        res.getDouble(sell.rate.toString()),
                        res.getInt(sell.quantity.toString()),
                        res.getInt("productId"));
                data.add(raw);
            }
            return data;
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    public static boolean createPurchaseTransactionTable(){
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE IF NOT EXISTS purchaseTransaction ( ");
        query.append(purchaseTransaction.id);
        query.append(" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        query.append(purchaseTransaction.transactionId);
        query.append(" TEXT NOT NULL UNIQUE, ");
        query.append(purchaseTransaction.supplierId);
        query.append(" INTEGER NOT NULL, ");
        query.append(purchaseTransaction.total);
        query.append(" DOUBLE DEFAULT 0.0, ");
        query.append(purchaseTransaction.paid);
        query.append(" DOUBLE NOT NULL, ");
        query.append(purchaseTransaction.takenBy);
        query.append(" TEXT NOT NULL, ");
        query.append(purchaseTransaction.date);
        query.append(" TEXT NOT NULL ) ");

        return execute(query.toString());

    }
    public static boolean insertPurchaseTransaction(Transaction transaction){
        String query = String.format("INSERT INTO purchaseTransaction ( %s, %s, %s, %s, %s, %s) VALUES (?,?,?,?,?,?)",
                purchaseTransaction.transactionId, purchaseTransaction.supplierId,
                purchaseTransaction.total,purchaseTransaction.paid, purchaseTransaction.date, purchaseTransaction.takenBy);

        try {
            Connection conn = getConnection();
            PreparedStatement prep = conn.prepareStatement(query);
            prep.setString(1, transaction.getTransactionId());
            prep.setInt(2, transaction.getCustomerId());
            prep.setDouble(3, transaction.getTotal());
            prep.setDouble(4, transaction.getPaid());
            prep.setString(5, transaction.getDate());
            prep.setString(6, transaction.getTakenBy());
            prep.execute();

            conn.close();
            return true;
        }catch (Exception e){
            System.out.println("PurchaseTransaction Insert "+e);
            return false;
        }

    }
    public static ObservableList<Transaction> getSellTransaction(int customerId){
        ObservableList<Transaction> data = FXCollections.observableArrayList();
        String query = String.format("SELECT * FROM sellTransaction WHERE customerId = %d ORDER BY id", customerId);

        try {
            ResultSet res = getConnection().createStatement().executeQuery(query);
            while (res.next()){
                Transaction raw = new Transaction(res.getInt(sellTransaction.id.toString()),
                        res.getInt(sellTransaction.customerId.toString()),
                        res.getDouble(sellTransaction.total.toString()),
                        res.getDouble(sellTransaction.paid.toString()),
                        res.getString(sellTransaction.date.toString()),
                        res.getString(sellTransaction.transactionId.toString()),
                        res.getString(sellTransaction.takenBy.toString()));
                data.add(raw);
            }
            return data;
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    public static boolean createPurchaseTable(){
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE IF NOT EXISTS purchase ( ");
        query.append(purchase.id);
        query.append(" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        query.append(purchase.supplierId);
        query.append(" INTEGER NOT NULL, ");
        query.append(purchase.productId);
        query.append(" INTEGER NOT NULL, ");
        query.append(purchase.rate);
        query.append(" DOUBLE NOT NULL, ");
        query.append(purchase.quantity);
        query.append(" INTEGER NOT NULL, ");
        query.append(purchase.date);
        query.append(" TEXT NOT NULL, ");
        query.append(purchase.transactionId);
        query.append(" TEXT NOT NULL, ");
        query.append(purchase.purchasedBy);
        query.append(" TEXT NOT NULL ) ");

        return execute(query.toString());
    }
    public static boolean insertPurchase(Invoice<Supplier> invoice) {
        try {
            String query = String.format("INSERT INTO purchase ( %s, %s, %s, %s, %s, %s, %s) VALUES (?,?,?,?,?,?,?)",
                    purchase.supplierId, purchase.productId, purchase.rate, purchase.quantity,
                    purchase.date, purchase.transactionId, purchase.purchasedBy);

            Connection conn = getConnection();

            for (Item item : invoice.getData()) {
                PreparedStatement prep = conn.prepareStatement(query);
                prep.setInt(1, invoice.getTrader().getId());
                prep.setInt(2, item.getProduct().getId());
                prep.setDouble(3, item.getRate());
                prep.setInt(4, item.getQuantity());
                prep.setString(5, invoice.getDate());
                prep.setString(6, invoice.getTransactionID());
                prep.setString(7, invoice.getAuthorityName());
                prep.execute();
                updateData("product", "stock", item.getQuantity()+item.getProduct().getStock(), item.getProduct().getId());
                updateData("product", "prate", item.getRate(), item.getProduct().getId());
                updateData("product", "rrate", item.getProduct().getRrate(), item.getProduct().getId());
                updateData("product", "wrate", item.getProduct().getWrate(), item.getProduct().getId());
            }

            conn.close();
            return true;
        } catch (Exception e) {
            System.out.println("Purchase Insert "+e);
            return false;
        }
    }
    public static ObservableList<SellHistory> getPurchaseHistory(int supplierId){
        ObservableList<SellHistory> data = FXCollections.observableArrayList();
        StringBuilder query = new StringBuilder();
        query.append("SELECT name, code, company, date, rate, quantity, purchasedBy, product.id as productId ");
        query.append("FROM purchase, product ");
        query.append("WHERE purchase.productID = product.id AND purchase.supplierId = ? ");
        query.append("ORDER BY purchase.id ");
        try {
            PreparedStatement prep = conn.prepareStatement(query.toString());
            prep.setInt(1, supplierId);
            ResultSet res = prep.executeQuery();
            while (res.next()){
                SellHistory raw = new SellHistory(
                        res.getString(product.name.toString()) +" ("+ res.getString(product.code.toString()) + ")",
                        res.getString(product.company.toString()),
                        res.getString(purchase.purchasedBy.toString()),
                        res.getString(purchase.date.toString()),
                        res.getDouble(purchase.rate.toString()),
                        res.getInt(purchase.quantity.toString()),
                        res.getInt("productId"));
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
                creatSupplierTable() &&
                createSellTransactionTable() &&
                createSellTable() &&
                createPurchaseTransactionTable() &&
                createPurchaseTable();
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
    public static enum sellTransaction {
        id,
        customerId,
        total,
        paid,
        date,
        transactionId,
        takenBy
    }
    public static enum sell {
        id,
        customerId,
        productID,
        rate,
        quantity,
        date,
        soldby,
        transactionID
    }
    public static enum purchaseTransaction {
        id,
        supplierId,
        total,
        paid,
        date,
        transactionId,
        takenBy
    }
    public static enum purchase {
        id,
        supplierId,
        productId,
        rate,
        quantity,
        date,
        purchasedBy,
        transactionId
    }
}



