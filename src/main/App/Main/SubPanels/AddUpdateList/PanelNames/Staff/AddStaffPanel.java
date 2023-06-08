package main.App.Main.SubPanels.AddUpdateList.PanelNames.Staff;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;

import javax.swing.*;
import main.Database.MySQL;
import main.Miscellanous.CommonComponent;
import main.Miscellanous.Messages;
import main.Properties.Focus;
import main.Properties.Custom.CustomButton;
import main.Properties.Custom.TextLabel;

public class AddStaffPanel extends JPanel{
    private JPanel personalFormPanel;
    private JTextField firstNameField, middleNameField, lastNameField, addressField, usernameField;
    private JPasswordField passwordField;
    private JFormattedTextField dateField, contactNumberField;
    private ButtonGroup gender;
    private JRadioButton male, female;
    private CustomButton clear, add;

    private JCheckBox showPassword;
    private JComboBox<String> educationLevelBox, userLevel;

    public AddStaffPanel(ListStaffPanel listStaffPanel){
        personalFormPanel = setPersonalFormPanel(listStaffPanel);
        add(personalFormPanel);
    }

    private JPanel setPersonalFormPanel(ListStaffPanel listStaffPanel){
        JPanel panel = new JPanel(new GridBagLayout());
        TextLabel firstNameLabel = new TextLabel("First name:", 12);
        CommonComponent.addComponent(panel, firstNameLabel, 0,0,1,1);
        firstNameField = CommonComponent.configureTextField(panel, "First name", 1, 0, 1, 1);
        TextLabel middleNameLabel = new TextLabel("Middle name:", 12);
        CommonComponent.addComponent(panel, middleNameLabel, 0,1,1,1);
        middleNameField = CommonComponent.configureTextField(panel, "Middle name", 1, 1, 1, 1);
        TextLabel lastNameLabel = new TextLabel("Last name:", 12);
        CommonComponent.addComponent(panel, lastNameLabel, 0,2,1,1);
        lastNameField = CommonComponent.configureTextField(panel, "Last name", 1, 2, 1, 1); 
        TextLabel addressLabel = new TextLabel("Address: ", 12);
        CommonComponent.addComponent(panel, addressLabel, 0, 3, 1, 1);
        addressField = CommonComponent.configureTextField(panel, "Address", 1, 3, 1, 1);
        TextLabel usernameLabel = new TextLabel("Username:", 12);
        CommonComponent.addComponent(panel, usernameLabel, 0, 5, 1, 1);
        usernameField = CommonComponent.configureTextField(panel, "Username", 1, 5,1,1);
        TextLabel passwordLabel = new TextLabel("Password", 12);
        CommonComponent.addComponent(panel, passwordLabel, 0, 6, 1, 1);
        passwordField = new JPasswordField(25);
        Focus.setPlaceholder(passwordField, "Password");
        passwordField.addFocusListener(new FocusListener(){
            public void focusGained(FocusEvent e){
                String password = String.valueOf(passwordField.getPassword());
                if(!showPassword.isSelected())passwordField.setEchoChar('●');
                if(password.equals("Password")) passwordField.setText("");
                passwordField.setForeground(Color.BLACK);
            }
            public void focusLost(FocusEvent e){
                String password = String.valueOf(passwordField.getPassword());
                if(password.isEmpty()) Focus.setPlaceholder(passwordField, "Password");
            }
        });
        CommonComponent.addComponent(panel, passwordField, 1, 6, 1, 1);

        TextLabel birthLabel = new TextLabel("Birth Date: (YYYY/MM/DD)", 12);
        CommonComponent.addComponent(panel, birthLabel, 3, 0, 1, 1);         
        dateField = CommonComponent.configureDateField(dateField);
        CommonComponent.addComponent(panel, dateField, 4, 0, 2, 1); 
        TextLabel contactLabel = new TextLabel("Contact Number: ", 12);
        CommonComponent.addComponent(panel, contactLabel, 3, 2, 1, 1); 
        contactNumberField = CommonComponent.configureContactNumberField(contactNumberField);
        CommonComponent.addComponent(panel, contactNumberField, 4, 2, 2, 1); 
        TextLabel genderLabel = new TextLabel("Gender:", 12);
        CommonComponent.addComponent(panel, genderLabel, 3, 1, 1, 1); 
        gender = new ButtonGroup();
        male = CommonComponent.createRadioButton("Male", "male", gender);
        CommonComponent.addComponent(panel, male, 4, 1, 1, 1); 
        female = CommonComponent.createRadioButton("Female", "female", gender);
        CommonComponent.addComponent(panel, female, 5, 1, 1, 1);
        TextLabel educationLevelLabel = new TextLabel("Education Level:", 12);
        CommonComponent.addComponent(panel, educationLevelLabel, 3, 3, 1,1); 
        educationLevelBox = new JComboBox<>(new String[] {"Elementary", "High School", "Senior High", "Vocational", "University"});
        CommonComponent.addComponent(panel, educationLevelBox, 4, 3, 2,1); 
        TextLabel userLevelLabel = new TextLabel("Role:", 12);
        CommonComponent.addComponent(panel, userLevelLabel, 3, 5, 1,1); 
        userLevel = new JComboBox<>(new String[] {"Admin", "Staff"});
        CommonComponent.addComponent(panel, userLevel, 4, 5, 2, 1);
        showPassword = new JCheckBox("Show password");
        showPassword.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (showPassword.isSelected()) passwordField.setEchoChar((char)0);
                else 
                    if(!String.valueOf(passwordField.getPassword()).equals("Password"))passwordField.setEchoChar('●');
            }
        });
        CommonComponent.addComponent(panel, showPassword, 3, 6, 1, 1); 
        add = new CustomButton("Add", null, e -> addStaff(listStaffPanel));
        CommonComponent.addComponent(panel, add, 4, 7, 1, 1); 
        clear = new CustomButton("Clear", null, e -> clearForm());
        CommonComponent.addComponent(panel, clear, 5,7, 1, 1);
        return panel;
    }

    private void addStaff(ListStaffPanel listStaffPanel){
        int userID = addUser();
        try(Connection conn = MySQL.getConnection()){
            PreparedStatement statement = conn.prepareStatement("INSERT INTO staffs VALUES (null, ?, ?, ?, ?, YEAR(CURDATE()) - YEAR(staff_birthdate) - (DATE_FORMAT(CURDATE(), '00-%m-%d') < DATE_FORMAT(staff_birthdate, '00-%m-%d')),?, ?, ?, ?, ?)");
            statement.setInt(1, userID);
            statement.setString(2, firstNameField.getText());
            statement.setString(3, middleNameField.getText());
            statement.setString(4, lastNameField.getText());
            statement.setDate(5, Date.valueOf(dateField.getText()));
            statement.setLong(6, Long.valueOf(contactNumberField.getText()));
            statement.setString(7, gender.getSelection().getActionCommand());
            statement.setString(8, addressField.getText());
            statement.setString(9, educationLevelBox.getSelectedItem().toString());
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 0){
                Messages.userAdded();
                listStaffPanel.updateTableData();
                clearForm();
            } else Messages.userAddFailed();
            conn.close();
        } catch (SQLException e){
            Messages.databaseConnectionFailed();
            e.printStackTrace();
        }
    }

    private int addUser(){
        try(Connection conn = MySQL.getConnection()){
            PreparedStatement statement = conn.prepareStatement("INSERT INTO users VALUES (null, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, usernameField.getText());
            statement.setString(2, String.valueOf(passwordField.getPassword()));
            statement.setInt(3, (userLevel.getSelectedIndex()+1));
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 0){
                ResultSet keys = statement.getGeneratedKeys();
                if(keys.next()) return keys.getInt(1);
            }
            conn.close();
        } catch (SQLException e){
            Messages.databaseConnectionFailed();
            e.printStackTrace();
        }
        return 0;
    }

    private void clearForm(){
        Focus.setPlaceholder(firstNameField, "First name");
        Focus.setPlaceholder(middleNameField, "Middle name");
        Focus.setPlaceholder(lastNameField, "Last name");
        Focus.setPlaceholder(addressField, "Address");
        Focus.setPlaceholder(usernameField, "Username");
        Focus.setPlaceholder(passwordField, "Password");
        dateField.setText("");
        contactNumberField.setText("0");
        gender.clearSelection();
        showPassword.setSelected(false);
    }


}
