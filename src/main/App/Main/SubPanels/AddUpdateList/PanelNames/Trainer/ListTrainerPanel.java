package main.App.Main.SubPanels.AddUpdateList.PanelNames.Trainer;
import java.awt.Dimension;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;

import main.Database.MySQL;
public class ListTrainerPanel extends JPanel{
    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    public ListTrainerPanel(){
        tableModel = new DefaultTableModel();
        table = new JTable();
        retrieveDataFromDatabase();
        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(650,350));
        add(scrollPane);
    }

    public void retrieveDataFromDatabase() {
        try (Connection conn = MySQL.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("SELECT trainer_id, training_id, concat(trainer_firstname, ' ', trainer_middlename, ' ', trainer_lastname) as fullname, trainer_birthdate, trainer_contactnumber, trainer_gender FROM trainers");
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
        if (columnName.startsWith("trainer_")) {
            return columnName.substring(8);
        }
        return columnName;
    }

    public void updateTableData() {
        tableModel.setRowCount(0);
        retrieveDataFromDatabase();
    }
}
