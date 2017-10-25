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
        query.append(CUS_ID);
        query.append(" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        query.append(CUS_NAME);
        query.append(" TEXT NOT NULL, ");
        query.append(CUS_ADDRESS);
        query.append(" TEXT, ");
        query.append(CUS_PHONE);
        query.append(" TEXT NOT NULL, ");
        query.append(CUS_EMAIL);
        query.append(" TEXT, ");
        query.append(CUS_TYPE);
        query.append(" TEXT NOT NULL )");

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

    public static boolean insertCustomer(String name, String address, String phone, String email, String type){

        String query = "INSERT INTO customer ( name, address, phone, email, type) VALUES (?,?,?,?,?)";

        try {
            Connection conn = getConnection();
            PreparedStatement prep = conn.prepareStatement(query);
            prep.setString(1, name);
            prep.setString(2, address);
            prep.setString(3, phone);
            prep.setString(4, email);
            prep.setString(5, type);
            prep.execute();

            prep.close();
            conn.close();
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
}
