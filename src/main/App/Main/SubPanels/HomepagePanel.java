package main.App.Main.SubPanels;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import main.App.Main.MainPanels.DashboardPanel;
import main.Miscellanous.Constants;
import main.Properties.Custom.ButtonPanel;
import main.Properties.Custom.TextPanel;

public class HomepagePanel extends JPanel {
    private TextPanel textPanel;
    private ButtonPanel buttonPanel;

    public HomepagePanel(DashboardPanel dashboardPanel){
        setLayout(new BorderLayout());
        textPanel = new TextPanel("Welcome to GYM RAWR", Constants.APP_ICON, 25);
        add(textPanel, BorderLayout.NORTH);
        buttonPanel = new ButtonPanel();
        add(buttonPanel, BorderLayout.CENTER);

        addButtons(buttonPanel, dashboardPanel);
    }

    private void addButtons(ButtonPanel buttonPanel, DashboardPanel dashboardPanel){
        buttonPanel.addButton("Homepage", Constants.scaledImage(Constants.HOMEPAGE_ICON), e -> dashboardPanel.getMainScreen().showHomepage(dashboardPanel));
        buttonPanel.addButton("Members", Constants.scaledImage(Constants.MEMBER_ICON), e -> dashboardPanel.getMainScreen().showMember(dashboardPanel));
        buttonPanel.addButton("Trainers", Constants.scaledImage(Constants.TRAINER_ICON), e -> dashboardPanel.getMainScreen().showTrainer(dashboardPanel));
        buttonPanel.addButton("Equipment", Constants.scaledImage(Constants.EQUIPMENT_ICON), e -> dashboardPanel.getMainScreen().showEquipment(dashboardPanel));
        buttonPanel.addButton("Attendance", Constants.scaledImage(Constants.ATTENDANCE_ICON), e -> dashboardPanel.getMainScreen().showAttendance(dashboardPanel));
        buttonPanel.addButton("Payment", Constants.scaledImage(Constants.PAYMENT_ICON), e -> dashboardPanel.getMainScreen().showPayment(dashboardPanel));
        buttonPanel.addButton("Sales Report", Constants.scaledImage(Constants.SALES_REPORT_ICON), e -> dashboardPanel.getMainScreen().showSalesReport(dashboardPanel));
        buttonPanel.addButton("Staff", Constants.scaledImage(Constants.STAFF_ICON), e -> dashboardPanel.getMainScreen().showStaff(dashboardPanel));
        buttonPanel.addButton("POS", Constants.scaledImage(Constants.POS_ICON), e -> dashboardPanel.getMainScreen().showPOS(dashboardPanel));
    }


}
