package main.App.Main.SubPanels.Attendance;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

import main.App.Main.SubPanels.AddUpdateList.BackPanel;
import main.Database.MySQL;
import main.Miscellanous.CommonComponent;
import main.Properties.Custom.CustomButton;
public class AttendanceRecordPanel extends JPanel{

    private BackPanel backPanel;
    private JPanel mainPanel;
    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    private JTextField searchMemberField;
    private CustomButton searchMemberButton;

    public AttendanceRecordPanel(AttendancePanel attendancePanel, AttendanceFormPanel attendanceFormPanel){
        setLayout(new BorderLayout());
        backPanel = new BackPanel(attendancePanel);
        add(backPanel, BorderLayout.NORTH);
        mainPanel = new JPanel(new GridBagLayout());
        add(mainPanel, BorderLayout.CENTER);
        tableModel = new DefaultTableModel();
        table = new JTable();
        retrieveDataFromDatabase(1);
        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        CommonComponent.addComponent(mainPanel, scrollPane, 0, 0, 2, 2);
        searchMemberField = CommonComponent.configureTextField(mainPanel, "Member full name", 0, 2, 1, 1);
        searchMemberButton = new CustomButton("Search member", null, e -> searchMember());
        CommonComponent.addComponent(mainPanel, searchMemberButton, 1, 2, 1, 1);
    }

    private void searchMember(){
        int select;
        if(!searchMemberField.getText().equals("Member full name")) select = 1;
        else select = 0;

        retrieveDataFromDatabase(select);
    }

    public void retrieveDataFromDatabase(int select) {
        String sql = "";
        if(select == 1) sql = "SELECT attendances.attendance_id, concat(members.member_firstname, ' ', members.member_middlename, ' ', members.member_lastname) as fullname, date(attendances.attendance_date) as attendance_date, time(attendances.attendance_date) as attendance_time FROM attendances JOIN members ON members.member_id = attendances.member_id";
        else sql = "SELECT attendances.attendance_id, concat(members.member_firstname, ' ', members.member_middlename, ' ', members.member_lastname) AS fullname, date(attendances.attendance_date) as attendance_date, time(attendances.attendance_date) as attendance_time FROM attendances JOIN members ON members.member_id = attendances.member_id WHERE concat(members.member_firstname, ' ', members.member_middlename, ' ', members.member_lastname) = ? ";
        try (Connection conn = MySQL.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            if(select == 0) statement.setString(1, searchMemberField.getText());
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
}
