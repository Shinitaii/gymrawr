package main.App.Main.SubPanels.AddUpdateList.PanelNames.Trainer;

import main.App.Main.SubPanels.AddUpdateList.ListPanel;
import main.Database.MySQL;
import main.Miscellanous.CommonComponent;
import main.Miscellanous.Messages;
import main.Objects.Training;
import main.Properties.Focus;
import main.Properties.Custom.*;

import java.awt.*;
import java.sql.*;

import javax.swing.*;

public class AddTrainerPanel extends JPanel {

    private JTextField firstNameField, middleNameField, lastNameField;
    private JFormattedTextField dateField, contactNumberField;
    private JRadioButton male, female;
    private JComboBox<String> selection;
    private CustomButton clear, add;
    private ButtonGroup gender;

    private int[] selectionID;
    
    public AddTrainerPanel(ListPanel listPanel){
        setLayout(new GridBagLayout());
        //firstname txtfield
        firstNameField = CommonComponent.configureTextField(this, "First name", 0, 0, 2, 1);
        //middlename
        middleNameField = CommonComponent.configureTextField(this, "Middle name", 0, 1, 2, 1);
        //lastname
        lastNameField = CommonComponent.configureTextField(this, "Last name", 0, 2, 2, 1); 

        TextLabel birthLabel = new TextLabel("Birth Date: (YYYY/MM/DD)", 12);
        CommonComponent.addComponent(this, birthLabel, 0, 3, 1, 1); 
        dateField = CommonComponent.configureDateField(dateField);
        CommonComponent.addComponent(this, dateField, 1, 3, 1, 1); 

        TextLabel contactLabel = new TextLabel("Contact Number: ", 12);
        CommonComponent.addComponent(this, contactLabel, 2, 0, 1, 1); 
        
        contactNumberField = CommonComponent.configureContactNumberField(contactNumberField);
        CommonComponent.addComponent(this, contactNumberField, 3, 0, 2, 1); 

        TextLabel genderLabel = new TextLabel("Gender:", 12);
        CommonComponent.addComponent(this, genderLabel, 2, 1, 1, 1); 
        
        gender = new ButtonGroup();
        male = CommonComponent.createRadioButton("Male", "male", gender);
        CommonComponent.addComponent(this, male, 3, 1, 1, 1); 
        female = CommonComponent.createRadioButton("Female", "female", gender);
        CommonComponent.addComponent(this, female, 4, 1, 1, 1); 

        selectionID = new int[Training.getTrainingList().size()];
        selection = CommonComponent.configureTrainingComboBox(selection, selectionID);
        CommonComponent.addComponent(this, selection, 2, 2, 1, 1);
        add = new CustomButton("Add", null, e -> addTrainer(listPanel.getListTrainerPanel()));
        CommonComponent.addComponent(this, add, 3, 3, 1, 1); 
        clear = new CustomButton("Clear", null, e -> clearForm());
        CommonComponent.addComponent(this, clear, 4,3, 1, 1);
    }

    private void addTrainer(ListTrainerPanel listTrainerPanel){
        try(Connection conn = MySQL.getConnection()){
            PreparedStatement statement = conn.prepareStatement("INSERT INTO trainers VALUES (null, ?, ?, ?, ?, ?, ?, ?)");
            statement.setInt(1, selectionID[selection.getSelectedIndex()]);
            statement.setString(2, firstNameField.getText());
            statement.setString(3, middleNameField.getText());
            statement.setString(4, lastNameField.getText());
            statement.setDate(5, Date.valueOf(dateField.getText()));
            statement.setLong(6, Long.valueOf(contactNumberField.getText()));
            statement.setString(7, gender.getSelection().getActionCommand());
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 0) {
                Messages.trainerAdded();
                listTrainerPanel.updateTableData();
            }
            else Messages.trainerAddFailed();
            conn.close();
        } catch (SQLException e){
            Messages.databaseConnectionFailed();
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
