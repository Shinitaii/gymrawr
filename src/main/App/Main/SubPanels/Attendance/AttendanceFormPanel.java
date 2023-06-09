package main.App.Main.SubPanels.Attendance;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.sql.*;

import javax.swing.*;

import main.App.Main.SubPanels.AddUpdateList.BackPanel;
import main.Database.MySQL;
import main.Miscellanous.CommonComponent;
import main.Miscellanous.Messages;
import main.Properties.Custom.CustomButton;

public class AttendanceFormPanel extends JPanel{

    private BackPanel backPanel;
    private JPanel mainPanel;
    private JTextField searchMemberField;
    private CustomButton searchButton, clockInMemberButton;
    private int memberID;

    public AttendanceFormPanel(AttendancePanel attendancePanel){
        setLayout(new BorderLayout());
        backPanel = new BackPanel(attendancePanel);
        add(backPanel, BorderLayout.NORTH);
        JPanel contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

        mainPanel = new JPanel(new GridBagLayout());
        contentPanel.add(mainPanel, BorderLayout.CENTER);
        searchMemberField = CommonComponent.configureTextField(mainPanel, "Search member", 0, 0, 3, 1);
        searchButton = new CustomButton("Search member", null, e -> searchMember());
        CommonComponent.addComponent(mainPanel, searchButton, 1, 1, 1, 1);
        clockInMemberButton = new CustomButton("Clock-in member", null, e -> clockInMember());
        CommonComponent.addComponent(mainPanel, clockInMemberButton, 2, 1,1,1);
        clockInMemberButton.setEnabled(false);
    }

    private void searchMember() {
        if (!searchAttendance()) {
            boolean memberFound = false;
            try (Connection conn = MySQL.getConnection()) {
                PreparedStatement statement = conn.prepareStatement("SELECT member_id, CONCAT(member_firstname, ' ', member_middlename, ' ', member_lastname) AS full_name FROM members WHERE NOT EXISTS (SELECT * FROM attendances WHERE attendances.member_id = members.member_id AND DATE(attendances.attendance_date) = CURDATE()) AND CONCAT(member_firstname, ' ', member_middlename, ' ', member_lastname) = ?");
                statement.setString(1, searchMemberField.getText());
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    memberID = resultSet.getInt("member_id");
                    memberFound = true;
                    clockInMemberButton.setEnabled(true);
                    revalidate();
                    repaint();
                }
                conn.close();
            } catch (SQLException e) {
                Messages.databaseConnectionFailed();
                e.printStackTrace();
            }

            if (memberFound) {
                Messages.memberFound();
            } else {
                Messages.memberNotFound();
                clockInMemberButton.setEnabled(false);
            }
        } else clockInMemberButton.setEnabled(false);
    }

    private boolean searchAttendance(){
        try (Connection conn = MySQL.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("select member_id, concat(member_firstname, ' ', member_middlename, ' ', member_lastname) as full_name from members where exists (select * from attendances where attendances.member_id = members.member_id and date(attendances.attendance_date) = curdate()) and concat(member_firstname, ' ', member_middlename, ' ', member_lastname) = ?");
            statement.setString(1, searchMemberField.getText());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                Messages.memberAlreadyClockedIn();
                revalidate();
                repaint();
                return true;
            }
            conn.close();
            return false;
        } catch (SQLException e) {
            Messages.databaseConnectionFailed();
            e.printStackTrace();
        }
        return false;
    }

    private void clockInMember(){
        try (Connection conn = MySQL.getConnection()){
            PreparedStatement statement = conn.prepareStatement("insert into attendances values (null, ?, CURRENT_TIMESTAMP)");
            statement.setInt(1, memberID);
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 0) {
                Messages.clockInMemberSuccessfully();
            } else Messages.clockInMemberFailed();
            conn.close();
        } catch (SQLException e){
            Messages.databaseConnectionFailed();
            e.printStackTrace();
        }
    }
}
