package main.App.Main;
import javax.swing.*;

import main.App.Main.MainPanels.*;
import main.Objects.User;

import java.awt.*;

public class MainPanel extends JPanel{
    
    private NavigationPanel navigationPanel;
    private DashboardPanel dashboardPanel;
    private MainScreenPanel mainScreenPanel;

    public MainPanel(User user){
        setLayout(new BorderLayout(0, 0));
        dashboardPanel = new DashboardPanel();
        dashboardPanel.setBackground(Color.decode("#08145c"));
        add(dashboardPanel, BorderLayout.WEST);
        mainScreenPanel = new MainScreenPanel(dashboardPanel);
        add(mainScreenPanel, BorderLayout.CENTER);
        dashboardPanel.setMainScreen(mainScreenPanel);
        navigationPanel = new NavigationPanel(user, dashboardPanel);
        add(navigationPanel, BorderLayout.NORTH);
    }
}
