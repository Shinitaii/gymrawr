package main.App.Main.SubPanels.AddUpdateList.PanelNames.Member;
import javax.swing.*;

import main.Database.MySQL;
import main.Miscellanous.CommonComponent;
import main.Miscellanous.Constants;
import main.Miscellanous.Messages;
import main.Properties.Focus;
import main.Properties.Custom.*;

import java.awt.*;
import java.sql.*;

public class AddMemberPanel extends JPanel{
    private JPanel searchReceiptPanel, mainPanel;
    private JTextField receiptNumberField, firstNameField, middleNameField, lastNameField;
    private JFormattedTextField dateField, contactNumberField;
    private JRadioButton male, female;
    private CustomButton clear, add, search, useProductCode;
    private ButtonGroup gender;

    private JComboBox<String> productCodeSelection;

    private String code;

    public AddMemberPanel(ListMemberPanel listMemberPanel){
        setLayout(new BorderLayout());
        searchReceiptPanel = new JPanel(new GridBagLayout());
        add(searchReceiptPanel, BorderLayout.NORTH);
        TextLabel receiptNumberLabel = new TextLabel("Receipt Number:", 12);
        CommonComponent.addComponent(searchReceiptPanel, receiptNumberLabel, 0, 0, 1, 1);
        receiptNumberField = CommonComponent.configureReceiptNumberField();
        CommonComponent.addComponent(searchReceiptPanel, receiptNumberField, 1, 0, 1, 1);
        search = new CustomButton("Search receipt number", null, e->searchReceipt());
        CommonComponent.addComponent(searchReceiptPanel, search, 2, 0, 1, 1);
        TextLabel productCodeLabel = new TextLabel("Select product code once receipt number is found:", 12);
        CommonComponent.addComponent(searchReceiptPanel, productCodeLabel, 0, 2, 1,1);
        productCodeSelection = new JComboBox<>();
        productCodeSelection.setEnabled(false);
        CommonComponent.addComponent(searchReceiptPanel, productCodeSelection, 1, 2, 1, 1);
        useProductCode = new CustomButton("Use product code", null, e -> searchMemberPanel(String.valueOf(productCodeSelection.getSelectedItem())));
        CommonComponent.addComponent(searchReceiptPanel, useProductCode, 2, 2, WIDTH, HEIGHT);

        mainPanel = new JPanel(new GridBagLayout());
        add(mainPanel, BorderLayout.CENTER);
        //firstname txtfield
        TextLabel firstNameLabel = new TextLabel("First name:", 12);
        CommonComponent.addComponent(mainPanel, firstNameLabel, 0,0,1,1);
        firstNameField = CommonComponent.configureTextField(mainPanel, "First name", 1, 0, 1, 1);
        //middlename
        TextLabel middleNameLabel = new TextLabel("Middle name:", 12);
        CommonComponent.addComponent(mainPanel, middleNameLabel, 0,1,1,1);
        middleNameField = CommonComponent.configureTextField(mainPanel, "Middle name", 1, 1, 1, 1);
        //lastname
        TextLabel lastNameLabel = new TextLabel("Last name:", 12);
        CommonComponent.addComponent(mainPanel, lastNameLabel, 0,2,1,1);
        lastNameField = CommonComponent.configureTextField(mainPanel, "Last name", 1, 2, 1, 1); 

        TextLabel birthLabel = new TextLabel("Birth Date: (YYYY/MM/DD)", 12);
        CommonComponent.addComponent(mainPanel, birthLabel, 0, 3, 1, 1); 
        dateField = CommonComponent.configureDateField(dateField);
        CommonComponent.addComponent(mainPanel, dateField, 1, 3, 1, 1); 

        TextLabel contactLabel = new TextLabel("Contact Number: ", 12);
        CommonComponent.addComponent(mainPanel, contactLabel, 3, 0, 1, 1); 
        
        contactNumberField = CommonComponent.configureContactNumberField(contactNumberField);
        CommonComponent.addComponent(mainPanel, contactNumberField, 4, 0, 2, 1); 

        TextLabel genderLabel = new TextLabel("Gender:", 12);
        CommonComponent.addComponent(mainPanel, genderLabel, 3, 1, 1, 1); 
        
        gender = new ButtonGroup();
        male = CommonComponent.createRadioButton("Male", "male", gender);
        CommonComponent.addComponent(mainPanel, male, 4, 1, 1, 1); 
        female = CommonComponent.createRadioButton("Female", "female", gender);
        CommonComponent.addComponent(mainPanel, female, 5, 1, 1, 1); 
        add = new CustomButton("Add", null, e -> addMember(listMemberPanel));
        CommonComponent.addComponent(mainPanel, add, 3, 3, 1, 1); 
        clear = new CustomButton("Clear", null, e -> clearForm());
        CommonComponent.addComponent(mainPanel, clear, 4,3, 1, 1);

        for(Component component : mainPanel.getComponents()){
            component.setEnabled(false);
        }
    }

