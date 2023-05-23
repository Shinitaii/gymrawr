package main.Properties.AddUpdateList.PanelNames.Member;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.*;
import javax.swing.*;
import javax.swing.text.*;

import main.Database.MySQL;
import main.Miscellanous.Messages;
import main.Objects.Products;
import main.Properties.Focus;
import main.Properties.Custom.*;

public class UpdateMemberPanel extends JPanel{
    private JPanel searchMemberPanel, editMemberPanel;
    private TextLabel fullNameLabel;
    private JTextField fullNameField;
    private JCheckBox withNoMiddleName;
    private CustomButton check;

    private JTextField firstNameField, middleNameField, lastNameField;
    private JFormattedTextField dateField, contactNumberField;
    private JComboBox<String> selection;
    private CustomButton update, clear, cancel;

    private int[] selectionDuration;

    private String fullNameQuery = "concat(member_firstname, ' ', member_middlename, ' ', member_lastname)";

    public UpdateMemberPanel(){
        searchMemberPanel = new JPanel(new GridBagLayout());
        add(searchMemberPanel);
        
        fullNameLabel = new TextLabel("Enter full name:", 12);
        addComponent(searchMemberPanel, fullNameLabel, 0, 0, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, defaultInsets());
        fullNameField = new JTextField(30);
        addComponent(searchMemberPanel, fullNameField, 1, 0, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, defaultInsets());
        withNoMiddleName = new JCheckBox("With no middle name?");
        withNoMiddleName.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent e) {
                if(withNoMiddleName.isSelected()) fullNameQuery = "concat(member_firstname, ' ', member_lastname)";
                else fullNameQuery = "concat(member_firstname, ' ', member_middlename, ' ', member_lastname)";
            } 
        });
        addComponent(searchMemberPanel, withNoMiddleName, 0, 1, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, defaultInsets());
        check = new CustomButton("Check member", null, e->checkMember());
        addComponent(searchMemberPanel, check, 0, 3, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, defaultInsets());
        update = new CustomButton("Update");

        editMemberPanel = new JPanel(new GridBagLayout());
        add(editMemberPanel);
        editMemberPanel.setVisible(false);

        firstNameField = new JTextField(30);
        Focus.setPlaceholder(firstNameField, "First name");
        firstNameField.addFocusListener(new Focus(firstNameField, "First name"));
        addComponent(editMemberPanel, firstNameField, 0, 0, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, defaultInsets());

        middleNameField = new JTextField(30);
        Focus.setPlaceholder(middleNameField, "Middle name");
        middleNameField.addFocusListener(new Focus(middleNameField, "Middle name"));
        addComponent(editMemberPanel, middleNameField, 0, 1, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, defaultInsets());
        
        lastNameField = new JTextField(30);
        Focus.setPlaceholder(lastNameField, "Last name");
        lastNameField.addFocusListener(new Focus(lastNameField, "Last name"));   
        addComponent(editMemberPanel, lastNameField, 0, 2, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, defaultInsets());

        TextLabel birthLabel = new TextLabel("Birth Date: (YYYY/MM/DD)", 12);
        addComponent(editMemberPanel, birthLabel, 0, 3, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, defaultInsets()); 
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
        addComponent(editMemberPanel, dateField, 1, 3, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, defaultInsets()); 

        TextLabel contactLabel = new TextLabel("Contact Number: ", 12);
        addComponent(editMemberPanel, contactLabel, 2, 0, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, defaultInsets()); 
        
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        numberFormat.setGroupingUsed(false);
        NumberFormatter numberFormatter = new NumberFormatter(numberFormat);
        numberFormatter.setValueClass(Long.class);
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setMaximum(9999999999L); // 10 digits
        contactNumberField = new JFormattedTextField(numberFormatter);
        contactNumberField.setText("0");
        contactNumberField.setColumns(10);
        addComponent(editMemberPanel, contactNumberField, 3, 0, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, defaultInsets()); 

        selection = new JComboBox<String>();
        selection.addItem("No renewal");
        selectionDuration = new int[Products.getProductList().size()];
        selectionDuration[0] = 0;
        int index = 1;
        for(Products products : Products.getProductList()){
            if(products.getProductType() == 1) { //if product type is 1 (member)
                selection.addItem(products.getProductName());
                selectionDuration[index] = products.getProductDayDuration();
                index++;
             }
        }
        addComponent(editMemberPanel, selection, 2, 2, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, defaultInsets());
        update = new CustomButton("Update", null, e -> updateMember());
        addComponent(editMemberPanel, update, 2, 3, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, defaultInsets()); 
        clear = new CustomButton("Clear", null, e -> clearForm());
        addComponent(editMemberPanel, clear, 3,3, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, defaultInsets());
        cancel = new CustomButton("Cancel", null, e-> cancel());
        addComponent(editMemberPanel, cancel, 4,3, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, defaultInsets());

    }

    private void checkMember(){
        try (Connection conn = MySQL.getConnection()){
            PreparedStatement statement = conn.prepareStatement("select * from members where " + fullNameQuery + " = ?");
            statement.setString(1, fullNameField.getText());
            ResultSet result = statement.executeQuery();
            if(result.next()){
                Messages.memberFound();
                searchMemberPanel.setVisible(false);
                editMemberPanel.setVisible(true);
                firstNameField.setText(result.getString("member_firstname"));
                middleNameField.setText(result.getString("member_middlename"));
                lastNameField.setText(result.getString("member_lastname"));
                dateField.setText(result.getString(String.valueOf("member_birthdate")));
                contactNumberField.setText(String.valueOf(result.getLong("member_contactnumber")));
                repaint();
                revalidate();
            } else Messages.memberNotFound();
        } catch (SQLException e) {
            Messages.databaseConnectionFailed();
            e.printStackTrace();
        }
    }

    private void updateMember(){
        try(Connection conn = MySQL.getConnection()){
            PreparedStatement statement = conn.prepareStatement("update members set member_firstname = ?, member_middlename = ?, member_lastname = ?, member_birthdate = ?, member_contactnumber = ?, membership_enddate = DATE_ADD(membership_enddate, INTERVAL ? DAY) where " + fullNameQuery + " = ?");
            statement.setString(1, firstNameField.getText());
            statement.setString(2, middleNameField.getText());
            statement.setString(3, lastNameField.getText());
            statement.setDate(4, Date.valueOf(dateField.getText()));
            statement.setLong(5, Long.valueOf(contactNumberField.getText()));
            statement.setInt(6, selectionDuration[selection.getSelectedIndex()]);
            statement.setString(7, fullNameField.getText());
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 0){
                Messages.memberUpdated();
                clearForm();
                cancel();
            } else Messages.memberNotUpdated();
        } catch (SQLException e){
            Messages.databaseConnectionFailed();
            e.printStackTrace();
        }
    }

    private void cancel(){
        searchMemberPanel.setVisible(true);
        editMemberPanel.setVisible(false);
        revalidate();
        repaint();
    }

    private void addComponent(JPanel panel, Component component, int gridx, int gridy, int gridwidth, int gridheight, int fill, int anchor, Insets insets) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        gbc.fill = fill;
        gbc.anchor = anchor;
        gbc.insets = insets;
        panel.add(component, gbc);
    }

    private void clearForm(){
        Focus.setPlaceholder(firstNameField, "First name");
        Focus.setPlaceholder(middleNameField, "Middle name");
        Focus.setPlaceholder(lastNameField, "Last name");
        dateField.setText("");
        contactNumberField.setText("0");
    }

    private Insets defaultInsets(){
        return new Insets(5, 5,5,5);
    }
}
