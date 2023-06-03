package main.App.Main.SubPanels;
import javax.swing.*;
import main.Miscellanous.CommonComponent;
import main.Miscellanous.Constants;
import main.Objects.Products;
import main.Objects.User;
import main.Properties.Hover;
import main.Properties.Custom.CustomButton;
import main.Properties.Custom.TextLabel;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class POSPanel extends JPanel {
    private User user;
    private ArrayList<String> nameLists;
    private ArrayList<Double> priceLists;
    private JPanel productPanel, cartPanel, productTypePanel, buttonPanel;
    private CustomButton allProductButton, membershipButton, trainerButton, equipmentButton;
    private TextLabel totalPriceLabel, paymentPriceLabel, changePriceLabel;

    private JPanel orderPanel;

    public POSPanel(User user){
        this.user = user;
        setLayout(new BorderLayout());
        nameLists = new ArrayList<>();
        priceLists = new ArrayList<>();
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

            if(type == 0 || product.getProductType() == type) {
                if(x == 2){
                    y++;
                    x = 0;
                }
                CustomButton productButton = new CustomButton("<html>"+id + "# - " +name + "<br>Price - ₱"+price+"</html>", null, e->addProductToCart(id, name, price));
                CommonComponent.addComponent(productPanel, productButton, x, y, 1, 1);
                x++;
            }
        }
        revalidate();
        repaint();
    }
    //cartpanels
    private void initializeCartPanel(){
        cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.decode("#08145c")));
        cartPanel.add(new JLabel("-----------------------------------------------------------------"), BorderLayout.NORTH);
        orderPanel = new JPanel(new GridLayout(0,1));
        JScrollPane scrollPanel = new JScrollPane(orderPanel);
        scrollPanel.setBorder(null);
        cartPanel.add(scrollPanel, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel totalPanel = new JPanel(new GridLayout(0,2));
        TextLabel totalLabel = new TextLabel("Total: ", 12);
        totalPanel.add(totalLabel);
        totalPriceLabel = new TextLabel("₱0.00", 12);
        totalPanel.add(totalPriceLabel);
        TextLabel paymentLabel = new TextLabel("Payment: ", 12);
        totalPanel.add(paymentLabel);
        paymentPriceLabel = new TextLabel("₱0.00", 12);
        totalPanel.add(paymentPriceLabel);
        TextLabel changeLabel = new TextLabel("Change: ", 12);
        totalPanel.add(changeLabel);
        changePriceLabel = new TextLabel("₱0.00", 12);
        totalPanel.add(changePriceLabel);
        bottomPanel.add(totalPanel, BorderLayout.NORTH);
        buttonPanel = new JPanel(new GridLayout(0, 2));
        CustomButton done = new CustomButton("", Constants.DONE_ICON, e->getPayment());
        buttonPanel.add(done);
        CustomButton cancel = new CustomButton("", Constants.CANCEL_ICON, e->clearOrder());
        cancel.setBackground(Color.GRAY);
        cancel.addMouseListener(new Hover(cancel));
        buttonPanel.add(cancel);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        cartPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(cartPanel, BorderLayout.EAST);
    }

    private void addProductToCart(int productID, String productName, double productPrice){
        JPanel productPanel = new JPanel();
        TextLabel name = new TextLabel(productID + "# - " + productName + ": ₱" + productPrice, 9);
        productPanel.add(name);
        CustomButton remove = new CustomButton("", Constants.miniScaleImage(Constants.CANCEL_ICON), e->removeProduct(orderPanel, productPanel, productName, productPrice));
        productPanel.add(remove);
        orderPanel.add(productPanel);
        nameLists.add(productName);
        priceLists.add(productPrice);
        updatePricing();
        revalidate();
        repaint();
    }

    private void removeProduct(JPanel mainPanel, JPanel panel,String name, double price){
        mainPanel.remove(panel);
        nameLists.remove(name);
        priceLists.remove(price);
        updatePricing();
        revalidate();
        repaint();
    }

    private void updatePricing(){
        double sum = 0;
        for(double price : priceLists){
            sum+=price;
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
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm:ss a");
        String formattedDateTime = currentDateTime.format(formatter);
        System.out.println("------------------------------------");
        System.out.println("GYM RAWR - " + formattedDateTime);
        System.out.println("Cashier: " + user.getUsername());
        System.out.println("------------------------------------");
        for(int i = 0; i < nameLists.size(); i++){
            System.out.println("1x - " + nameLists.get(i) + " \t ₱" + priceLists.get(i));
        }
        System.out.println("------------------------------------");
        System.out.println("Total: ₱" + amount);
        System.out.println("Payment: ₱" + payment);
        System.out.println("Change: ₱" + (amount - payment));
        System.out.println("------------------------------------");
    }

    private void clearOrder(){

    }

    private void initializeProductTypePanel(){
        productTypePanel = new JPanel();
        productTypePanel.setBackground(Color.decode("#08145c").brighter().brighter());
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
