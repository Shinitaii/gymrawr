package main.App.Main.SubPanels;
import javax.swing.*;
import main.Database.MySQL;
import main.Miscellanous.*;
import main.Objects.*;
import main.Properties.Hover;
import main.Properties.Custom.*;

import java.awt.*;
import java.sql.*;
import java.util.*;

public class POSPanel extends JPanel {
    private User user;
    private SalesReportPanel salesReportPanel;
    private ArrayList<ProductInCart> productInCarts;
    private JPanel productPanel, cartPanel, productTypePanel, buttonPanel;
    private CustomButton allProductButton, membershipButton, trainerButton, equipmentButton;
    private TextLabel totalPriceLabel, paymentPriceLabel, changePriceLabel;

    private JPanel orderPanel;

    public POSPanel(User user, SalesReportPanel salesReportPanel){
        this.user = user;
        this.salesReportPanel = salesReportPanel;
        setLayout(new BorderLayout());
        productInCarts = new ArrayList<>();
        initializeProductPanel();
        initializeCartPanel();
        initializeProductTypePanel();
        showProducts(0);
    }
    //productpanels
    private void initializeProductPanel(){
        productPanel = new JPanel(new GridBagLayout());
        productPanel.setBorder(BorderFactory.createLineBorder(Color.decode("#08145c"), 1));
        add(productPanel, BorderLayout.CENTER);
    }

    private void showProducts(int type){
        productPanel.removeAll();
        int x = 0, y = 0;
        for(Products product : Products.getProductList()){
            int id = product.getProductID();
            String name = product.getProductName();
            double price = product.getProductPrice();
            int productType = product.getProductType();
            int duration = product.getProductDayDuration();
            
            String productLetter = identifyType(productType);
            if(type == 0 || product.getProductType() == type) {
                if(x % 4 == 0){
                    y++;
                    x = 0;
                }
                CustomButton productButton = new CustomButton("<html><p style='font-size: 9px;'>"+id + "# - " +name + "<br>Price - ₱"+price+"</p></html>", null, e->addProductToCart(orderPanel,id, name, price, duration,productLetter));
                CommonComponent.addComponent(productPanel, productButton, x, y, 1, 1, GridBagConstraints.NORTH);
                x++;
            }
        }
        revalidate();
        repaint();
    }

