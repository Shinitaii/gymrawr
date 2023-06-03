package main.App.Main.MainPanels;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import main.App.Login.LoginWindow;
import main.Miscellanous.Constants;
import main.Objects.User;
import main.Properties.Custom.CustomButton;

public class DashboardPanel extends JPanel{

    private MainScreenPanel mainScreenPanel;
    private CustomButton homePage, member, trainers, equipment, attendance, payment, salesReport, staff, pos, logOut;
    public DashboardPanel(User user){
        setLayout(new GridLayout(10, 1));
        homePage = new CustomButton("Homepage", Constants.scaledImage(Constants.HOMEPAGE_ICON), e -> mainScreenPanel.showHomepage(this));
        add(homePage);
        member = new CustomButton("Members", Constants.scaledImage(Constants.MEMBER_ICON), e -> mainScreenPanel.showMember(this));
        add(member);
        trainers = new CustomButton("Trainers", Constants.scaledImage(Constants.TRAINER_ICON), e -> mainScreenPanel.showTrainer(this));
        add(trainers);
        equipment = new CustomButton("Equipment", Constants.scaledImage(Constants.EQUIPMENT_ICON), e -> mainScreenPanel.showEquipment(this));
        add(equipment);
        attendance = new CustomButton("Attendance", Constants.scaledImage(Constants.ATTENDANCE_ICON), e -> mainScreenPanel.showAttendance(this));
        add(attendance);
        salesReport = new CustomButton("Sales Report", Constants.scaledImage(Constants.SALES_REPORT_ICON), e -> mainScreenPanel.showSalesReport(this));
        add(salesReport);
        staff = new CustomButton("Staff", Constants.scaledImage(Constants.STAFF_ICON), e -> mainScreenPanel.showStaff(this));
        add(staff);
        pos = new CustomButton("POS", Constants.scaledImage(Constants.POS_ICON), e -> mainScreenPanel.showPOS(this));
        add(pos);
        logOut = new CustomButton("Log out", Constants.scaledImage(Constants.LOGOUT_ICON), new ActionListener(){
            public void actionPerformed(ActionEvent e){
                int answer = JOptionPane.showConfirmDialog(null, "Do you want to log out?", Constants.APP_TITLE, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(answer == JOptionPane.YES_OPTION) logOut();
            }
        });
        add(logOut);

        setActiveButton(homePage);

        if(user.getUserID() == 2){
            remove(staff);
        }
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

    public void setActiveButton(CustomButton button) {
        for (Component component : getComponents()) {
            if (component instanceof CustomButton) {
                ((CustomButton) component).setHighlighted(false); // Reset the button's highlight
            }
        }
        button.setHighlighted(true);
    }

    public CustomButton getHomepage(){
        return homePage;
    }
    public CustomButton getMember(){
        return member;
    }
    public CustomButton getTrainer(){
        return trainers;
    }
    public CustomButton getEquipment(){
        return equipment;
    }
    public CustomButton getAttendance(){
        return attendance;
    }
    public CustomButton getPayment(){
        return payment;
    }
    public CustomButton getSalesReport(){
        return salesReport;
    }
    public CustomButton getStaff(){
        return staff;
    }
    public CustomButton getPOS(){
        return pos;
    }
}
