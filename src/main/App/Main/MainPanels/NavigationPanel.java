package main.App.Main.MainPanels;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import main.Miscellanous.Constants;
import main.Objects.User;
import main.Properties.*;
import main.Properties.CustomButton;

public class NavigationPanel extends JPanel{
    
    private CustomButton dashboardButton;
    private Labels profileLabel;

    public NavigationPanel(User user, DashboardPanel dashboardPanel){
        setLayout(new BorderLayout(10, 10));
        dashboardButton = new CustomButton("Dashboard", Constants.scaledImage(Constants.DASHBOARD_ICON));
        setBackground(dashboardButton.getBackground());
        dashboardButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                dashboardPanel.setVisible(!dashboardPanel.isVisible());
                SwingUtilities.getWindowAncestor(dashboardPanel).revalidate();
                SwingUtilities.getWindowAncestor(dashboardPanel).repaint();
            }
        });
        add(dashboardButton,BorderLayout.WEST);
        profileLabel = new Labels(user.getUsername(), Constants.scaledImage(Constants.USER_ICON));
        add(profileLabel, BorderLayout.EAST);
    }
}
