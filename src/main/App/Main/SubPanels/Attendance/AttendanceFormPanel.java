package main.App.Main.SubPanels.Attendance;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
    private JCheckBox withNoMiddleName;
    private CustomButton searchButton, clockInMemberButton;

    private String fullNameQuery = "concat(member_firstname, ' ', member_middlename, ' ', member_lastname)";
    private int memberID;

    public AttendanceFormPanel(AttendancePanel attendancePanel){
        setLayout(new BorderLayout());
        backPanel = new BackPanel(attendancePanel);
        add(backPanel, BorderLayout.NORTH);
        mainPanel = new JPanel(new GridBagLayout());
        add(mainPanel, BorderLayout.CENTER);
        searchMemberField = CommonComponent.configureTextField(mainPanel, "Search member", 0, 0, 3, 1);
        withNoMiddleName = new JCheckBox("Without middle name?");
        withNoMiddleName.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent e) {
                if(withNoMiddleName.isSelected()) fullNameQuery = "concat(member_firstname, ' ', member_lastname)";
                else fullNameQuery = "concat(member_firstname, ' ', member_middlename, ' ', member_lastname)";
            } 
        });
        CommonComponent.addComponent(mainPanel, withNoMiddleName, 0, 1, 1, 1);
        searchButton = new CustomButton("Search member", null, e -> searchMember());
        CommonComponent.addComponent(mainPanel, searchButton, 1, 1, 1, 1);
        clockInMemberButton = new CustomButton("Clock-in member", null, e -> clockInMember());
        CommonComponent.addComponent(mainPanel, clockInMemberButton, 2, 1,1,1);
        clockInMemberButton.setEnabled(false);
    }

    private void searchMember(){
        if(!searchAttendance()){
            try (Connection conn = MySQL.getConnection()) {
                PreparedStatement statement = conn.prepareStatement("select member_id, "+ fullNameQuery +" as full_name from members where not exists (select * from attendances where attendances.member_id = members.member_id and date(attendances.attendance_date) = curdate()) and " + fullNameQuery + " = ?");
                statement.setString(1, searchMemberField.getText());
                ResultSet resultSet = statement.executeQuery();
                if(resultSet.next()){
                    memberID = resultSet.getInt("member_id");
                    Messages.memberFound();
                    clockInMemberButton.setEnabled(true);
                    revalidate();
                    repaint();
                } else {
                    Messages.memberNotFound();
                    clockInMemberButton.setEnabled(false);
                }
                conn.close();
            } catch (SQLException e) {
                Messages.databaseConnectionFailed();
                e.printStackTrace();
            }
        }
    }

    private boolean searchAttendance(){
        try (Connection conn = MySQL.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("select member_id, "+ fullNameQuery +" as full_name from members where exists (select * from attendances where attendances.member_id = members.member_id and date(attendances.attendance_date) = curdate()) and " + fullNameQuery + " = ?");
            statement.setString(1, searchMemberField.getText());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                Messages.memberAlreadyClockedIn();
                revalidate();
                repaint();
                return true;
            } else Messages.memberNotFound();
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
                memberID = 0;
            } else Messages.clockInMemberFailed();
            conn.close();
        } catch (SQLException e){
            Messages.databaseConnectionFailed();
            e.printStackTrace();
        }
    }
}
