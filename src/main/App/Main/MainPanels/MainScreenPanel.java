package main.App.Main.MainPanels;
import java.awt.CardLayout;

import javax.swing.*;

import main.App.Main.SubPanels.*;
import main.Miscellanous.Constants;
import main.Properties.Custom.CustomListener;
import main.Properties.Custom.CustomPanel;

public class MainScreenPanel extends JPanel{

    private CardLayout cardLayout;
    private HomepagePanel homepagePanel;
    private CustomPanel memberPanel, trainerPanel, equipmentPanel, staffPanel;
    private AttendancePanel attendancePanel;
    private PaymentPanel paymentPanel;
    private SalesReportPanel salesReportPanel;
    private POSPanel posPanel;
    private CustomListener member, trainer, equipment, staff;

    public MainScreenPanel(DashboardPanel dashboardPanel){
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        member = new CustomListener(0);
        trainer = new CustomListener(1);
        equipment = new CustomListener(2);
        staff = new CustomListener(3);

        homepagePanel = new HomepagePanel(dashboardPanel);
        add(homepagePanel, "Homepage");
        memberPanel = new CustomPanel("Member", Constants.BLUE_MEMBER_ICON, Constants.MEMBER_ICON, member);
        add(memberPanel, "Member");
        trainerPanel = new CustomPanel("Trainer", Constants.BLUE_TRAINER_ICON, Constants.TRAINER_ICON,trainer);
        add(trainerPanel, "Trainer");
        equipmentPanel = new CustomPanel("Equipment", Constants.BLUE_EQUIPMENT_ICON, Constants.EQUIPMENT_ICON, equipment);
        add(equipmentPanel, "Equipment");
        attendancePanel = new AttendancePanel();
        add(attendancePanel, "Attendance");
        paymentPanel = new PaymentPanel();
        add(paymentPanel, "Payment");
        salesReportPanel = new SalesReportPanel();
        add(salesReportPanel, "Sales Report");
        staffPanel = new CustomPanel("Staff", Constants.STAFF_ICON, Constants.STAFF_ICON,staff);
        add(staffPanel, "Staff");
        posPanel = new POSPanel();
        add(posPanel, "POS");
    }

    public void showHomepage(){
        cardLayout.show(this, "Homepage");
    }

    public void showMember(){
        cardLayout.show(this, "Member");
    }

    public void showTrainer(){
        cardLayout.show(this,"Trainer");
    }

    public void showEquipment(){
        cardLayout.show(this, "Equipment");
    }

    public void showAttendance(){
        cardLayout.show(this, "Attendance");
    }

    public void showPayment(){
        cardLayout.show(this,"Payment");
    }

    public void showSalesReport(){
        cardLayout.show(this,"Sales Report");
    }

    public void showStaff(){
        cardLayout.show(this, "Staff");
    }

    public void showPOS(){
        cardLayout.show(this,"POS");
    }
}
