package main.Properties.AddUpdateList.PanelNames.Member;
import javax.swing.*;
import javax.swing.text.*;

import main.Database.MySQL;
import main.Miscellanous.Messages;
import main.Objects.Products;
import main.Properties.Focus;
import main.Properties.Custom.*;

import java.awt.*;
import java.sql.*;
import java.text.*;

public class AddMemberPanel extends JPanel{
    private JTextField firstNameField, middleNameField, lastNameField;
    private JFormattedTextField dateField, contactNumberField;
    private JRadioButton male, female;
    private JComboBox<String> selection;
    private CustomButton clear, add;
    private ButtonGroup gender;

    private int[] selectionDuration;

    public AddMemberPanel(ListMemberPanel listMemberPanel){
        setLayout(new GridBagLayout());
        //firstname txtfield
        firstNameField = new JTextField(30);
        Focus.setPlaceholder(firstNameField, "First name");
        firstNameField.addFocusListener(new Focus(firstNameField, "First name"));
        addComponent(firstNameField, 0, 0, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, new Insets(5, 5, 5, 5));
        //middlename
        middleNameField = new JTextField(30);
        Focus.setPlaceholder(middleNameField, "Middle name");
        middleNameField.addFocusListener(new Focus(middleNameField, "Middle name"));
        addComponent(middleNameField, 0, 1, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, new Insets(5, 5, 5, 5));
        //lastname
        lastNameField = new JTextField(30);
        Focus.setPlaceholder(lastNameField, "Last name");
        lastNameField.addFocusListener(new Focus(lastNameField, "Last name"));   
        addComponent(lastNameField, 0, 2, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, new Insets(5, 5, 5, 5));

        TextLabel birthLabel = new TextLabel("Birth Date: (YYYY/MM/DD)", 12);
        addComponent(birthLabel, 0, 3, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, new Insets(5, 5, 5, 5)); 
        MaskFormatter formatter;
        try{
            formatter = new MaskFormatter("####-##-##");
            formatter.setPlaceholderCharacter('_');
        } catch (ParseException e){
            e.printStackTrace();
            return;
        }
        dateField = new JFormattedTextField(formatter);
        dateField.setColumns(10);
        addComponent(dateField, 1, 3, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, new Insets(5, 5, 5, 5)); 

        TextLabel contactLabel = new TextLabel("Contact Number: ", 12);
        addComponent(contactLabel, 2, 0, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, new Insets(5, 5, 5, 5)); 
        
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        numberFormat.setGroupingUsed(false);
        NumberFormatter numberFormatter = new NumberFormatter(numberFormat);
        numberFormatter.setValueClass(Long.class);
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setMaximum(9999999999L); // 10 digits
        contactNumberField = new JFormattedTextField(numberFormatter);
        contactNumberField.setText("0");
        contactNumberField.setColumns(10);
        addComponent(contactNumberField, 3, 0, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, new Insets(5, 5, 5, 5)); 

        TextLabel genderLabel = new TextLabel("Gender:", 12);
        addComponent(genderLabel, 2, 1, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, new Insets(5, 5, 5, 5)); 
        
        gender = new ButtonGroup();
        male = createRadioButton("Male", "male");
        addComponent(male, 3, 1, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, new Insets(5, 5, 5, 5)); 
        female = createRadioButton("Female", "female");
        addComponent(female, 4, 1, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, new Insets(5, 5, 5, 5)); 

        selection = new JComboBox<String>();
        selectionDuration = new int[Products.getProductList().size()];
        int index = 0;
        for(Products products : Products.getProductList()){
            if(products.getProductType() == 1) { //if product type is 1 (member)
                selection.addItem(products.getProductName());
                selectionDuration[index] = products.getProductDayDuration();
                index++;
             }
        }
        addComponent(selection, 2, 2, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, new Insets(5, 5, 5, 5));
        add = new CustomButton("Add", null, e -> addMember(listMemberPanel));
        addComponent(add, 3, 3, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, new Insets(5, 5, 5, 5)); 
        clear = new CustomButton("Clear", null, e -> clearForm());
        addComponent(clear, 4,3, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, new Insets(5, 5, 5, 5));
    }

    private void addMember(ListMemberPanel listMemberPanel){
        try(Connection conn = MySQL.getConnection()){
            PreparedStatement statement = conn.prepareStatement("INSERT INTO members VALUES (null, ?, ?, ?, ?, ?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL ? DAY))");
            statement.setString(1, firstNameField.getText());
            statement.setString(2, middleNameField.getText());
            statement.setString(3, lastNameField.getText());
            statement.setDate(4, Date.valueOf(dateField.getText()));
            statement.setLong(5, Long.valueOf(contactNumberField.getText()));
            statement.setString(6, gender.getSelection().getActionCommand());
            statement.setInt(7, selectionDuration[selection.getSelectedIndex()]);

            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 0) {
                Messages.memberAdded();
                listMemberPanel.updateTableData();
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

    private void clearForm(){
        Focus.setPlaceholder(firstNameField, "First name");
        Focus.setPlaceholder(middleNameField, "Middle name");
        Focus.setPlaceholder(lastNameField, "Last name");
        dateField.setText("");
        contactNumberField.setText("0");
        gender.clearSelection();
    }

    private void addComponent(Component component, int gridx, int gridy, int gridwidth, int gridheight, int fill, int anchor, Insets insets) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        gbc.fill = fill;
        gbc.anchor = anchor;
        gbc.insets = insets;
        add(component, gbc);
    }

    private JRadioButton createRadioButton(String label, String actionCommand) {
        JRadioButton radioButton = new JRadioButton(label);
        radioButton.setForeground(Color.decode("#08145c"));
        radioButton.setActionCommand(actionCommand);
        gender.add(radioButton);
        return radioButton;
    }
}
