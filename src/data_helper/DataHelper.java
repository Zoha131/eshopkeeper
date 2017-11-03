package data_helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DataHelper {

    //Customer Table Columns
    private static final String CUS_ID = "id";
    private static final String CUS_NAME = "name";
    private static final String CUS_ADDRESS = "address";
    private static final String CUS_PHONE = "phone";
    private static final String CUS_EMAIL = "email";
    private static final String CUS_TYPE = "type";

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

    public static boolean createProductTable() {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE IF NOT EXISTS product ( ");
        query.append("'id' INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        query.append("'name' TEXT NOT NULL, ");
        query.append("'code' TEXT, ");
        query.append("'consumer_rate' DOUBLE NOT NULL, ");
        query.append("'holesale_rate' DOUBLE NOT NULL, ");
        query.append("'company' TEXT )");

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

    public static boolean insertProduct(String name, String code, Double consumer_rate, Double holesale_rate, String company){
        String query = "INSERT INTO product ( name, code, consumer_rate, holesale_rate, company) VALUES (?,?,?,?,?)";

        try {
            Connection conn = getConnection();
            PreparedStatement prep = conn.prepareStatement(query);
            prep.setString(1, name);
            prep.setString(2, code);
            prep.setDouble(3, consumer_rate);
            prep.setDouble(4, holesale_rate);
            prep.setString(5, company);
            prep.execute();

            conn.close();
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean createAllTables(){
        return creatCustomerTable() && createProductTable();
    }
}

enum Customer{
    id,
    name,
    phone,
    address,
    email,
    type,
    store,
    due
}

