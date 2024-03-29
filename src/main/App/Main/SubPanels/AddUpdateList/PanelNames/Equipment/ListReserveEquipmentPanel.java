package main.App.Main.SubPanels.AddUpdateList.PanelNames.Equipment;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;

import main.App.Main.SubPanels.AddUpdateList.AddUpdateListPanel;
import main.App.Main.SubPanels.AddUpdateList.BackPanel;
import main.Database.MySQL;


public class ListReserveEquipmentPanel extends JPanel {
    private BackPanel backPanel;
    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    public ListReserveEquipmentPanel(AddUpdateListPanel addUpdateListPanel){
        setLayout(new BorderLayout());
        backPanel = new BackPanel(addUpdateListPanel);
        add(backPanel, BorderLayout.NORTH);
        tableModel = new DefaultTableModel();
        table = new JTable();
        retrieveDataFromDatabase();
        table.getTableHeader().setReorderingAllowed(false);
        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(650,350));
        add(scrollPane, BorderLayout.CENTER);
    }

    public void retrieveDataFromDatabase() {
        tableModel.setRowCount(0);
        try (Connection conn = MySQL.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("SELECT r.reservation_id, CONCAT(m.member_firstname, ' ', m.member_middlename, ' ', m.member_lastname) AS member_fullname, et.equipment_type_name, e.equipment_id, r.reservation_date FROM reservations r INNER JOIN members m ON r.member_id = m.member_id INNER JOIN equipments e ON r.equipment_id = e.equipment_id INNER JOIN equipment_types et ON r.equipment_type_id = et.equipment_type_id ORDER BY r.reservation_id;");
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
        if (columnName.startsWith("appointment_")) {
            return columnName.substring(12);
        }
        return columnName;
    }

    public void updateTableData() {
        tableModel.setRowCount(0);
        retrieveDataFromDatabase();
    }
}

