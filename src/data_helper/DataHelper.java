package data_helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

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
        query.append(Customer.id);
        query.append(" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        query.append(Customer.name);
        query.append(" TEXT NOT NULL, ");
        query.append(Customer.store);
        query.append(" TEXT, ");
        query.append(Customer.address);
        query.append(" TEXT, ");
        query.append(Customer.phone);
        query.append(" TEXT NOT NULL, ");
        query.append(Customer.email);
        query.append(" TEXT, ");
        query.append(Customer.type);
        query.append(" TEXT NOT NULL, ");
        query.append(Customer.due);
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

    public static boolean insertCustomer(String name, String address, String phone,
                                         String email, String type, String store, double due){

        String query = "INSERT INTO customer ( name, address, phone, email, type) VALUES (?,?,?,?,?)";
        String query1 = String.format("INSERT INTO customer ( %s, %s, %s, %s, %s, %s, %s) VALUES (?,?,?,?,?,?,?)",
                Customer.name,
                Customer.address,
                Customer.phone,
                Customer.email,
                Customer.type,
                Customer.store,
                Customer.due);

        try {
            Connection conn = getConnection();
            PreparedStatement prep = conn.prepareStatement(query1);
            prep.setString(1, name);
            prep.setString(2, address);
            prep.setString(3, phone);
            prep.setString(4, email);
            prep.setString(5, type);
            prep.setString(6, store);
            prep.setDouble(7, due);
            prep.execute();

            prep.close();
            conn.close();
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }


    public static boolean creatSupplierTable(){
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE IF NOT EXISTS supplier ( ");
        query.append(Supplier.id);
        query.append(" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        query.append(Supplier.name);
        query.append(" TEXT NOT NULL, ");
        query.append(Supplier.store);
        query.append(" TEXT NOT NULL, ");
        query.append(Supplier.address);
        query.append(" TEXT, ");
        query.append(Supplier.phone);
        query.append(" TEXT NOT NULL, ");
        query.append(Supplier.email);
        query.append(" TEXT, ");
        query.append(Supplier.due);
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


    public static boolean insertSupplier(String name, String store, String address, String phone,
                                         String email, double due){

        String query1 = String.format("INSERT INTO supplier ( %s, %s, %s, %s, %s, %s) VALUES (?,?,?,?,?,?)",
                Supplier.name,
                Supplier.store,
                Supplier.address,
                Supplier.phone,
                Supplier.email,
                Supplier.due);

        try {
            Connection conn = getConnection();
            PreparedStatement prep = conn.prepareStatement(query1);
            prep.setString(1, name);
            prep.setString(2, store);
            prep.setString(3, address);
            prep.setString(4, phone);
            prep.setString(5, email);
            prep.setDouble(6, due);
            prep.execute();

            prep.close();
            conn.close();
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean createProductTable() {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE IF NOT EXISTS product ( ");
        query.append(Product.id);
        query.append(" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        query.append(Product.name);
        query.append(" TEXT NOT NULL, ");
        query.append(Product.code);
        query.append(" TEXT, ");
        query.append(Product.company);
        query.append(" TEXT NOT NULL, ");
        query.append(Product.prate);
        query.append(" DOUBLE DEFAULT 0.00, ");
        query.append(Product.wrate);
        query.append(" DOUBLE NOT NULL, ");
        query.append(Product.rrate);
        query.append(" DOUBLE NOT NULL, ");
        query.append(Product.stock);
        query.append(" DOUBLE NOT NULL ) ");


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

    public static boolean insertProduct(String name, String code, String company,
                                        double prate, double wrate, double rrate, double stock ){
        //String query = "INSERT INTO product ( name, code, consumer_rate, holesale_rate, company) VALUES (?,?,?,?,?)";
        String query =
                String.format("INSERT INTO product ( %s, %s, %s, %s, %s, %s, %s) VALUES (?,?,?,?,?,?,?)",
                        Product.name, Product.code, Product.company, Product.prate,
                        Product.wrate, Product.rrate, Product.stock);

        try {
            Connection conn = getConnection();
            PreparedStatement prep = conn.prepareStatement(query);
            prep.setString(1, name);
            prep.setString(2, code);
            prep.setString(3, company);
            prep.setDouble(4, prate);
            prep.setDouble(5, wrate);
            prep.setDouble(6, rrate);
            prep.setDouble(7, stock);
            prep.execute();

            conn.close();
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean createAllTables(){
        return creatCustomerTable() &&
                createProductTable() &&
                creatSupplierTable();
    }

    public static enum Customer{
        id,
        name,
        phone,
        address,
        email,
        type,
        store,
        due
    }
    public static enum Product{
        id,
        name,
        code,
        company,
        prate,
        wrate,
        rrate,
        stock

    }
    public static enum Supplier{
        id,
        name,
        phone,
        address,
        email,
        store,
        due
    }
}