    private void searchMemberPanel(String code){
        this.code = code;
        for(Component component : mainPanel.getComponents()){
            component.setEnabled(true);
        }
    }

    private void searchReceipt(){
        productCodeSelection.removeAllItems();
        try(Connection conn = MySQL.getConnection()){
            String receipt = receiptNumberField.getText().toString();
            String trimmedString = receipt.replaceFirst("^0+", "");
            PreparedStatement statement = conn.prepareStatement("SELECT * from receipt_codes where receipt_id = ? and is_used = false and left(product_code, 1) = 'M'");
            statement.setString(1, trimmedString);
            ResultSet result = statement.executeQuery();
            boolean found = false;
            while (result.next()) {
                found = true;
                productCodeSelection.setEnabled(true);
                productCodeSelection.addItem(result.getString("product_code"));
            } 
            if(!found) {
                JOptionPane.showMessageDialog(null, "The receipt cannot be found or all of the product codes in this receipt are already used up.", Constants.APP_TITLE, JOptionPane.ERROR_MESSAGE);
            }
            revalidate();
            repaint();
        } catch(SQLException e){
            e.printStackTrace();
            Messages.databaseConnectionFailed();
        }
    }

    private void addMember(ListMemberPanel listMemberPanel){
        int duration = Integer.valueOf(code.substring(3));
        try(Connection conn = MySQL.getConnection()){
            PreparedStatement statement = conn.prepareStatement("INSERT INTO members VALUES (null, ?, ?, ?, ?, ?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL ? DAY))");
            statement.setString(1, firstNameField.getText());
            statement.setString(2, middleNameField.getText());
            statement.setString(3, lastNameField.getText());
            statement.setDate(4, Date.valueOf(dateField.getText()));
            statement.setLong(5, Long.valueOf(contactNumberField.getText()));
            statement.setString(6, gender.getSelection().getActionCommand());
            statement.setInt(7, duration);

            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 0) {
                Messages.memberAdded();
                listMemberPanel.updateTableData();
                disableProduct(code);
                clearForm();
                searchReceipt();
                for(Component component : mainPanel.getComponents()){
                    component.setEnabled(false);
                }
                revalidate();
                repaint();
            }
            else Messages.memberAddFailed();
            conn.close();
        } catch (SQLException e){
            Messages.databaseConnectionFailed();
            e.printStackTrace();
        } catch (IllegalArgumentException e){
            Messages.invalidDateFormat();
            e.printStackTrace();
        } catch (NullPointerException e){
            Messages.invalidGender();
            e.printStackTrace();
        }
    }

    private void disableProduct(String code){
        try(Connection conn = MySQL.getConnection()){
            String receipt = receiptNumberField.getText().toString();
            String trimmedString = receipt.replaceFirst("^0+", "");
            PreparedStatement statement = conn.prepareStatement("UPDATE receipt_codes set is_used = true where receipt_id = ? and product_code = ?");
            statement.setInt(1, Integer.valueOf(trimmedString));
            statement.setString(2, code);
            statement.executeUpdate();
            conn.close();
        } catch(SQLException e){
            e.printStackTrace();
            Messages.databaseConnectionFailed();
        }
    }

    private void clearForm(){
        Focus.setPlaceholder(firstNameField, "First name");
        Focus.setPlaceholder(middleNameField, "Middle name");
        Focus.setPlaceholder(lastNameField, "Last name");
        dateField.setText("");
        contactNumberField.setText("0");
        gender.clearSelection();
    }
}
