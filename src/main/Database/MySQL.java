package main.Database;
import java.sql.*;
public class MySQL {
    private final static String URL = "jdbc:mysql://localhost:3306/GymRAWR";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "root";

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
    
}
