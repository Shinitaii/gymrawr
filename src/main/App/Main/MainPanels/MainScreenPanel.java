package main.App.Main.MainPanels;
import java.awt.CardLayout;
import javax.swing.*;
import main.App.Main.SubPanels.*;
import main.App.Main.SubPanels.AddUpdateList.AddUpdateListPanel;
import main.App.Main.SubPanels.AddUpdateList.PanelNames.Trainer.AssignTrainerPanel;
import main.App.Main.SubPanels.AddUpdateList.PanelNames.Trainer.ListAssignedTrainerPanel;
import main.App.Main.SubPanels.Attendance.AttendancePanel;
import main.Miscellanous.Constants;
import main.Objects.User;

public class MainScreenPanel extends JPanel{

    private CardLayout cardLayout;
    private HomepagePanel homepagePanel;
    private AddUpdateListPanel memberPanel, trainerPanel, equipmentPanel, staffPanel;
    private AttendancePanel attendancePanel;
    private SalesReportPanel salesReportPanel;
    private POSPanel posPanel;

    private AssignTrainerPanel assignTrainerPanel;
    private ListAssignedTrainerPanel listAssignedTrainerPanel;

    public MainScreenPanel(User user, DashboardPanel dashboardPanel){
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        homepagePanel = new HomepagePanel(user, dashboardPanel);
        add(homepagePanel, "Homepage");
        memberPanel = new AddUpdateListPanel("Member", Constants.BLUE_MEMBER_ICON, 25);
        add(memberPanel, "Member");
        trainerPanel = new AddUpdateListPanel("Trainer", Constants.BLUE_TRAINER_ICON, 25);
        assignTrainerPanel = new AssignTrainerPanel(trainerPanel, listAssignedTrainerPanel);
        trainerPanel.add(assignTrainerPanel, "Assign Trainer");
        listAssignedTrainerPanel = new ListAssignedTrainerPanel(trainerPanel);
        trainerPanel.add(listAssignedTrainerPanel, "List Assign Trainer");
        trainerPanel.addButton("Assign Trainer", Constants.ASSIGN_TRAINER_ICON, e -> assignTrainer());
        trainerPanel.addButton("List of Assigned Trainer", Constants.LIST_ICON, e -> trainerPanel.getCardLayout().show(trainerPanel, "List Assign Trainer"));
        add(trainerPanel, "Trainer");
        equipmentPanel = new AddUpdateListPanel("Equipment", Constants.BLUE_EQUIPMENT_ICON, 25);
        equipmentPanel.removeListButton("Equipment");
        add(equipmentPanel, "Equipment");
        attendancePanel = new AttendancePanel("Attendance", Constants.BLUE_ATTENDANCE_ICON, 25);
        add(attendancePanel, "Attendance");
        salesReportPanel = new SalesReportPanel();
        add(salesReportPanel, "Sales Report");
        staffPanel = new AddUpdateListPanel("Staff", Constants.BLUE_STAFF_ICON, 25);
        add(staffPanel, "Staff");
        posPanel = new POSPanel(user, salesReportPanel);
        add(posPanel, "POS");

    }

    public void showHomepage(DashboardPanel dashboardPanel){
        cardLayout.show(this, "Homepage");
        dashboardPanel.setActiveButton(dashboardPanel.getHomepage());
    }

    public void showMember(DashboardPanel dashboardPanel){
        cardLayout.show(this, "Member");
        dashboardPanel.setActiveButton(dashboardPanel.getMember());
    }

    public void showTrainer(DashboardPanel dashboardPanel){
        cardLayout.show(this,"Trainer");
        dashboardPanel.setActiveButton(dashboardPanel.getTrainer());
    }

    public void showEquipment(DashboardPanel dashboardPanel){
        cardLayout.show(this, "Equipment");
        dashboardPanel.setActiveButton(dashboardPanel.getEquipment());
    }

    public void showAttendance(DashboardPanel dashboardPanel){
        cardLayout.show(this, "Attendance");
        dashboardPanel.setActiveButton(dashboardPanel.getAttendance());
    }

    public void showPayment(DashboardPanel dashboardPanel){
        cardLayout.show(this,"Payment");
        dashboardPanel.setActiveButton(dashboardPanel.getPayment());
    }

    public void showSalesReport(DashboardPanel dashboardPanel){
        cardLayout.show(this,"Sales Report");
        dashboardPanel.setActiveButton(dashboardPanel.getSalesReport());
    }

    public void showStaff(DashboardPanel dashboardPanel){
        cardLayout.show(this, "Staff");
        dashboardPanel.setActiveButton(dashboardPanel.getStaff());
    }

    public void showPOS(DashboardPanel dashboardPanel){
        cardLayout.show(this,"POS");
        dashboardPanel.setActiveButton(dashboardPanel.getPOS());
    }

    private void assignTrainer(){
        trainerPanel.getCardLayout().show(trainerPanel, "Assign Trainer");
        assignTrainerPanel.updateMemberTableData();
    }
}
