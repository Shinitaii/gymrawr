package main.App.Main.SubPanels.AddUpdateList.PanelNames.Staff;
import java.awt.Dimension;

import javax.swing.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

import main.Database.MySQL;
public class ListStaffPanel extends JPanel{
    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    public ListStaffPanel(){
        tableModel = new DefaultTableModel();
        table = new JTable();
        retrieveDataFromDatabase();
        table.getTableHeader().setReorderingAllowed(false);
        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(650,350));
        add(scrollPane);
    }

    public void retrieveDataFromDatabase() {
        try (Connection conn = MySQL.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("SELECT staff_id, concat(staff_firstname, ' ', staff_middlename, ' ', staff_lastname) as fullname, staff_birthdate, staff_age, staff_contactnumber, staff_gender, staff_address, staff_educlevel FROM staffs");
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
        if (columnName.startsWith("staff_")) {
            return columnName.substring(6);
        }
        return columnName;
    }

    public void updateTableData() {
        tableModel.setRowCount(0);
        retrieveDataFromDatabase();
    }
}
