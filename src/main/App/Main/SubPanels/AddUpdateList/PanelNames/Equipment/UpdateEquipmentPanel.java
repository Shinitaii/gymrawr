package main.App.Main.SubPanels.AddUpdateList.PanelNames.Equipment;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import main.Database.MySQL;
import main.Miscellanous.CommonComponent;
import main.Miscellanous.Constants;
import main.Miscellanous.Messages;
import main.Objects.EquipmentType;
import main.Properties.Custom.CustomButton;

public class UpdateEquipmentPanel extends JPanel {

    private JTextField equipmentNameField;
    private CustomButton searchEquipment;
    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    private JComboBox<String> selection;
    private int[] selectionEquipmentType;

public UpdateEquipmentPanel(){
    setLayout(new GridBagLayout());
    tableModel = new DefaultTableModel();
    table = new JTable();
    table.setModel(tableModel);
    table.getTableHeader().setReorderingAllowed(false);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    scrollPane = new JScrollPane(table);
    scrollPane.setPreferredSize(new Dimension(400, 300));
    CommonComponent.addComponent(this, scrollPane, 0, 0, 3, 2);
    equipmentNameField = CommonComponent.configureTextField(this, "Equipment name", 0, 2, 1, 1);
    selectionEquipmentType = new int[EquipmentType.getEquipmentTypeList().size() + 1];
    selection = new JComboBox<>();
    selection = CommonComponent.configureEquipmentTypeComboBox(selection, selectionEquipmentType);
    CommonComponent.addComponent(this, selection, 1, 2, 1, 1);
    searchEquipment = new CustomButton("Search equipment", null, e -> searchEquipment());
    CommonComponent.addComponent(this, searchEquipment, 2, 2, 1, 1);
    table.addMouseListener(new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
            JTable target = (JTable) e.getSource();
            int selectedRow = target.getSelectedRow();

            if (selectedRow >= 0) {
                if (e.getClickCount() == 2) {
                    int equipmentID = getIntTableValue(target, selectedRow, 0);
                    String availablity = getStringTableValue(target, selectedRow, 3);
                    int option = JOptionPane.showOptionDialog(null, "Select action", Constants.APP_TITLE, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]{ "Change Availability", "Cancel"}, null);
                    if(option == 0) updateAvailability(equipmentID, availablity);
                }
            }
        }
    });
    table.setDefaultEditor(Object.class, null);
    }

    private int getIntTableValue(JTable table, int row, int column){
        return Integer.valueOf(String.valueOf(table.getValueAt(row, column)));
    }

    private String getStringTableValue(JTable table, int row, int column){
        return String.valueOf(table.getValueAt(row, column));
    }

    private void updateAvailability(int id, String availability){
        try (Connection conn = MySQL.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("update equipments set equipment_availability = ? where equipment_id = ?");
            if(availability.equals("true"))statement.setBoolean(1, Boolean.valueOf(false));
            else statement.setBoolean(1, Boolean.valueOf(true));
            statement.setInt(2, id);
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 0){
                Messages.equipmentUpdated();
                searchEquipment();
            } else Messages.equipmentNotUpdated();
        } catch (SQLException e){
            Messages.databaseConnectionFailed();
            e.printStackTrace();
        }
    }

    public void searchEquipment(){
        int select = selection.getSelectedIndex() + 1;

        retrieveDataFromDatabase(select);
    }

    private void retrieveDataFromDatabase(int select) {
        tableModel.setRowCount(0);
        System.out.println(select);
        String sql = "SELECT e.equipment_id, et.equipment_type_name, e.equipment_name, e.equipment_availability FROM equipments e JOIN equipment_types et ON e.equipment_type_id = et.equipment_type_id WHERE 1=1";
        sql += " AND et.equipment_type_id = ?";
        if(!equipmentNameField.getText().equals("Equipment name")){
            sql += " AND e.equipment_name = ?";        
        }
        try (Connection conn = MySQL.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, select);
            if(!equipmentNameField.getText().equals("Equipment name")){
                statement.setString(2, "%" + equipmentNameField.getText() + "%");
            }
            System.out.println("SQL Query: " + statement.toString());
            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            Object[] columnNames = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                columnNames[i - 1] = removePrefix(columnName);
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

    private String removePrefix(String columnName) {
        if (columnName.startsWith("equipment_")) {
            return columnName.substring(10);
        } else if (columnName.startsWith("equipment_type_")){
            return columnName.substring(15);
        }
        return columnName;
    }
}
