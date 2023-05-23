package main.Objects;

import java.sql.*;
import java.util.ArrayList;

import main.Database.MySQL;
import main.Miscellanous.Messages;

public class Products {

    public static ArrayList<Products> productList = new ArrayList<Products>();

    private int productID, productType, productDayDuration;
    private String productName;
    private double productPrice;

    private Products(int id, String name, int type, double price, int duration){
        productID = id;
        productName = name;
        productType = type;
        productPrice = price;
        productDayDuration = duration;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductDayDuration(int productDayDuration) {
        this.productDayDuration = productDayDuration;
    }

    public int getProductDayDuration() {
        return productDayDuration;
    }

    public static ArrayList<Products> getProductList() {
        return productList;
    }
    
    public static void initializeProducts(){
        try (Connection conn = MySQL.getConnection()){
            PreparedStatement statement = conn.prepareStatement("select * from products");
            ResultSet result = statement.executeQuery();
            while(result.next()){
                int productID = result.getInt("product_id");
                String productName = result.getString("product_name");
                int productType = result.getInt("product_type"); //if 0 = non-mem, 1 = mem, 2 = trainer
                double productPrice = result.getDouble("product_price");
                int productDuration = result.getInt("product_duration_days");

                Products product = new Products(productID, productName, productType, productPrice, productDuration);
                productList.add(product);
            }
            conn.close();
        } catch (SQLException e) {
            Messages.databaseConnectionFailed();
            e.printStackTrace();
        }
    }
}
