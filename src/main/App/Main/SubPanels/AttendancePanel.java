package main.App.Main.SubPanels;
import javax.swing.*;

import main.Miscellanous.Constants;
import main.Properties.CustomButton;

public class AttendancePanel extends JPanel {
    private CustomButton attendanceForm, attendanceList;

    public AttendancePanel(){
        attendanceForm = new CustomButton("Attendance Form", Constants.ATTENDANCE_FORM_ICON);
        add(attendanceForm);
        attendanceList = new CustomButton("Attendance List");
        add(attendanceList);

    }
}