    private String identifyType(int type){
        if(type == 1) return "M";
        else if(type == 2) return "T";
        else if(type == 3) return "E";
        else return "N";
    }
    //cartpanels
    private void initializeCartPanel(){
        cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.decode("#08145c")));
        cartPanel.add(new JLabel("---------------------------------------------------------------------"), BorderLayout.NORTH);
        JPanel mainOrderPanel = new JPanel(new BorderLayout());
        orderPanel = new JPanel(new GridBagLayout());
        mainOrderPanel.add(orderPanel, BorderLayout.NORTH);
        JScrollPane scrollPanel = new JScrollPane(mainOrderPanel);
        scrollPanel.setBorder(null);
        cartPanel.add(scrollPanel, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel totalPanel = new JPanel(new GridLayout(0,2));
        TextLabel totalLabel = new TextLabel("<html><b>Total:</b></html>", 12);
        totalLabel.setForeground(Color.RED);
        totalPanel.add(totalLabel);
        totalPriceLabel = new TextLabel("₱0.00", 12);
        totalPriceLabel.setForeground(Color.RED);
        totalPanel.add(totalPriceLabel);
        TextLabel paymentLabel = new TextLabel("<html><b>Payment:</b></html>", 12);
        totalPanel.add(paymentLabel);
        paymentPriceLabel = new TextLabel("₱0.00", 12);
        totalPanel.add(paymentPriceLabel);
        TextLabel changeLabel = new TextLabel("<html><b>Change:</b></html>", 12);
        totalPanel.add(changeLabel);
        changePriceLabel = new TextLabel("₱0.00", 12);
        totalPanel.add(changePriceLabel);
        bottomPanel.add(totalPanel, BorderLayout.NORTH);
        buttonPanel = new JPanel(new GridLayout(0, 2));
        CustomButton done = new CustomButton("", Constants.DONE_ICON, e->getPayment());
        buttonPanel.add(done);
        CustomButton cancel = new CustomButton("", Constants.CANCEL_ICON, e->clearOrder());
        cancel.setBackground(Color.RED);
        cancel.addMouseListener(new Hover(cancel));
        buttonPanel.add(cancel);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        cartPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(cartPanel, BorderLayout.EAST);
    }

    private void addProductToCart(JPanel panel, int productID, String productName, double productPrice, int duration, String productType){
        int y = panel.getComponentCount();
        try {
            String input = JOptionPane.showInputDialog(null, "Input quantity:", Constants.APP_TITLE, JOptionPane.PLAIN_MESSAGE);
            if(input != null){
                int quantity = Integer.valueOf(input);
                JPanel productPanel = new JPanel();
                TextLabel name = new TextLabel(quantity + "x - " + productID + "# - " + productName + ": ₱" + quantity*productPrice, 12);
                productPanel.add(name);
                ProductInCart product = new ProductInCart(productID, productName, productType,quantity, quantity*productPrice, duration);
                productInCarts.add(product);
                CustomButton remove = new CustomButton("", Constants.miniScaleImage(Constants.CANCEL_ICON), e->removeProduct(orderPanel, productPanel, product));
                remove.setBackground(Color.RED);
                remove.addMouseListener(new Hover(remove));
                productPanel.add(remove);
                CommonComponent.addComponent(orderPanel, productPanel, 0, y, 1, 1);
                updatePricing();
                revalidate();
                repaint();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Invalid input.", Constants.APP_TITLE, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeProduct(JPanel mainPanel, JPanel panel, ProductInCart productInCart){
        mainPanel.remove(panel);
        productInCarts.remove(productInCart);
        updatePricing();
        revalidate();
        repaint();
    }

    private void updatePricing(){
        double sum = 0;
        for(ProductInCart product : productInCarts){
            sum+=product.getPrice();
        }
        totalPriceLabel.setText(String.format("₱%,.2f" , sum));
    }

    private void getPayment(){
        double getTotalPrice = Double.valueOf(totalPriceLabel.getText().substring(1).replace(",", ""));
        try {
            String input = JOptionPane.showInputDialog(null, "Input customer's payment:", Constants.APP_TITLE, JOptionPane.PLAIN_MESSAGE);
            if(input != null){
                double paymentAmount = Double.valueOf(input);
                if(paymentAmount >= getTotalPrice) {
                    paymentPriceLabel.setText(String.format("₱%,.2f", paymentAmount));
                    changePriceLabel.setText(String.format("₱%,.2f", paymentAmount - getTotalPrice));
                    createReceipt(paymentAmount, getTotalPrice);
                } else JOptionPane.showMessageDialog(null, "Please enter a higher amount than the total.", Constants.APP_TITLE, JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid payment amount.", Constants.APP_TITLE, JOptionPane.ERROR_MESSAGE);
        }
        revalidate();
        repaint();
    }

    private void createReceipt(double amount, double payment){
        System.out.println(getReceiptNumber() + 1);
        System.out.println(String.format("%010d",getReceiptNumber() + 1));
        String receiptCode = String.valueOf(String.format("%010d",(getReceiptNumber() + 1))) + combineProductCodes().replaceAll("\\s", "");
        try (Connection conn = MySQL.getConnection()){
            PreparedStatement statement = conn.prepareStatement("INSERT INTO receipts VALUES (null, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?)");
            statement.setString(1, receiptCode);
            statement.setString(2, user.getUsername());
            statement.setDouble(3, payment);
            statement.setDouble(4, amount);
            statement.setDouble(5, (amount - payment));
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 0){
                generateItems();
                generateProductCodes();
                Messages.receiptGenerated(receiptCode);
                clearOrder();
                salesReportPanel.retrieveReceipts(salesReportPanel.getSortDays().getSelectedIndex());
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Messages.databaseConnectionFailed();
        }
    }

    private int getReceiptNumber(){
        int num = 0;
        try (Connection conn = MySQL.getConnection()){
            PreparedStatement statement = conn.prepareStatement("SELECT COUNT(*) as receipt_count FROM receipts");
            ResultSet result = statement.executeQuery();
            if(result.next()){
                num = result.getInt("receipt_count");
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Messages.databaseConnectionFailed(); 
        }
        return num;
    }

    private String combineProductCodes() {
        String str = "";
        Map<String, Integer> typeCountMap = new HashMap<>();
        for (ProductInCart product : productInCarts) {
            String productType = product.getCode();
            int count = typeCountMap.getOrDefault(productType, 1);
            int duration = product.getDuration();
            for(int i = 0; i <product.getQuantity(); i++){
                str += String.format("%s%d-%d ",productType, count, duration);
                count++;
                typeCountMap.put(productType, count);
            }
        }
        return str;
    }

    private String[] getSeperateProductCodes(){
        return combineProductCodes().split(" ");
    }

    private void generateProductCodes(){
        String[] productCodes = getSeperateProductCodes();
        int quantity = getTotalQuantity();
        try (Connection conn = MySQL.getConnection()){
            PreparedStatement statement = conn.prepareStatement("INSERT INTO receipt_codes VALUES (null, ?, ?, ?)");
            for(int i = 0; i < quantity; i++){
                statement.setInt(1, getReceiptNumber());
                statement.setString(2, productCodes[i]);
                statement.setBoolean(3, false);
                statement.addBatch();
            }
            statement.executeBatch();
            conn.close();
        } catch(SQLException e){
            e.printStackTrace();
            Messages.databaseConnectionFailed();
        }
    }

    private void generateItems(){
        try (Connection conn = MySQL.getConnection()){
            PreparedStatement statement = conn.prepareStatement("INSERT INTO items VALUES (null, ?, ?, ?)");
            for(ProductInCart product : productInCarts){
                if(!product.getCode().contains("N")){
                    statement.setInt(1, getReceiptNumber());
                    statement.setInt(2, product.getId());
                    statement.setInt(3, product.getQuantity());
                    statement.addBatch();
                }
            }
            statement.executeBatch();
            conn.close();
        } catch(SQLException e){
            e.printStackTrace();
            Messages.databaseConnectionFailed();
        }
    }

    private int getTotalQuantity(){
        int num = 0;
        for(ProductInCart product : productInCarts){
            num+=product.getQuantity();
        }
        return num;
    }

    private void clearOrder(){
        orderPanel.removeAll();
        totalPriceLabel.setText("₱0.00");
        paymentPriceLabel.setText("₱0.00");
        changePriceLabel.setText("₱0.00");
        productInCarts.removeAll(productInCarts);
    }

    private void initializeProductTypePanel(){
        productTypePanel = new JPanel();
        productTypePanel.setBackground(Color.decode("#08145c"));
        allProductButton = new CustomButton("All Products", null, e -> showProducts(0));
        CommonComponent.addComponent(productTypePanel, allProductButton, 0, 0, 1, 1);
        membershipButton = new CustomButton("Membership", null, e -> showProducts(1));
        CommonComponent.addComponent(productTypePanel, membershipButton, 1, 0, 1, 1);
        trainerButton = new CustomButton("Trainer", null, e -> showProducts(2));
        CommonComponent.addComponent(productTypePanel, trainerButton, 2, 0, 1, 1);
        equipmentButton = new CustomButton("Equipment", null, e -> showProducts(3));
        CommonComponent.addComponent(productTypePanel, equipmentButton, 3, 0, 1, 1);
        add(productTypePanel, BorderLayout.SOUTH);
    }
}
