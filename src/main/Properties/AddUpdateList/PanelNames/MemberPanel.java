package main.Properties.AddUpdateList.PanelNames;
import java.awt.*;
import java.sql.*;
import java.text.*;

import javax.swing.*;
import javax.swing.text.*;

import main.Database.MySQL;
import main.Miscellanous.Messages;
import main.Properties.Focus;
import main.Properties.Custom.*;

public class MemberPanel extends JPanel{
    private JTextField firstNameField, middleNameField, lastNameField;
    private JFormattedTextField dateField, contactNumberField;
    private JRadioButton male, female;
    private CustomButton clear, add, update, selectMember;
    private ButtonGroup gender;
    
    public MemberPanel(String command, String panelName){
        if(command.equals("Add")) {
            add(panelName); 
            addButtons(panelName);
        } else if(command.equals("Update")) {
            update(panelName);
        } else list(panelName);   
    }

    private void add(String panelName){
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

    }

    private void addButtons(String panelName){
        add = new CustomButton("Add", null, e -> addMember());
        addComponent(add, 3, 3, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, new Insets(5, 5, 5, 5)); 
        clear = new CustomButton("Clear", null, e -> clearForm());
        addComponent(clear, 4,3, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, new Insets(5, 5, 5, 5));
    }
    
    private void addMember(){
        try(Connection conn = MySQL.getConnection()){
            PreparedStatement statement = conn.prepareStatement("INSERT INTO members VALUES (null, ?, ?, ?, ?, ?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 30 DAY))");
            statement.setString(1, firstNameField.getText());
            statement.setString(2, middleNameField.getText());
            statement.setString(3, lastNameField.getText());
            statement.setDate(4, Date.valueOf(dateField.getText()));
            statement.setLong(5, Long.valueOf(contactNumberField.getText()));
            statement.setString(6, gender.getSelection().getActionCommand());

            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 0) Messages.memberAdded();
            else Messages.memberAddFailed();
            clearForm();
            conn.close();
        } catch (SQLException e){
            Messages.databaseConnectionFailed();
        } catch (IllegalArgumentException e){
            Messages.invalidDateFormat();
        } catch (NullPointerException e){
            Messages.invalidGender();
        }
    }

    private void update(String panelName){
        add(panelName);
        selectMember = new CustomButton("Select member", null, e -> selectMember());
        update = new CustomButton("Update", null, e -> updateMember());
        addComponent(update, 3, 3, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, new Insets(5, 5, 5, 5));
        clearButton();
    }

    private void selectMember(){
        JOption
    }

    private void updateMember(){
        try(Connection conn = MySQL.getConnection()){
            PreparedStatement statement = conn.prepareStatement("select *")
        } catch (SQLException e){
            Messages.databaseConnectionFailed();
        }
    }

    private void list(String panelName){

    }

    private void clearForm(){
        Focus.setPlaceholder(firstNameField, "First name");
        Focus.setPlaceholder(middleNameField, "Middle name");
        Focus.setPlaceholder(lastNameField, "Last name");
        dateField.setText("");
        contactNumberField.setText("0");
        gender.clearSelection();
    }

    private void clearButton(){
        clear = new CustomButton("Clear", null, e -> clearForm());
        addComponent(clear, 4,3, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER, new Insets(5, 5, 5, 5));
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
