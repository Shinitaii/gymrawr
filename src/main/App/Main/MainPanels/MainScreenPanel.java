package main.App.Main.MainPanels;
import java.awt.CardLayout;
import javax.swing.*;
import main.App.Main.SubPanels.*;
import main.Miscellanous.Constants;
import main.Properties.AddUpdateList.AddUpdateListPanel;
import main.Properties.Custom.CustomPanel;

public class MainScreenPanel extends JPanel{

    private CardLayout cardLayout;
    private HomepagePanel homepagePanel;
    private AddUpdateListPanel memberPanel, trainerPanel, equipmentPanel, staffPanel;
    private CustomPanel attendancePanel, paymentPanel, salesReportPanel;
    private POSPanel posPanel;

    public MainScreenPanel(DashboardPanel dashboardPanel){
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        homepagePanel = new HomepagePanel(dashboardPanel);
        add(homepagePanel, "Homepage");
        memberPanel = new AddUpdateListPanel("Member", Constants.BLUE_MEMBER_ICON, 25);
        add(memberPanel, "Member");
        trainerPanel = new AddUpdateListPanel("Trainer", Constants.BLUE_TRAINER_ICON, 25);
        add(trainerPanel, "Trainer");
        equipmentPanel = new AddUpdateListPanel("Equipment", Constants.BLUE_EQUIPMENT_ICON, 25);
        add(equipmentPanel, "Equipment");
        attendancePanel = new CustomPanel("Attendance", Constants.BLUE_ATTENDANCE_ICON, 25);
        add(attendancePanel, "Attendance");
        paymentPanel = new CustomPanel("Payment", Constants.BLUE_PAYMENT_ICON, 25);
        add(paymentPanel, "Payment");
        salesReportPanel = new CustomPanel("Sales Report", Constants.BLUE_SALES_REPORT_ICON, 25);
        add(salesReportPanel, "Sales Report");
        staffPanel = new AddUpdateListPanel("Staff", Constants.BLUE_STAFF_ICON, 25);
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
