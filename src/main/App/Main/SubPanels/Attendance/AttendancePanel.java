package main.App.Main.SubPanels.Attendance;

import javax.swing.*;

import main.Miscellanous.Constants;
import main.Properties.Custom.*;
public class AttendancePanel extends CustomPanel {

    private AttendanceFormPanel attendanceFormPanel;
    private AttendanceRecordPanel attendanceRecordPanel;

    public AttendancePanel(String panelName, ImageIcon icon, int size) {
        super(panelName, icon, size);
        attendanceFormPanel = new AttendanceFormPanel(this);
        add(attendanceFormPanel, "Attendance Form");
        attendanceRecordPanel = new AttendanceRecordPanel(this, attendanceFormPanel);
        add(attendanceRecordPanel, "Attendance Records");
        addButton("Attendance Clock-in", Constants.ATTENDANCE_FORM_ICON, e -> getCardLayout().show(this, "Attendance Form"));
        addButton("Attendance Records", Constants.LIST_ICON, e -> getCardLayout().show(this, "Attendance Records"));
    }
}
