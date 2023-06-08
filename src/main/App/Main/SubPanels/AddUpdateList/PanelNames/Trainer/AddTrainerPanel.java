package main.App.Main.SubPanels.AddUpdateList.PanelNames.Trainer;

import main.App.Main.SubPanels.AddUpdateList.ListPanel;
import main.Database.MySQL;
import main.Miscellanous.CommonComponent;
import main.Miscellanous.Messages;
import main.Objects.Training;
import main.Properties.Focus;
import main.Properties.Custom.*;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;
import java.util.ArrayList;

import javax.swing.*;

import com.mysql.cj.x.protobuf.MysqlxPrepare.Prepare;

public class AddTrainerPanel extends JPanel {

    private JTextField firstNameField, middleNameField, lastNameField;
    private JFormattedTextField dateField, contactNumberField;
    private JRadioButton male, female;
    private CustomButton clear, add;
    private ButtonGroup gender;
    private JCheckBox[] specialization;
    private ArrayList<Integer> checked;
    
    public AddTrainerPanel(ListPanel listPanel){
        setLayout(new GridBagLayout());
        //firstname txtfield
        TextLabel firstNameLabel = new TextLabel("First name:", 12);
        CommonComponent.addComponent(this, firstNameLabel, 0,0,1,1);
        firstNameField = CommonComponent.configureTextField(this, "First name", 1, 0, 4, 1);
        //middlename
        TextLabel middleNameLabel = new TextLabel("Middle name:", 12);
        CommonComponent.addComponent(this, middleNameLabel, 0,1,1,1);
        middleNameField = CommonComponent.configureTextField(this, "Middle name", 1, 1, 4, 1);
        //lastname
        TextLabel lastNameLabel = new TextLabel("Last name:", 12);
        CommonComponent.addComponent(this, lastNameLabel, 0,2,1,1);
        lastNameField = CommonComponent.configureTextField(this, "Last name", 1, 2, 4, 1); 

        TextLabel birthLabel = new TextLabel("Birth Date: (YYYY/MM/DD)", 12);
        CommonComponent.addComponent(this, birthLabel, 0, 3, 1, 1);         
        dateField = CommonComponent.configureDateField(dateField);
        CommonComponent.addComponent(this, dateField, 1, 3, 1, 1); 
        TextLabel contactLabel = new TextLabel("Contact Number: ", 12);
        CommonComponent.addComponent(this, contactLabel, 2, 3, 1, 1); 
        contactNumberField = CommonComponent.configureContactNumberField(contactNumberField);
        CommonComponent.addComponent(this, contactNumberField, 3, 3, 3, 1);
        
        TextLabel specializationLabel = new TextLabel("Select specialization/s:", 12);
        CommonComponent.addComponent(this, specializationLabel, 0, 4, 1, 1); 
        specialization = new JCheckBox[4];
        checked = new ArrayList<>();

        int x = 0; // Starting x position
        int y = 5; // Starting y position

        for (int i = 0; i < 4; i++) {
            final int index = i + 1;
            specialization[i] = new JCheckBox(Training.getTrainingList().get(i).getTrainingName());
            specialization[i].addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    JCheckBox checkbox = (JCheckBox) e.getItem();
                    if (checkbox.isSelected()) {
                        checked.add(index);
                    } else {
                        checked.remove(Integer.valueOf(index)); // Remove the Integer object, not the index value
                    }
                }
            });
            
            CommonComponent.addComponent(this, specialization[i], x, y, 1, 1); // Add the checkbox to the panel
            
            x++;
            
            if (x == 2) { // Once x = 2, increment y and reset x to 0
                x = 0;
                y++;
            }
        }
        TextLabel genderLabel = new TextLabel("Gender:", 12);
        CommonComponent.addComponent(this, genderLabel, 2, 4, 1, 1); 

        gender = new ButtonGroup();
        male = CommonComponent.createRadioButton("Male", "male", gender);
        CommonComponent.addComponent(this, male, 3, 4, 1, 1); 
        female = CommonComponent.createRadioButton("Female", "female", gender);
        CommonComponent.addComponent(this, female, 4, 4, 1, 1); 

        

        add = new CustomButton("Add", null, e -> addTrainer(listPanel.getListTrainerPanel()));
        CommonComponent.addComponent(this, add, 3, 6, 1, 1); 
        clear = new CustomButton("Clear", null, e -> clearForm());
        CommonComponent.addComponent(this, clear, 4,6, 1, 1);
    }

    private void addTrainer(ListTrainerPanel listTrainerPanel){
        try(Connection conn = MySQL.getConnection()){
            PreparedStatement statement = conn.prepareStatement("INSERT INTO trainers VALUES (null, ?, ?, ?, ?, YEAR(CURDATE()) - YEAR(trainer_birthdate) - (DATE_FORMAT(CURDATE(), '00-%m-%d') < DATE_FORMAT(trainer_birthdate, '00-%m-%d')), ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, firstNameField.getText());
            statement.setString(2, middleNameField.getText());
            statement.setString(3, lastNameField.getText());
            statement.setDate(4, Date.valueOf(dateField.getText()));
            statement.setLong(5, Long.valueOf(contactNumberField.getText()));
            statement.setString(6, gender.getSelection().getActionCommand());
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 0) {
                Messages.trainerAdded();
                ResultSet getID = statement.getGeneratedKeys();
                if(getID.next()){
                    PreparedStatement statements = conn.prepareStatement("INSERT INTO trainer_specialization VALUES (null, ?, ?)");
                    for(int i = 0; i < checked.size(); i++){
                        statements.setInt(1, getID.getInt(1));
                        statements.setInt(2, checked.get(i));
                        statements.addBatch();
                    }   
                    statements.executeBatch();
                }
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
