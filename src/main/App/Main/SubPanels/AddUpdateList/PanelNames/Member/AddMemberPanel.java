package main.App.Main.SubPanels.AddUpdateList.PanelNames.Member;
import javax.swing.*;

import main.Database.MySQL;
import main.Miscellanous.CommonComponent;
import main.Miscellanous.Messages;
import main.Objects.Products;
import main.Properties.Focus;
import main.Properties.Custom.*;

import java.awt.*;
import java.sql.*;

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
        TextLabel firstNameLabel = new TextLabel("First name:", 12);
        CommonComponent.addComponent(this, firstNameLabel, 0,0,1,1);
        firstNameField = CommonComponent.configureTextField(this, "First name", 1, 0, 2, 1);
        //middlename
        TextLabel middleNameLabel = new TextLabel("Middle name:", 12);
        CommonComponent.addComponent(this, middleNameLabel, 0,1,1,1);
        middleNameField = CommonComponent.configureTextField(this, "Middle name", 1, 1, 2, 1);
        //lastname
        TextLabel lastNameLabel = new TextLabel("Last name:", 12);
        CommonComponent.addComponent(this, lastNameLabel, 0,2,1,1);
        lastNameField = CommonComponent.configureTextField(this, "Last name", 1, 2, 2, 1); 

        TextLabel birthLabel = new TextLabel("Birth Date: (YYYY/MM/DD)", 12);
        CommonComponent.addComponent(this, birthLabel, 0, 3, 1, 1); 
        dateField = CommonComponent.configureDateField(dateField);
        CommonComponent.addComponent(this, dateField, 1, 3, 1, 1); 

        TextLabel contactLabel = new TextLabel("Contact Number: ", 12);
        CommonComponent.addComponent(this, contactLabel, 3, 0, 1, 1); 
        
        contactNumberField = CommonComponent.configureContactNumberField(contactNumberField);
        CommonComponent.addComponent(this, contactNumberField, 4, 0, 2, 1); 

        TextLabel genderLabel = new TextLabel("Gender:", 12);
        CommonComponent.addComponent(this, genderLabel, 3, 1, 1, 1); 
        
        gender = new ButtonGroup();
        male = CommonComponent.createRadioButton("Male", "male", gender);
        CommonComponent.addComponent(this, male, 4, 1, 1, 1); 
        female = CommonComponent.createRadioButton("Female", "female", gender);
        CommonComponent.addComponent(this, female, 5, 1, 1, 1); 

        TextLabel membershipLabel = new TextLabel("Select membership", 12);
        
        selectionDuration = new int[Products.getProductList().size()];
        selection = CommonComponent.configureProductComboBox(selection, selectionDuration, 1);
        CommonComponent.addComponent(this, selection, 4, 2, 1, 1);
        add = new CustomButton("Add", null, e -> addMember(listMemberPanel));
        CommonComponent.addComponent(this, add, 3, 3, 1, 1); 
        clear = new CustomButton("Clear", null, e -> clearForm());
        CommonComponent.addComponent(this, clear, 4,3, 1, 1);
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
}
