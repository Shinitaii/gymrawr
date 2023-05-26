package main.App.Main.SubPanels.AddUpdateList.PanelNames.Member;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import main.Database.MySQL;
import main.Miscellanous.*;
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
        CommonComponent.addComponent(searchMemberPanel, fullNameLabel, 0, 0, 1, 1);
        fullNameField = CommonComponent.configureTextField(searchMemberPanel, "Full name", 1, 0, 1, 1);
        withNoMiddleName = new JCheckBox("With no middle name?");
        withNoMiddleName.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent e) {
                if(withNoMiddleName.isSelected()) fullNameQuery = "concat(member_firstname, ' ', member_lastname)";
                else fullNameQuery = "concat(member_firstname, ' ', member_middlename, ' ', member_lastname)";
            } 
        });
        CommonComponent.addComponent(searchMemberPanel, withNoMiddleName, 0, 1, 1, 1);
        check = new CustomButton("Check member", null, e->checkMember());
        CommonComponent.addComponent(searchMemberPanel, check, 0, 3, 1, 1);
        update = new CustomButton("Update");

        editMemberPanel = new JPanel(new GridBagLayout());
        add(editMemberPanel);
        editMemberPanel.setVisible(false);

        firstNameField = CommonComponent.configureTextField(editMemberPanel, "First name", 0, 0, 2, 1);;
        middleNameField = CommonComponent.configureTextField(editMemberPanel, "Middle name", 0, 1, 2, 1);
        lastNameField = CommonComponent.configureTextField(editMemberPanel, "Last name", 0, 2, 2, 1); 

        TextLabel birthLabel = new TextLabel("Birth Date: (YYYY/MM/DD)", 12);
        CommonComponent.addComponent(editMemberPanel, birthLabel, 0, 3, 1, 1); 
        
        dateField = CommonComponent.configureDateField(dateField);
        CommonComponent.addComponent(editMemberPanel, dateField, 1, 3, 1, 1); 

        TextLabel contactLabel = new TextLabel("Contact Number: ", 12);
        CommonComponent.addComponent(editMemberPanel, contactLabel, 2, 0, 1, 1); 
        
        contactNumberField = CommonComponent.configureContactNumberField(contactNumberField);
        CommonComponent.addComponent(editMemberPanel, contactNumberField, 3, 0, 2, 1); 

        selectionDuration = new int[Products.getProductList().size()];
        selection = CommonComponent.configureProductComboBox(selection, selectionDuration, 1);
        CommonComponent.addComponent(editMemberPanel, selection, 2, 2, 1, 1);
        update = new CustomButton("Update", null, e -> updateMember());
        CommonComponent.addComponent(editMemberPanel, update, 2, 3, 1, 1); 
        clear = new CustomButton("Clear", null, e -> clearForm());
        CommonComponent.addComponent(editMemberPanel, clear, 3, 3, 1, 1);
        cancel = new CustomButton("Cancel", null, e-> cancel());
        CommonComponent.addComponent(editMemberPanel, cancel, 4,3, 1, 1);

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

    private void clearForm(){
        Focus.setPlaceholder(firstNameField, "First name");
        Focus.setPlaceholder(middleNameField, "Middle name");
        Focus.setPlaceholder(lastNameField, "Last name");
        dateField.setText("");
        contactNumberField.setText("0");
    }
}