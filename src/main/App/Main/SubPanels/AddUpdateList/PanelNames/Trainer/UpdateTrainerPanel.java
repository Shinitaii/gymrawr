package main.App.Main.SubPanels.AddUpdateList.PanelNames.Trainer;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import main.Database.MySQL;
import main.Miscellanous.*;
import main.Objects.Training;
import main.Properties.Focus;
import main.Properties.Custom.*;

public class UpdateTrainerPanel extends JPanel{
    private JPanel searchMemberPanel, editMemberPanel;
    private TextLabel fullNameLabel;
    private JTextField fullNameField;
    private CustomButton check;

    private JTextField firstNameField, middleNameField, lastNameField;
    private JFormattedTextField dateField, contactNumberField;
    private CustomButton update, clear, cancel;
    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    private JCheckBox[] specialization;
    private ArrayList<Integer> checked;

    public UpdateTrainerPanel(){
        searchMemberPanel = new JPanel(new GridBagLayout());
        add(searchMemberPanel);

        tableModel = new DefaultTableModel();
        table = new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                JTable target = (JTable) e.getSource();
                int selectedRow = target.getSelectedRow();
    
                if (selectedRow >= 0) {
                    checkMember(target.getValueAt(selectedRow, 0).toString());
                    searchMemberPanel.setVisible(false);
                    editMemberPanel.setVisible(true);
                }
            }
        });
        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(350,250));
        CommonComponent.addComponent(searchMemberPanel, scrollPane, 0, 0, 3, 1);
        
        fullNameLabel = new TextLabel("Enter name:", 12);
        CommonComponent.addComponent(searchMemberPanel, fullNameLabel, 0, 1, 1, 1);
        fullNameField = CommonComponent.configureTextField(searchMemberPanel, "Name", 1, 1, 1, 1);
        check = new CustomButton("Check trainer", null, e -> retrieveDataFromDatabase());
        CommonComponent.addComponent(searchMemberPanel, check, 2, 1, 1,1);
        TextLabel instruction = new TextLabel("After results shown from the table, click any one of them to edit their details.", 12);
        CommonComponent.addComponent(searchMemberPanel, instruction, 0, 3, 3, 1);
        editMemberPanel = new JPanel(new GridBagLayout());
        add(editMemberPanel);
        editMemberPanel.setVisible(false);

        //firstname txtfield
        TextLabel firstNameLabel = new TextLabel("First name:", 12);
        CommonComponent.addComponent(editMemberPanel, firstNameLabel, 0,0,1,1);
        firstNameField = CommonComponent.configureTextField(editMemberPanel, "First name", 1, 0, 4, 1);
        //middlename
        TextLabel middleNameLabel = new TextLabel("Middle name:", 12);
        CommonComponent.addComponent(editMemberPanel, middleNameLabel, 0,1,1,1);
        middleNameField = CommonComponent.configureTextField(editMemberPanel, "Middle name", 1, 1, 4, 1);
        //lastname
        TextLabel lastNameLabel = new TextLabel("Last name:", 12);
        CommonComponent.addComponent(editMemberPanel, lastNameLabel, 0,2,1,1);
        lastNameField = CommonComponent.configureTextField(editMemberPanel, "Last name", 1, 2, 4, 1); 

        TextLabel birthLabel = new TextLabel("Birth Date: (YYYY/MM/DD)", 12);
        CommonComponent.addComponent(editMemberPanel, birthLabel, 0, 3, 1, 1);         
        dateField = CommonComponent.configureDateField(dateField);
        CommonComponent.addComponent(editMemberPanel, dateField, 1, 3, 1, 1); 
        TextLabel contactLabel = new TextLabel("Contact Number: ", 12);
        CommonComponent.addComponent(editMemberPanel, contactLabel, 2, 3, 1, 1); 
        contactNumberField = CommonComponent.configureContactNumberField(contactNumberField);
        CommonComponent.addComponent(editMemberPanel, contactNumberField, 3, 3, 3, 1);  
        
        TextLabel specializationLabel = new TextLabel("Select specialization/s:", 12);
        CommonComponent.addComponent(editMemberPanel, specializationLabel, 0, 4, 1, 1); 
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
            
            CommonComponent.addComponent(editMemberPanel, specialization[i], x, y, 1, 1); // Add the checkbox to the panel
            
            x++;
            
            if (x == 2) { // Once x = 2, increment y and reset x to 0
                x = 0;
                y++;
            }
        }

        update = new CustomButton("Update", null, e -> updateMember());  
        CommonComponent.addComponent(editMemberPanel, update, 2, 5, 1, 1); 
        clear = new CustomButton("Clear", null, e -> clearForm());
        CommonComponent.addComponent(editMemberPanel, clear, 3, 5, 1, 1);
        cancel = new CustomButton("Cancel", null, e-> cancel());
        CommonComponent.addComponent(editMemberPanel, cancel, 4,5, 1, 1);
    }

    private void retrieveDataFromDatabase() {
        tableModel.setRowCount(0);
        try (Connection conn = MySQL.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("SELECT concat(trainer_firstname, ' ', trainer_middlename, ' ', trainer_lastname) as fullname FROM trainers where concat(trainer_firstname, ' ', trainer_middlename, ' ', trainer_lastname) like ?");
            statement.setString(1, "%" +fullNameField.getText()+"%");
            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            Object[] columnNames = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                columnNames[i - 1] = columnName;
            }
            tableModel.setColumnIdentifiers(columnNames);

            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                tableModel.addRow(rowData);
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        table.setModel(tableModel);

        table.repaint();
        table.revalidate();
    }

    private void checkMember(String foundName){
        try (Connection conn = MySQL.getConnection()){
            PreparedStatement statement = conn.prepareStatement("select trainer_id, trainer_firstname, trainer_middlename, trainer_lastname,trainer_birthdate, trainer_contactnumber from trainers where concat(trainer_firstname, ' ', trainer_middlename, ' ', trainer_lastname) like ?");
            statement.setString(1, "%"+foundName+"%");
            ResultSet result = statement.executeQuery();
            if(result.next()){
                int id = result.getInt("trainer_id");
                firstNameField.setText(result.getString("trainer_firstname"));
                middleNameField.setText(result.getString("trainer_middlename"));
                lastNameField.setText(result.getString("trainer_lastname"));
                dateField.setText(result.getString(String.valueOf("trainer_birthdate")));
                contactNumberField.setText(String.valueOf(result.getLong("trainer_contactnumber")));
                PreparedStatement specializationStatement = conn.prepareStatement("select training_id from trainer_specialization where trainer_id = ?");
                specializationStatement.setInt(1,id);
                ResultSet specializationResult = specializationStatement.executeQuery();
                Set<String> trainerSpecializations = new HashSet<>();
                while (specializationResult.next()) {
                    int trainingId = specializationResult.getInt("training_id");
    
                    trainerSpecializations.add(specialization[trainingId-1].getText());
                }

                for (JCheckBox box : specialization) {
                    String trainingName = box.getText();
                    
                    if (trainerSpecializations.contains(trainingName)) {
                        box.setSelected(true);
                    }
                }
                repaint();
                revalidate();
            }
        } catch (SQLException e) {
            Messages.databaseConnectionFailed();
            e.printStackTrace();
        }
    }

    private void updateMember(){
        try(Connection conn = MySQL.getConnection()){
            PreparedStatement statement = conn.prepareStatement("update trainers set trainer_firstname = ?, trainer_middlename = ?, trainer_lastname = ?, trainer_birthdate = ?, trainer_contactnumber = ? where concat(trainer_firstname, ' ', trainer_middlename, ' ', trainer_lastname) like ?");
            statement.setString(1, firstNameField.getText());
            statement.setString(2, middleNameField.getText());
            statement.setString(3, lastNameField.getText());
            statement.setDate(4, Date.valueOf(dateField.getText()));
            statement.setLong(5, Long.valueOf(contactNumberField.getText()));
            statement.setString(6, "%" + fullNameField.getText() + "%");
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 0){
                Messages.trainerUpdated();
                PreparedStatement getId = conn.prepareStatement("select trainer_id from trainers where concat(trainer_firstname, ' ', trainer_middlename, ' ', trainer_lastname) = ?");
                getId.setString(1, firstNameField.getText() + " " + middleNameField.getText() + " " + lastNameField.getText());
                ResultSet result = getId.executeQuery();
                if(result.next()){
                    int id = result.getInt("trainer_id");
                    PreparedStatement delete = conn.prepareStatement("DELETE FROM trainer_specialization WHERE training_id = ?");
                    delete.setInt(1, id);
                    delete.executeUpdate();
                    PreparedStatement updateSpecialization = conn.prepareStatement("INSERT INTO trainer_specialization VALUES (null, ?, ?)");
                    for (int i = 0; i < checked.size(); i++) {
                        updateSpecialization.setInt(1, id);
                        updateSpecialization.setInt(2, checked.get(i));
                        updateSpecialization.addBatch();
                    }
                    updateSpecialization.executeBatch();
                }
                editMemberPanel.setVisible(false);
                searchMemberPanel.setVisible(true);
                clearForm();
                cancel();
                revalidate();
                repaint();
            } else Messages.trainerNotUpdated();
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