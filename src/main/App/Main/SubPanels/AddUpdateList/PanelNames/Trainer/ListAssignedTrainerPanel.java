package main.App.Main.SubPanels.AddUpdateList.PanelNames.Trainer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;

import main.App.Main.SubPanels.AddUpdateList.AddUpdateListPanel;
import main.App.Main.SubPanels.AddUpdateList.BackPanel;
import main.Database.MySQL;


public class ListAssignedTrainerPanel extends JPanel {
    private BackPanel backPanel;
    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    public ListAssignedTrainerPanel(AddUpdateListPanel addUpdateListPanel){
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
        try (Connection conn = MySQL.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("SELECT CONCAT(m.member_firstname, ' ', m.member_middlename, ' ', m.member_lastname) AS member_fullname, CONCAT(t.trainer_firstname, ' ', t.trainer_middlename, ' ', t.trainer_lastname) AS trainer_fullname, tr.training_name, a.appointment_startdate, a.appointment_enddate FROM appointments a INNER JOIN members m ON a.member_id = m.member_id INNER JOIN trainer_specialization ts ON a.trainer_id = ts.trainer_id INNER JOIN trainers t ON ts.trainer_id = t.trainer_id INNER JOIN trainings tr ON ts.training_id = tr.training_id");
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

