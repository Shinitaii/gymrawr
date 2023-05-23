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
        buttonPanel.addButton("Homepage", Constants.scaledImage(Constants.HOMEPAGE_ICON), e -> dashboardPanel.getMainScreen().showHomepage());
        buttonPanel.addButton("Members", Constants.scaledImage(Constants.MEMBER_ICON), e -> dashboardPanel.getMainScreen().showMember());
        buttonPanel.addButton("Trainers", Constants.scaledImage(Constants.TRAINER_ICON), e -> dashboardPanel.getMainScreen().showTrainer());
        buttonPanel.addButton("Equipment", Constants.scaledImage(Constants.EQUIPMENT_ICON), e -> dashboardPanel.getMainScreen().showEquipment());
        buttonPanel.addButton("Attendance", Constants.scaledImage(Constants.ATTENDANCE_ICON), e -> dashboardPanel.getMainScreen().showAttendance());
        buttonPanel.addButton("Payment", Constants.scaledImage(Constants.PAYMENT_ICON), e -> dashboardPanel.getMainScreen().showPayment());
        buttonPanel.addButton("Sales Report", Constants.scaledImage(Constants.SALES_REPORT_ICON), e -> dashboardPanel.getMainScreen().showSalesReport());
        buttonPanel.addButton("Staff", Constants.scaledImage(Constants.STAFF_ICON), e -> dashboardPanel.getMainScreen().showStaff());
        buttonPanel.addButton("POS", Constants.scaledImage(Constants.POS_ICON), e -> dashboardPanel.getMainScreen().showPOS());
    }


}
