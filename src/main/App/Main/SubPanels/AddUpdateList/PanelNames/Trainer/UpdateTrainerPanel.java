package main.App.Main.SubPanels.AddUpdateList.PanelNames.Trainer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import main.Database.MySQL;
import main.Miscellanous.CommonComponent;
import main.Miscellanous.Messages;
import main.Objects.Training;
import main.Properties.Focus;
import main.Properties.Custom.*;

public class UpdateTrainerPanel extends JPanel{
    private JPanel searchMemberPanel, editMemberPanel;
    private TextLabel fullNameLabel;
    private JTextField fullNameField;
    private JCheckBox withNoMiddleName;
    private CustomButton check;

    private JTextField firstNameField, middleNameField, lastNameField;
    private JFormattedTextField dateField, contactNumberField;
    private JComboBox<String> selection;
    private CustomButton update, clear, cancel;

    private int[] selectionID;

    private String fullNameQuery = "concat(trainer_firstname, ' ', trainer_middlename, ' ', trainer_lastname)";

    public UpdateTrainerPanel(){
        searchMemberPanel = new JPanel(new GridBagLayout());
        add(searchMemberPanel);
        
        fullNameLabel = new TextLabel("Enter full name:", 12);
        CommonComponent.addComponent(searchMemberPanel, fullNameLabel, 0, 0, 1, 1);
        fullNameField = CommonComponent.configureTextField(searchMemberPanel, fullNameField, "Full name", 1, 0, 1, 1);
        withNoMiddleName = new JCheckBox("With no middle name?");
        withNoMiddleName.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent e) {
                if(withNoMiddleName.isSelected()) fullNameQuery = "concat(trainer_firstname, ' ', trainer_lastname)";
                else fullNameQuery = "concat(trainer_firstname, ' ', trainer_middlename, ' ', trainer_lastname)";
            } 
        });
        CommonComponent.addComponent(searchMemberPanel, withNoMiddleName, 0, 1, 1, 1);
        check = new CustomButton("Check member", null, e->checkTrainer());
        CommonComponent.addComponent(searchMemberPanel, check, 0, 3, 1, 1);
        update = new CustomButton("Update");

        editMemberPanel = new JPanel(new GridBagLayout());
        add(editMemberPanel);
        editMemberPanel.setVisible(false);

        firstNameField = CommonComponent.configureTextField(editMemberPanel, firstNameField, "First name", 0, 0, 2, 1);;
        middleNameField = CommonComponent.configureTextField(editMemberPanel, middleNameField, "Middle name", 0, 1, 2, 1);
        lastNameField = CommonComponent.configureTextField(editMemberPanel, lastNameField, "Last name", 0, 2, 2, 1); 

        TextLabel birthLabel = new TextLabel("Birth Date: (YYYY/MM/DD)", 12);
        CommonComponent.addComponent(editMemberPanel, birthLabel, 0, 3, 1, 1); 
        
        dateField = CommonComponent.configureDateField(dateField);
        CommonComponent.addComponent(editMemberPanel, dateField, 1, 3, 1, 1); 

        TextLabel contactLabel = new TextLabel("Contact Number: ", 12);
        CommonComponent.addComponent(editMemberPanel, contactLabel, 2, 0, 1, 1); 
        
        contactNumberField = CommonComponent.configureContactNumberField(contactNumberField);
        CommonComponent.addComponent(editMemberPanel, contactNumberField, 3, 0, 2, 1); 

        selectionID = new int[Training.getTrainingList().size()];
        selection = CommonComponent.configureTrainingComboBox(selection, selectionID);
        CommonComponent.addComponent(editMemberPanel, selection, 2, 2, 1, 1);
        update = new CustomButton("Update", null, e -> updateTrainer());
        CommonComponent.addComponent(editMemberPanel, update, 2, 3, 1, 1); 
        clear = new CustomButton("Clear", null, e -> clearForm());
        CommonComponent.addComponent(editMemberPanel, clear, 3, 3, 1, 1);
        cancel = new CustomButton("Cancel", null, e-> cancel());
        CommonComponent.addComponent(editMemberPanel, cancel, 4,3, 1, 1);

    }

    private void checkTrainer(){
        try (Connection conn = MySQL.getConnection()){
            PreparedStatement statement = conn.prepareStatement("select * from trainers where " + fullNameQuery + " = ?");
            statement.setString(1, fullNameField.getText());
            ResultSet result = statement.executeQuery();
            if(result.next()){
                Messages.trainerFound();
                searchMemberPanel.setVisible(false);
                editMemberPanel.setVisible(true);
                selection.setSelectedIndex(result.getInt("training_id") - 1);
                firstNameField.setText(result.getString("trainer_firstname"));
                middleNameField.setText(result.getString("trainer_middlename"));
                lastNameField.setText(result.getString("trainer_lastname"));
                dateField.setText(result.getString(String.valueOf("trainer_birthdate")));
                contactNumberField.setText(String.valueOf(result.getLong("trainer_contactnumber")));
                repaint();
                revalidate();
            } else Messages.trainerNotFound();
        } catch (SQLException e) {
            Messages.databaseConnectionFailed();
            e.printStackTrace();
        }
    }

    private void updateTrainer(){
        try(Connection conn = MySQL.getConnection()){
            PreparedStatement statement = conn.prepareStatement("update trainers set training_id = ?, trainer_firstname = ?, trainer_middlename = ?, trainer_lastname = ?, trainer_birthdate = ?, trainer_contactnumber = ? where " + fullNameQuery + " = ?");
            statement.setInt(1, selectionID[selection.getSelectedIndex()]);
            statement.setString(2, firstNameField.getText());
            statement.setString(3, middleNameField.getText());
            statement.setString(4, lastNameField.getText());
            statement.setDate(5, Date.valueOf(dateField.getText()));
            statement.setLong(6, Long.valueOf(contactNumberField.getText()));
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
