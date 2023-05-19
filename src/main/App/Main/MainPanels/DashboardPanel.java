package main.App.Main.MainPanels;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import main.App.Login.LoginWindow;
import main.Miscellanous.Constants;
import main.Properties.CustomButton;

public class DashboardPanel extends JPanel{

    private MainScreenPanel mainScreenPanel;
    private CustomButton homePage, member, trainers, equipment, attendance, payment, salesReport, staff, pos, logOut;
    
    public DashboardPanel(){
        setLayout(new GridLayout(10, 1));

        homePage = new CustomButton("Homepage", Constants.scaledImage(Constants.HOMEPAGE_ICON), e -> mainScreenPanel.showHomepage());
        add(homePage);
        member = new CustomButton("Members", Constants.scaledImage(Constants.MEMBER_ICON), e -> mainScreenPanel.showMember());
        add(member);
        trainers = new CustomButton("Trainers", Constants.scaledImage(Constants.TRAINER_ICON), e -> mainScreenPanel.showTrainer());
        add(trainers);
        equipment = new CustomButton("Equipment", Constants.scaledImage(Constants.EQUIPMENT_ICON), e -> mainScreenPanel.showEquipment());
        add(equipment);
        attendance = new CustomButton("Attendance", Constants.scaledImage(Constants.ATTENDANCE_ICON), e -> mainScreenPanel.showAttendance());
        add(attendance);
        payment = new CustomButton("Payment", Constants.scaledImage(Constants.PAYMENT_ICON), e -> mainScreenPanel.showPayment());
        add(payment);
        salesReport = new CustomButton("Sales Report", Constants.scaledImage(Constants.SALES_REPORT_ICON), e -> mainScreenPanel.showSalesReport());
        add(salesReport);
        staff = new CustomButton("Staff", Constants.scaledImage(Constants.STAFF_ICON), e -> mainScreenPanel.showStaff());
        add(staff);
        pos = new CustomButton("POS", Constants.scaledImage(Constants.POS_ICON), e -> mainScreenPanel.showPOS());
        add(pos);
        logOut = new CustomButton("Log out", Constants.scaledImage(Constants.LOGOUT_ICON), new ActionListener(){
            public void actionPerformed(ActionEvent e){
                int answer = JOptionPane.showConfirmDialog(null, "Do you want to log out?", Constants.APP_TITLE, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(answer == JOptionPane.YES_OPTION) logOut();
            }
        });
        add(logOut);
    }

    private void logOut(){
        SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					LoginWindow window = new LoginWindow();
					window.getWindow().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

        SwingUtilities.getWindowAncestor(this).dispose();
    }

    public void setMainScreen(MainScreenPanel mainScreenPanel){
        this.mainScreenPanel = mainScreenPanel;
    }

    public MainScreenPanel getMainScreen(){
        return mainScreenPanel;
    }
}
